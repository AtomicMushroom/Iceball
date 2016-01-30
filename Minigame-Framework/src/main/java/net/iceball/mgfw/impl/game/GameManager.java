package net.iceball.mgfw.impl.game;

import com.sun.istack.internal.Nullable;
import net.iceball.mgfw.api.arena.Arena;
import net.iceball.mgfw.api.game.GameStatus;
import net.iceball.mgfw.api.game.config.MGTeamsModifier;
import net.iceball.mgfw.api.game.events.GameJoinEvent;
import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.arena.ArenaLoader;
import net.iceball.mgfw.impl.game.signs.GameSignManager;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import network.iceball.data.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.logging.Level;

/**
 * Created by Floris on 21-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class manages all the names             //
//  it creates names when the time comes.        //
///////////////////////////////////////////////////
public class GameManager implements Listener {

    private final GameSettings settings;
    private final String log = "[GameManager]: ";
    private static boolean started = false;

    private String[] maps;
    private JavaPlugin plugin;
    private BukkitTask updater;
    private HashMap<String, GameImpl> games = new HashMap<>();

    public GameManager(GameSettings settings, JavaPlugin plugin) {
        this.settings = settings;
        this.plugin = plugin;
    }

    /**
     * Starts the Game Launcher!
     */
    public void start() {
        if (!started)
            plugin.getLogger().log(Level.FINEST, log + "Starting!");
        if (settings == null) {
            plugin.getLogger().log(Level.SEVERE, log + "Error! No settings set!");
            return;
        }
        Bridge.load(new Config(ConfigFile.config_yml, (MinigameFramework) plugin).getConfig().getString("servername"));
        getWorkingArenas();
        updateTeams();
        initialiseMaps();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        started = true;
        updater = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(plugin, 0, 50);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        UUID uuid = new Profile(name).getUUID();
        event.setJoinMessage("");
        String arena = getGame(uuid);
        if (arena == null) { // player is not registered in games
            String map = Bridge.getChosenMap(name); // player is searching for a game
            if (map != null) {
                if (games.keySet().contains(map)) { // does the requested map exist as game?
                    final GameJoinEvent gameJoinEvent = new GameJoinEvent(event.getPlayer(), games.get(map), true);
                    Bukkit.getPluginManager().callEvent(gameJoinEvent);
                    if (gameJoinEvent.isCancelled()) {
                        return;
                    }
                    games.get(map).join(event.getPlayer().getName(), uuid);
                    return;
                } else {
                    player.sendMessage(ChatColor.RED + "The game you were asking for could not be found!");
                    new Teleporter(plugin, player.getName()).joinLobby();
                    return;
                }
            }
            player.sendMessage(ChatColor.RED + "The game you were asking for could not be found!");
            new Teleporter(plugin, player.getName()).joinLobby();
        } else {
            if (games.get(arena) != null) { // player was already playing and left before.
                final GameJoinEvent gameJoinEvent = new GameJoinEvent(event.getPlayer(), games.get(arena), false);
                Bukkit.getPluginManager().callEvent(gameJoinEvent);
                if (gameJoinEvent.isCancelled()) {
                    return;
                }
                games.get(arena).updatePlayer(event.getPlayer());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        String name = event.getPlayer().getName();
        UUID uuid = new Profile(name).getUUID();
        String arenaName = getGame(uuid);
        if (arenaName != null) {
            if (games.get(arenaName) == null) {
                return;
            }
            games.get(arenaName).leave(event.getPlayer().getName());
        }
    }

    /**
     * Updates the game information in the database. Should be called often, otherwise players will keep joining
     * the server, because the {@link GameSignManager} will otherwise not see your games.
     * players.
     */
    private void update() {
        List<String> arrayList = new ArrayList<>();
        for (GameImpl game : games.values()) {
            if (game.getArena().getStatus().equals(GameStatus.WAITING)) {
                arrayList.add(game.getArena().getName() + "@" + game.getOnlinePlayers().size() + "/" + game.getArena().getMaxPlayers());
            }
        }
        String[] maps = new String[arrayList.size()];
        maps = arrayList.toArray(maps);
        Bridge.updateGame(settings.name, maps, settings.multimode);
    }

    /**
     * Gets the names of all arenas that are ready to play with!
     */
    private void getWorkingArenas() {
        List<String> names = new ArrayList<>();
        for (String name : ArenaLoader.getLoader().getArenasNames()) {
            Arena arena = ArenaLoader.getLoader().getArena(name);
            if (arena.isComplete()) {
                names.add(arena.getName());
            }
        }
        maps = names.toArray(new String[names.size()]);
    }

    /**
     * Initialises all the maps, and says we are ready to go go! Players will be able to join after this method.
     */
    private void initialiseMaps() {
        List<String> maps_player = new ArrayList<>();
        for (int i = 0; i < maps.length; i++) {
            Arena arena = ArenaLoader.getLoader().getArena(maps[i]);
            games.put(maps[i], new GameImpl(arena, plugin, settings));
            maps_player.add(maps[i] + "@0/" + arena.getMaxPlayers());
        }
        String[] maps = new String[maps_player.size()];
        maps = maps_player.toArray(maps);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty peaceful");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty normal");
        Bridge.setupGame(settings.name, maps, settings.multimode);
    }

    /**
     * Sets all the new values set values for all teams that were set in the config.
     */
    private void updateTeams() {
        for (String name : maps) { //For each Arena
            for (UnaryOperator<MGTeamsModifier> operator : settings.team_actions) {
                Objects.requireNonNull(operator);
                Arena arena = ArenaLoader.getLoader().getArena(name);
                final ListIterator<MGTeamsModifier> li = MGTeamsModifier.getIterator(arena.getTeams());
                while (li.hasNext()) {
                    li.set(operator.apply(li.next()));
                }
            }
        }
    }

    /**
     * Gets the name of the map were the player is registered in.
     *
     * @return name of map otherwise null
     */
    private @Nullable String getGame(UUID uuid) {
        for (GameImpl game : games.values()) {
            if (game.isRegistered(uuid)) {
                return game.getArena().getName();
            }
        }
        return null;
    }
    /*
     *[16:47:43 ERROR]: Could not pass event BlockBreakEvent to MG-Framework v0.9
org.bukkit.event.EventException
        at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:310) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:62) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.plugin.SimplePluginManager.fireEvent(SimplePluginManager.java:502) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:487) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerInteractManager.breakBlock(PlayerInteractManager.java:286) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerInteractManager.a(PlayerInteractManager.java:121) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnection.a(PlayerConnection.java:623) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInBlockDig.a(SourceFile:40) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInBlockDig.a(SourceFile:10) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnectionUtils$1.run(SourceFile:13) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [?:1.8.0_66]
        at java.util.concurrent.FutureTask.run(FutureTask.java:266) [?:1.8.0_66]
        at net.minecraft.server.v1_8_R3.SystemUtils.a(SourceFile:44) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.B(MinecraftServer.java:714) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.DedicatedServer.B(DedicatedServer.java:374) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.A(MinecraftServer.java:653) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.run(MinecraftServer.java:556) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_66]
Caused by: java.util.ConcurrentModificationException
        at java.util.LinkedList$ListItr.checkForComodification(LinkedList.java:966) ~[?:1.8.0_66]
        at java.util.LinkedList$ListItr.next(LinkedList.java:888) ~[?:1.8.0_66]
        at net.iceball.mgfw.impl.game.signs.GameSignManager.removeSign(GameSignManager.java:103) ~[?:?]
        at net.iceball.mgfw.impl.game.signs.GameSignManager.onSignBroke(GameSignManager.java:175) ~[?:?]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_66]
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_66]
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_66]
        at java.lang.reflect.Method.invoke(Method.java:497) ~[?:1.8.0_66]
        at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:306) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        ... 17 more
[16:47:43 INFO]: [MG
Removing while something is else is also being removed
     */

}
