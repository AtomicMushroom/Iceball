package net.iceball.mgfw.impl.game.signs;

import com.sun.istack.internal.Nullable;
import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.game.Teleporter;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import net.iceball.mgfw.impl.util.ObjectInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;
import java.util.logging.Level;

/**
 * Created by Floris on 01-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public class GameSignManager implements Listener, Runnable {

    private HashMap<GameServer, Set<MapInfo>> servers() { return SignsInformant.getGameServers(); }
    private List<GameSign> signs;
    private MinigameFramework plugin;
    private Config config;

    public GameSignManager(MinigameFramework plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("Gamesigns enabled!");
        config = new Config(ConfigFile.signs_yml, this.plugin);
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        signs = loadSigns();
        mapsFilter = new LinkedHashSet<>();
        singlemodeServers = new LinkedHashSet<>();
    }

    @Override
    public void run() {
        mapsFilter.clear();
        singlemodeServers.clear();
        for (GameSign sign : signs) {
            //plugin.getLogger().info("sign.isIdle(): " + sign.isIdle() + "| Status: " + sign.getStatus().name());
            if (sign.isIdle()) { // is the sign not hosting a game?
                if (gameExist(sign.getGame())) { // does the game that the sign has exist?
                    MapInfo info = findGame(sign.getGame());
                    if (info != null) {
                        sign.activate(info);
                        continue;
                    }
                    sign.display(Status.SEARCHING_GAMES);
                    continue;
                }
                sign.display(Status.SEARCHING_SERVERS);
                continue;
            } else if (!sign.map.isMultimode()) {
                sign.update(getSingleModeGame(sign.getID()));
                continue;
            }
            sign.update(getMultiModeGame(sign.getID()));
        }
    }

    /**
     * Adds a new GameSign to the GameSignManager.
     *
     * @param sign The sign that will be registered.
     * @param game The game the sign will search for.
     */
    public void addSign(Sign sign, String game) {
        GameSign gameSign = new GameSign(game, sign, plugin);
        gameSign.serialize();
        //config.getConfig().set("gamesigns.blocks.B" + (getHighestSignID() + 1), save);
        //config.saveConfig();
        signs.add(gameSign);
    }

    /**
     * Removes the GameSign located on the given location.
     *
     * @param location the given location of the sign.
     * @return True when the sign is successfully removed, otherwise false.
     */
    public boolean removeSign(Location location) {
        if (location == null) {
            return false;
        } else {
            boolean contains = false;
            Iterator<GameSign> iterator = signs.iterator();
            while (iterator.hasNext()) {
                GameSign sign = iterator.next(); // it looks like it was null here before
                if (sign != null && sign.getSign().getBlock().getLocation().equals(location)) {
                    signs.remove(sign);
                    contains = true;
                }
            }
            config.getConfig().set("signs", null);
            config.saveConfig();
            for (int i = 0; i < signs.size(); i++) {
                //String save = Serialization.serializeSign(signs.get(i));
                signs.get(i).serialize();
                //config.getConfig().set("signs.S" + (i + 1), save);
            }
            return contains;
        }
    }

    /**
     * Loads all the signs from the config file!
     *
     * @return List of all GameSigns!
     */
    public List<GameSign> loadSigns() {
        int highest = getHighestSignID();
        List<GameSign> signs = new LinkedList<>();
        if (highest == 0) {
            return new LinkedList<>();
        }

        for (int i = 1; i < (highest + 1); i++) {
            if (config.getConfig().isSet("signs.S" + i)) {
                String sign = config.getConfig().getString("signs.S" + i);
                if (sign != null && !sign.isEmpty()) {
                    signs.add(GameSign.deserialize(sign, plugin));
                }
            }
        }
        return signs;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignPlace(SignChangeEvent event) {
        Player player = event.getPlayer();
        String permissions = "mg.gamesigns";
        String line0 = "[Minigame]";

        if (event.getPlayer().hasPermission(permissions)) {
            if (event.getLine(0).equals(line0)) {
                String game = event.getLine(2);
                if (!game.isEmpty()) {
                    addSign((Sign) event.getBlock().getState(), game);
                    player.sendMessage(MinigameFramework.prefix + "Sign with "+ MinigameFramework.mark + game + MinigameFramework.suffix + " as game is successfully added!");
                    return;
                }
                player.sendMessage(MinigameFramework.prefix + ChatColor.RED + "You have to set the name of the game at line 3!");
                event.getBlock().breakNaturally();
            }
        } else {
            if (event.getLine(0).equals(line0)) {
                event.getBlock().breakNaturally();
                event.getPlayer().sendMessage(MinigameFramework.prefix + ChatColor.RED + "You don't have " + permissions + "!");
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignBroke(BlockBreakEvent event) {
        plugin.getLogger().info("break");
        Player player = event.getPlayer();
        if (player.hasPermission("mg.gamesigns")) {
            if (event.getBlock().getState() instanceof Sign) {
                Sign sign = (Sign) event.getBlock().getState();
                if (removeSign(sign.getLocation())) {
                    player.sendMessage(MinigameFramework.prefix + "Sign at " + MinigameFramework.mark
                            + ObjectInfo.blockDescription(sign.getLocation()) + MinigameFramework.suffix + " removed!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null && block.getState() != null)
        if (block.getState() instanceof Sign) {
            GameSign sign = getGameSign(block.getLocation());
            if (sign != null) { //Sign is registered.
                switch (event.getAction()) {
                    case LEFT_CLICK_BLOCK:
                        sign.leftClick(player.getName(), player.isSneaking());
                        break;
                    case RIGHT_CLICK_BLOCK:
                        sign.rightClick(player.getName(), player.isSneaking());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Gets the amount of blocks that were set in the config.
     *
     * @return 0, or the highest number block set.
     */
    private int getHighestSignID() {
        Set<String> block = new HashSet<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(this.config.getFile());
        if (config.getString("signs.S1") == null) {
            return 0;
        }
        for (String key : config.getConfigurationSection("signs").getKeys(false)) {
            block.add(key);
        }
        return block.size();
    }

    /**
     * Whether this game exists or not.
     *
     * @param game The game that will be checked
     * @return True when the game exists, otherwise false.
     */
    private boolean gameExist(String game) {
        HashMap<GameServer, Set<MapInfo>> servers = servers();
        if (servers == null) {
            plugin.getLogger().log(Level.WARNING, "Could not get to access to table in database. Trying to create table.. ");
            MinigameFramework.createTable();
            plugin.getLogger().log(Level.FINE, "Done!");
            servers = servers();
            if (servers == null) {
                plugin.getLogger().log(Level.WARNING, "Something went wrong with getting our database connection! Server is shutting down!");
                Bukkit.getOnlinePlayers().stream().filter(player -> player != null).forEach(player -> {
                    new Teleporter(plugin, player.getName()).joinLobby();
                });
                System.exit(-1);
                return false;
            }
        } else
        for (GameServer server : servers.keySet()) {
            if (server.getGame().equals(game)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the GameSign that is registered on the given location.
     *
     * @param location the given location
     * @return the GameSign or Null when there is no GameSign registered at that location.
     */
    private @Nullable GameSign getGameSign(Location location) {
        for (GameSign sign : signs) {
            if (sign.getSign().getLocation().equals(location)) {
                return sign;
            }
        }
        return null;
    }

    private MapInfo getMultiModeGame(String id) {
        for (Set<MapInfo> mapInfoSet : servers().values()) {
            for (MapInfo map : mapInfoSet) {
                if (map.getID().equals(id)) {
                    if (mapsFilter.contains(map.getID())) {
                        return null;
                    }
                    mapsFilter.add(map.getID());
                return map;
                }
            }
        }
        return null; //no games with that given id, the game is dead/full.
    }
    private MapInfo getSingleModeGame(String id) {
        for (Set<MapInfo> mapInfoSet : servers().values()) {
            for (MapInfo map : mapInfoSet) {
                if (map.getID().equals(id) && !map.isMultimode()) { //singlemode true: multimode=false
                    if (singlemodeServers.contains(map.getServer())){
                        return null;
                    }
                    singlemodeServers.add(map.getServer());
                    return map;
                }
            }
        }
        return null; //no games with that given id, the game is dead/full.
    }

    private Set<String> mapsFilter;
    private Set<String> singlemodeServers;
    private @Nullable MapInfo findGame(String game) {
        for (Map.Entry<GameServer, Set<MapInfo>> entry : servers().entrySet()) {
            if (entry.getKey().getGame().equals(game)) {

                if (!entry.getKey().isMultimode()) { //singlemode
                    String server = entry.getKey().getServer();
                    if (!singlemodeServers.contains(server) && !entry.getValue().isEmpty()) {

                        List<MapInfo> mapList = new ArrayList<>(entry.getValue());
                        Collections.shuffle(mapList);
                        MapInfo map = mapList.get(0);
                        singlemodeServers.add(server); //is in singlemode
                        return map; //return random map
                    }
                    return null;
                } else {
                    for (MapInfo map : entry.getValue())
                        if (!map.getMap().isEmpty() && !mapsFilter.contains(map.getID())) {
                            mapsFilter.add(map.getID());
                            return map;
                        }
                }
            }
        }
        return null; //there no games right now
    }
}
