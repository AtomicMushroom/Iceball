package net.iceball.mgfw.impl.game;

import com.google.common.base.Preconditions;
import net.iceball.mgfw.api.arena.Arena;
import net.iceball.mgfw.api.arena.MGTeam;
import net.iceball.mgfw.api.game.Game;
import net.iceball.mgfw.api.game.events.GameSetupEvent;
import net.iceball.mgfw.api.game.GameStatus;
import network.iceball.data.Profile;
import network.iceball.permissions.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Created by Floris on 06-08-15
 *
 * This is the class, you've been waiting for.  This is the one and only, GameImpl.java :D
 */
class GameImpl implements Game {

    private Arena arena;
    private HashMap<UUID, String> players; //uuid:players
    private BukkitTask counter;
    private JavaPlugin plugin;
    private TeamManager teamManager;
    private GameSettings settings;
    private HashMap<UUID, Interval> joinLeaveInterval;
    private final ChatColor P = ChatColor.YELLOW;

    protected GameImpl(Arena arena, JavaPlugin plugin, GameSettings settings) {
        this.plugin = plugin;
        this.arena = arena;
        this.settings = settings;
        players = new HashMap<>();
        joinLeaveInterval = new HashMap<>();
        teamManager = new TeamManager(plugin, arena.getTeams(), settings);
        setup();
    }

    /**
     * Adds the player to the game, assuming that this is the first time that they join the game.
     *
     * @param name Name of player.
     * @param uuid UUID of player.
     */
    public void join(String name, UUID uuid) {
        Player player = Bukkit.getPlayer(name);
        String prefix = Permissions.getInstance().getPlayerManager().getPlayer(name).getPrefix();
        String suffix = Permissions.getInstance().getPlayerManager().getPlayer(name).getSuffix();
        switch (arena.getStatus()) {
            case WAITING:
                players.put(uuid, name);
                broadcast(prefix + " " + name + ChatColor.DARK_AQUA + " joined the game! "
                        + ChatColor.GOLD + "(" + players.size() + "/" + arena.getMaxPlayers() + ")");
                if (settings.joinTeamInstantly) {
                    teamManager.joinTeam(name);
                }
                switch (settings.autoTeleportTo) {
                    case SPAWN:
                        MGTeam team = teamManager.getTeam(name);
                        if (team.isMainStartSpawn()) {
                            player.teleport(team.getSpawns()[0]);
                            break;
                        }
                        int idx = new Random().nextInt(team.getSpawns().length);
                        player.teleport(team.getSpawns()[idx]);
                        break;
                    case LOBBY:
                        player.teleport(arena.getLobby());
                        break;
                }
                if (players.size() >= settings.minimumPlayers) {
                    countdown();
                }
                break;
            case STARTED:
                if (isRegistered(uuid)) {
                    Interval interval = joinLeaveInterval.get(uuid);
                    if (interval != null && interval.isCompleted()){
                        players.remove(uuid);
                        player.setGameMode(GameMode.SPECTATOR);
                        if (!player.hasPermission("mg.invisible")) {
                            broadcast(prefix + " " + name + suffix + " is now spectating.");
                        }
                        player.sendMessage(P + "It took you way too long, you've been set in spectator-mode.");
                    }
                    broadcast(prefix + " " + name + suffix + " joined the game! ");
                    players.put(uuid, player.getName());
                } else {
                    player.teleport(arena.getSpectator());
                    player.setGameMode(GameMode.SPECTATOR);
                    if (!player.hasPermission("mg.invisible")) {
                        broadcast(prefix + " " + name + suffix + " is now spectating.");
                    }
                    //TODO: add private chat to add players
                }
                break;
            case ENDED:
                player.teleport(arena.getSpectator());
                player.setGameMode(GameMode.SPECTATOR);
                new Teleporter(plugin, player.getName()).joinLobby();
                break;
            default:
                break;
        }
    }

    /**
     * Removes the player from the game. The player will however not get deleted with this method.
     *
     * @param name The name of the player.
     */
    public void leave(String name) {
        UUID uuid = getUUID(name);
        String prefix = Permissions.getInstance().getPlayerManager().getPlayer(name).getPrefix();
        String suffix = Permissions.getInstance().getPlayerManager().getPlayer(name).getSuffix();
        switch (arena.getStatus()) {
            case WAITING:
                players.remove(getUUID(name));
                if (settings.minimumPlayers > players.size()) {
                    if (counter != null)
                        counter.cancel();
                        counter = null;
                }
                break;
            case STARTED:
                if (settings.leaveInterval != 0 && isRegistered(uuid)) {
                    joinLeaveInterval.put(uuid, new Interval(plugin));
                }
                broadcast(prefix + " " + name + suffix + " left the game.");
                break;
            case ENDED:
                broadcast(prefix + " " + name + suffix + " left the game.");
                break;
        }
    }

    /**
     * Gets the arena where the game is playing in.
     *
     * @return The arena which the game is using.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets all players UUID's registered in the game.
     *
     * @return All registered UUIDs of the players in the game.
     */
    public Set<UUID> getPlayers() {
        return players.keySet();
    }

    /**
     * Gets all the online players of this game!
     *
     * @return A set of names of the online players of the game!
     */
    public Set<String> getOnlinePlayers() {
        return this.players.values().stream().filter(player -> Bukkit.getPlayer(player) != null).collect(Collectors.toSet());
    }

    /**
     * Whether the player is already registered in the game or not.
     *
     * @param uuid UUID of the player
     * @return True when the player is registered, otherwise false.
     */
    public boolean isRegistered(UUID uuid) { //
        return players.keySet().contains(uuid);
    }

    /**
     * Send a message to all players in the arena.
     */
    public void broadcast(String message) {
        for (String name : players.values()) {
            Player player = Bukkit.getServer().getPlayer(name);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * This method gets called when the player is registered in the game, however left first and then joined back in.
     *
     * @param player the player who joined back in
     */
    public void updatePlayer(Player player) {
        switch (arena.getStatus()) {
            case WAITING:
                break;
            case STARTED:
                break;
            case ENDED:
                break;
            default:
                break;
        }
    }

    private void setup() {
        arena.setStatus(GameStatus.WAITING);
        Bukkit.getPluginManager().callEvent(new GameSetupEvent(this));
        Interval.seconds = settings.leaveInterval;
    }

    public void start() {
        counter.cancel();
        arena.setStatus(GameStatus.STARTED);
        Set<String> online_players = getOnlinePlayers();
        teamManager.finishTeams(online_players);
        for (Iterator<String> iterator = players.values().iterator(); iterator.hasNext(); ) {
            String name = iterator.next();
            if (!online_players.contains(name)) {
                removePlayer(name);
                break;
            }
            MGTeam team = teamManager.getTeam(name);
            Player player = Bukkit.getPlayer(name);
            Preconditions.checkNotNull(player);
            if (team.isMainStartSpawn()){
                player.teleport(team.getSpawns()[0]);
            } else {
                int idx = new Random().nextInt(team.getSpawns().length);
                player.teleport(team.getSpawns()[idx]);
            }
            //spawn shits
            // we have to configure the team first, I don't know in which team the player is.
        }
    }

    private void removePlayer(String name) {
        UUID removal = null;
        for (Map.Entry<UUID, String> entry : players.entrySet()) {
            if (entry.getValue().equals(name)) {
                removal = entry.getKey();
                break;
            }
        }
        players.remove(removal);
    }

    @Override
    public void end() {

    }

    private void countdown() {
        counter = new BukkitRunnable() {
            int i = settings.countdown;

            @Override
            public void run() {
                i--;
                boolean isDivisibleBy10 = i % 10 == 0;
                if (i == settings.countdown || isDivisibleBy10 || i < 10 || i == 15) {
                    broadcast(ChatColor.GREEN + "Game is starting in " + i + " seconds..");
                    for (String name : getOnlinePlayers()) {
                        Player player = Bukkit.getPlayer(name);
                        if (player != null) {
                            player.playEffect(player.getLocation(), Effect.CLICK2, null);
                        }
                    }
                }
                if (i == 1) {
                    start();
                }
            }
        }.runTaskTimer(plugin, 5, 20);
    }

    private UUID getUUID(String name) {
        for (Map.Entry<UUID, String> entry : players.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return new Profile(name).getUUID();
    }
}
class Interval {

    public static int seconds;
    private boolean complete;
    private BukkitTask task;
    private JavaPlugin plugin;

    public Interval(JavaPlugin plugin){
        this.complete = false;
        this.plugin = plugin;
        task = new BukkitRunnable(){
            @Override
            public void run() {
                complete = true;
            }
        }.runTaskLater(plugin, seconds);
    }

    public boolean isCompleted(){ return complete; }
}
/*
:27 INFO]: Displayname: text-1 | [k]
[17:42:27 INFO]: Maerec[/192.168.2.67:44002] logged in with entity id 56 at ([World]-109.0, 64.0, 82.0)
[17:42:28 WARN]: Couldn't look up profile properties for com.mojang.authlib.GameProfile@210ea161[id=0652b9cd-3e80-4c5d-a8a2-58d37e333fab,name=Maerec,properties={},legacy=false]
com.mojang.authlib.exceptions.AuthenticationException: The client has sent too many requests within a certain amount of time
        at com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService.makeRequest(YggdrasilAuthenticationService.java:65) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService.fillGameProfile(YggdrasilMinecraftSessionService.java:175) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService.fillProfileProperties(YggdrasilMinecraftSessionService.java:168) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$1.load(TileEntitySkull.java:70) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$1.load(TileEntitySkull.java:1) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LoadingValueReference.loadFuture(LocalCache.java:3524) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.loadSync(LocalCache.java:2317) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2280) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2195) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache.get(LocalCache.java:3934) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache.getOrLoad(LocalCache.java:3938) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LocalLoadingCache.get(LocalCache.java:4821) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LocalLoadingCache.getUnchecked(LocalCache.java:4827) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$3.run(TileEntitySkull.java:172) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [?:1.8.0_66]
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [?:1.8.0_66]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_66]
[17:42:39 INFO]: !!OWNER!! Thradrys: echt null
[17:42:39 INFO]: Mr_RedSheep issued server command: /locate Maerec
[17:42:41 INFO]: !!OWNER!! Thradrys: niks
[17:42:48 INFO]: !!OWNER!! Thradrys: overigens
[17:42:50 INFO]: [Admin] Mr_RedSheep: lol
[17:42:52 INFO]: !!OWNER!! Thradrys: hij werkt niet hehelaal
[17:42:56 WARN]: [MG-Framework] Task #12 for MG-Framework v0.9 generated an exception
java.lang.IllegalArgumentException: Player cannot join a single team! All teams are full!
        at com.google.common.base.Preconditions.checkArgument(Preconditions.java:125) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.iceball.mgfw.impl.game.TeamManager.joinTeam(TeamManager.java:56) ~[?:?]
        at net.iceball.mgfw.impl.game.TeamManager.finishTeams(TeamManager.java:87) ~[?:?]
        at net.iceball.mgfw.impl.game.GameImpl.start(GameImpl.java:223) ~[?:?]
        at net.iceball.mgfw.impl.game.GameImpl$1.run(GameImpl.java:278) ~[?:?]
        at org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftTask.run(CraftTask.java:71) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.craftbukkit.v1_8_R3.scheduler.CraftScheduler.mainThreadHeartbeat(CraftScheduler.java:350) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.B(MinecraftServer.java:722) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.DedicatedServer.B(DedicatedServer.java:374) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.A(MinecraftServer.java:653) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.run(MinecraftServer.java:556) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_66]
[17:42:57 INFO]: [MG-Framework] Timout: 0

 */
