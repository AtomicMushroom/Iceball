package net.iceball.mgfw.impl.game.signs;

import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.game.Teleporter;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by Floris on 03-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class GameSign {

    private int dots = 0;

    private final String game;
    private final Sign sign;
    private final MinigameFramework plugin;

    private int timeout = 0;
    private HashMap<String, Integer> reminder;
    private String inUse = null;
    private String selected_map = "-";

    public MapInfo map = null;
    private Status status = Status.SEARCHING_SERVERS;

    /**
     * The constructor used to create the GameSign.
     * @param game The name of the game the sign will host.
     * @param sign The sign that will display the text.
     * @param plugin JavaPlugin instance.
     */
    public GameSign(String game, Sign sign, MinigameFramework plugin) {
        this.game = game;
        this.sign = sign;
        this.plugin = plugin;
        reminder = new HashMap<>();
    }

    /**
     * Activates the sign, which will turn it into a working gamesign. At this moment it will display the map!
     * @param map the map that will be shown
     */
    public void activate(MapInfo map) {
        if (map != null) {
            this.map = map;
            if (map.isMultimode()) {
                display(Status.JOIN);
            } else {
                display(Status.NEW);
            }
            reminder = new HashMap<>();
        }
    }

    /**
     * Once the gamesign is activated, the information in the sign needs to be updated each second!
     * @param game Our updated game.
     */
    public void update(MapInfo game) {
        if (!isIdle()) {
            if (game != null) {
                if (getID().equals(game.getID())) {
                    this.map = game;
                    if (map.isMultimode()) {
                        display(Status.JOIN);
                        return;
                    }
                    display(Status.NEW);
                }
            } else { // the id of the game is null/full
                if (map != null) { // is it hosting?
                    if (timeout >= 3) {
                        display(Status.SEARCHING_GAMES);
                        timeout = 0;
                    } else if (map.isMultimode()) {
                        display(Status.FULL);
                        timeout = timeout + 1;
                        plugin.getLogger().info("Timout: " + dots);
                    }
                }
            }
        }
    }

    /**
     * Display all the information the gamesign got us to tell.
     * @param status The status of the gamesign.
     */
    public void display(Status status){
        this.status = status;
        if (sign != null)
        switch (status){
            case SEARCHING_GAMES:
                sign.setLine(0, ChatColor.DARK_PURPLE + "[SEARCHING]");
                sign.setLine(1, ChatColor.LIGHT_PURPLE + "Searching for");
                sign.setLine(2, ChatColor.LIGHT_PURPLE + "games" + animatedDots());
                sign.setLine(3, ChatColor.BLACK + " ");
                sign.update();
                break;
            case SEARCHING_SERVERS:
                sign.setLine(0, ChatColor.DARK_PURPLE + "[SEARCHING]");
                sign.setLine(1, ChatColor.LIGHT_PURPLE + "Looking for");
                sign.setLine(2, ChatColor.LIGHT_PURPLE  + getGame());
                sign.setLine(3, ChatColor.LIGHT_PURPLE + "servers" + animatedDots());
                sign.update();
            case JOIN:
                if (map != null) {
                    sign.setLine(0, ChatColor.DARK_GREEN + "[JOIN]");
                    sign.setLine(1, ChatColor.DARK_BLUE + "" + ChatColor.UNDERLINE + map.getMap());
                    sign.setLine(2, ChatColor.DARK_RED + "Waiting" + animatedDots()); //TODO: add ability to see which &b server
                    String format = map.getOnlinePlayers() + "/" + map.getPlayers();
                    sign.setLine(3, ChatColor.BLACK + format + " Players");
                    sign.update();
                }
                break;
            case FULL:
                if (map != null) {
                    sign.setLine(0, ChatColor.DARK_RED + "[FULL]");
                    sign.setLine(1, ChatColor.RED + "" + ChatColor.UNDERLINE + map.getMap());
                    sign.setLine(2, ChatColor.DARK_BLUE + "Right-click " + ChatColor.BLACK + "to");
                    sign.setLine(3, ChatColor.BLACK + "Spectate!");
                    sign.update();
                }
                break;
            case NEW:
                sign.setLine(0, ChatColor.DARK_BLUE + "[NEW]");
                sign.setLine(1, ChatColor.DARK_PURPLE + "Select Map: ");
                sign.setLine(2, ChatColor.BLACK + selected_map);
                sign.setLine(3, ChatColor.DARK_BLUE + "Rank required!");
                sign.update();
                break;
        }
    }

    /**
     * This method is triggered when somebody right-clicks the sign.
     * @param name Name of player.
     * @param sneaking Whether the player was sneaking or not when he clicked.
     */
    public void rightClick(String name, boolean sneaking) {
        plugin.getLogger().info("RIGHT CLICK");
        Player player = Bukkit.getPlayer(name);
        if (player != null)
        switch (status.getI()) {
            case 0:
                if (reminder.containsKey(name)){
                    if (reminder.get(name) == 3){
                        player.setVelocity(new Vector(0, 16, 0));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(ChatColor.YELLOW + "Thanks for your patience.");
                            }
                        }.runTaskLater(plugin, 36l);
                        reminder.remove(name);
                    } else if (reminder.get(name) == 1){
                        player.sendMessage(ChatColor.YELLOW + "Stop clicking me please.");
                        reminder.put(name, 2);
                    } else if (reminder.get(name) == 2){
                        player.sendMessage(ChatColor.YELLOW + "This is my last warning.");
                        reminder.put(name, 3);
                    }
                } else {
                    reminder.put(player.getName(), 1);
                    player.sendMessage(ChatColor.YELLOW + "Have some patience please!");
                }
                break;
            case 1:
                SignsInformant.setChosenMap(map.getMap(), name);
                new Teleporter(plugin, player.getName()).joinServer(map.getServer());

                map.addOnlinePlayer();
                sign.setLine(0, ChatColor.DARK_GREEN + "[JOIN]");
                sign.setLine(1, ChatColor.DARK_BLUE + map.getMap());
                sign.setLine(2, ChatColor.DARK_RED + "Waiting" + animatedDots());
                String format = map.getOnlinePlayers() + "/" + map.getPlayers();
                sign.setLine(3, ChatColor.BLACK + format + " Players");
                break;
            case 2:
                if(player.hasPermission("mg.spectate")) {
                    SignsInformant.setChosenMap("spectate: " + map.getMap(), name); //TODO: add support for this
                    new Teleporter(plugin, player.getName()).joinServer(map.getServer());
                    //sendMessage(player, new String[]{"Connect", map.getServer()});
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have a rank!");
                }
                break;
            case 3:
                newMap(name, sneaking, true);
            default:
                break;
        }
    }

    /**
     * This method is triggered when somebody left-clicks the sign.
     * @param name Name of player.
     * @param sneaking Whether the player was sneaking or not when he clicked.
     */
    public void leftClick(String name, boolean sneaking) {
        Player player = Bukkit.getPlayer(name);
        plugin.getLogger().info("LEFT CLICK");
        if (player != null)
        switch (status) {
            case NEW:
                newMap(name, sneaking, false);
                break;
            default:
                break;
        }
    }


    private void newMap(String name, boolean sneaking, boolean forward){
        Player player = Bukkit.getPlayer(name);
        if (player != null)
        if (player.hasPermission("mg.newgame")) {
            if (inUse == null) {
                inUse = player.getName();
                player.sendMessage(ChatColor.GREEN + "Right-click for next map, left-click to go back.");
                player.sendMessage(ChatColor.GREEN + "Sneak and click to select the map!");
                //while (inUse != null){ sign.setLine(3, ChatColor.BLACK + " ");} //TODO: update this
            } else if (!inUse.equals(player.getName())){
                player.sendMessage(ChatColor.RED + "Only one player at a time can select the map!");
            } else
            if (sneaking){
                confirmMap();
            } else
                nextMap(forward);
            display(Status.NEW);
        }
    }

    private void confirmMap(){
        map.setMap(selected_map);
        if (SignsInformant.doesMapExist(map.getServer(), selected_map)){

        }
        // communicating.. check if map is still available
        // TODO: work on this
        //TODO: first work on the other stuff
        /* the join mechanism doesnt work right know
        The prefix chat at the join is not translated
        I am directly sended back to a lobby server i was.
        Further more. The Searching for Server status has to be updated to something better, cause the way it displays
        the game is just ugly.
        */
        status = Status.JOIN;
    }

    private void nextMap(boolean forward){
        Set<String> newMaps = new TreeSet<>();
        newMaps.add(map.getMap());
        Collections.addAll(newMaps, map.getMaps());
        ArrayList<String> maps = new ArrayList<>(newMaps);
        if (maps.contains(selected_map)){
            int index = maps.indexOf(selected_map);
            int maxIndex = maps.size()-1;

            if (forward) {
                if (index == maxIndex){
                    selected_map = maps.get(0);
                } else {
                    selected_map = maps.get((index+1));
                }
            } else {
                if (index == 0){
                    selected_map = maps.get(maxIndex);
                } else {
                    selected_map = maps.get((index-1));
                }
            }
        } else {
            selected_map = maps.get(0);
        }
    }

    /**
     * Gets the current ID of the map.
     * @return the ID of the map or an empty string when null.
     */
    public String getID() { return map != null ? map.getID() : ""; }

    /**
     * Gets the current status of the sign.
     * @return the status of the sign.
     */
    public Status getStatus() { return status; }

    /**
     * Is the sign searching for games for games or servers?
     * @return True when the status of the sign has {@link Status#SEARCHING_GAMES} or {@link Status#SEARCHING_SERVERS}
     */
    public boolean isIdle() { return status == Status.SEARCHING_GAMES || status == Status.SEARCHING_SERVERS; }

    /**
     * Gets the game the sign will host!
     * @return The name of the game the sign will host.
     */
    public String getGame() { return game; }

    /**
     * Gets the registered sign!
     * @return the sign that is displaying the text.
     */
    public Sign getSign() { return sign; }

    public void serialize(){
        String location = "";
        Location loc = sign.getLocation();
        if (loc == null){
            location = "Not defined yet.";
        } else {
            String world = loc.getWorld().getName();
            location = String.format("X: %d, Y: %d, Z: %d, World: %s, Game: %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), world, game);
        }
        Config config = new Config(ConfigFile.signs_yml, plugin);
        config.getConfig().set("signs.S" + (getHighestSignID() + 1), location);
        config.saveConfig();
    }

    /**
     * Gets the amount of signs that were set in the config.
     *
     * @return 0, or the highest number of the set signs.
     */
    private int getHighestSignID() {
        Set<String> block = new HashSet<>();
        Config configuration = new Config(ConfigFile.signs_yml, plugin);
        FileConfiguration config = YamlConfiguration.loadConfiguration(configuration.getFile());
        if (config.getString("signs.S1") == null) {
            return 0;
        }
        for (String key : config.getConfigurationSection("signs").getKeys(false)) {
            block.add(key);
        }
        return block.size();
    }

    public static GameSign deserialize(String s, MinigameFramework plugin) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        String[] sort = s.split(",");
        String[] values = new String[5];
        for (int i = 0; i < values.length; i++) {
            values[i] = sort[i].split(":")[1].trim();
        }
        int x = Integer.valueOf(values[0]);
        int y = Integer.valueOf(values[1]);
        int z = Integer.valueOf(values[2]);
        World world = Bukkit.getServer().getWorld(String.valueOf(values[3]));
        Location location = new Location(world, x, y, z);
        String game = values[4];
        Sign sign = null;
        if (location.getBlock().getState() instanceof Sign) {
            sign = (Sign) location.getBlock().getState();
        }
        return new GameSign(game, sign, plugin);
    }

    private String animatedDots() {
        dots = dots >= 3 ? 1 : (dots + 1);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dots; i++) {
            builder.append(".");
        }
        return builder.toString();
    }
}
