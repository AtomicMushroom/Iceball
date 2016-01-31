package net.iceball.mgfw.impl.game.signs;

import network.iceball.bukkit.api.Database;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Floris on 29-11-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class SignsInformant {

    public static void setChosenMap(String map, String player){
        String cmd = "UPDATE playerdata SET chosen_map = ? WHERE player = ?;";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setString(1, map);
            ps.setString(2, player);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the registered gameservers!
     * @return HashMap with String as servername and Set MapInfo with the all the maps of that server.
     */
    public static HashMap<GameServer, Set<MapInfo>> getGameServers(){
        String cmd = "SELECT servername, free_maps, game FROM gamedata WHERE last_update > ADDDATE(NOW(), INTERVAL -10 SECOND);";
        HashMap<GameServer, Set<MapInfo>> gameservers = new HashMap<>();
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(cmd);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String servername = rs.getString("servername");
                String game = rs.getString("game");
                String mapsString = rs.getString("free_maps");
                boolean multimode = true;
                boolean skip = false;
                String parse = ",";
                if (mapsString == null || mapsString.isEmpty()){
                    skip = true;
                } else if (mapsString.contains(":")){
                    multimode = false;
                    parse = ":";
                } // Jungle@1/24,City@7/82 or Jungle@0/24:City@0/82
                if (!skip) {
                    String[] maps = mapsString.split(parse); //0: Jungle@1/24  1: City@7/82
                    int[] max_players = new int[maps.length];
                    int[] online_players = new int[maps.length];

                    for (int i=0; i < max_players.length; i++){
                        int playerMax = Integer.valueOf(maps[i].split("@")[1].split("/")[1]);//Jungle 1/24
                        int playerOnline = Integer.valueOf(maps[i].split("@")[1].split("/")[0]);
                        max_players[i] = playerMax;
                        online_players[i] = playerOnline;
                    }
                    Set<MapInfo> mapInfoSet = new LinkedHashSet<>();
                    for (int i=0; i < maps.length; i++) {
                        maps[i] = maps[i].split("@")[0];
                    }
                    Set<String> serverFilter = new LinkedHashSet<>();
                    for (int i=0; i < maps.length; i++){
                        MapInfo info = new MapInfo(maps[i], max_players[i], online_players[i], multimode, servername); //info.setsm check
                        if (multimode){
                            mapInfoSet.add(info);
                        } else if (!serverFilter.contains(info.getServer())){
                            info.setSingleMode(maps);
                            mapInfoSet.add(info);
                            serverFilter.add(info.getServer());
                        }
                    }
                    gameservers.put(new GameServer(multimode, servername, game), mapInfoSet);
                }
                /*for (Map.Entry<GameServer, Set<MapInfo>> entry : gameservers.entrySet()) {
                    MinigameFramework.logger.info("Server: " + entry.getKey().getServer() + " Multimode: " + entry.getKey().isMultimode() + " Game: " + entry.getKey().getGame());
                    MinigameFramework.logger.info("Maps: " + entry.getValue().size());
                }*/
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedOperationException soe){
            soe.printStackTrace();
            return null;
        }
        return gameservers;
    }
    //TODO: when a server is offline, it takes too long. Interval has to be shorten to 10s at least.
    public static boolean doesMapExist(String server, String selected_map) {
        String cmd = "SELECT free_maps FROM gamedata WHERE free_maps = ? AND last_update > ADDDATE(NOW(), INTERVAL -1 MINUTE);";
        String map = "";
        boolean b = false;
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setString(1, selected_map);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                map = rs.getString("free_maps");
            }
            b = map.equals(selected_map);
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }
}
