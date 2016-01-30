package net.iceball.mgfw.impl.game.signs;

/**
 * Created by Floris on 02-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class MapInfo {

    private String[] maps;
    private String map;
    private String server;
    private int online_players;
    private int max_players;
    private boolean multimode = true;

    /**
     * Constructor for MapInfo
     * @param map Name of arena.
     * @param max_players Maximum players the arena can take.
     * @param online_players The players in the arena that are online.
     * @param multimode Whether the game is in single or in multimode.
     * @param server Name of server.
     */
    public MapInfo(String map, int max_players, int online_players, boolean multimode, String server) {
        this.map = map;
        this.max_players = max_players;
        this.online_players = online_players;
        this.maps = new String[0];
        this.multimode = multimode;
        this.server = server;
    }

    /**
     * Gets the map!
     * @return the name of the arena
     */
    public String getMap() {
        return map;
    }

    /**
     * Gets the maximum players the arena can take.
     * @return maximum amount of players
     */
    public int getPlayers() {
        return max_players;
    }

    /**
     * Gets the amount of online players in the arena.
     * @return the amount of online players.
     */
    public int getOnlinePlayers() { return online_players; }

    /**
     * Gets the BungeeCord registered name of the server.
     * @return the server the map is located in.
     */
    public String getServer(){
        return server;
    }

    /**
     * Gets the ID of the map.
     * @return a unique string
     */
    public String getID() {return map + max_players + server;}

    /**
     * Whether the game is in Multimode or in Singlemode.
     * @return True when in multimode, otherwise false.
     */
    public boolean isMultimode() {
        return multimode;
    }

    /**
     * If the game is in singlemode there is a big change that there are multiple maps. Well, this is the method for that.
     * @return empty array or all maps of the singlemode game.
     */
    public String[] getMaps() {
        return maps;
    }

    /**
     * Sets the MapInfo in singlemode.
     * @param maps all the maps that are included.
     */
    public void setSingleMode(String[] maps){
        this.maps = maps;
        multimode = false;
    }

    /**
     * This method is only for the use of {@link GameSign#confirmMap()} method.
     * @param map String of the selected map.
     */
    public void setMap(String map) {
        this.map = map;
    }

    /**
     * This method is only for the use of {@link GameSign#rightClick(String, boolean)} method, it is used when somebody
     * joins and the sign is updating.
     */
    public void addOnlinePlayer(){ online_players++;}
}
