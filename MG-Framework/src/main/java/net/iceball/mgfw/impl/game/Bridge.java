package net.iceball.mgfw.impl.game;

import com.google.common.base.Preconditions;
import net.iceball.mgfw.impl.MinigameFramework;
import network.iceball.bukkit.api.Database;
import network.iceball.bukkit.permissions.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Floris on 29-11-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class Bridge {
    private static String servername;

    public static void load(String servername){
        Bridge.servername = servername;
        MinigameFramework.createTable();
    }

    public static void setupGame(String game, String[] maps, boolean multimode) throws NullPointerException {
        if(serverExist()){
            updateGame(game, maps, multimode);
        } else {
            createGame(game, maps, multimode);
        }
    }

    public static void updateGame(String game, String[] maps, boolean multimode){
        String CMD = "UPDATE gamedata SET game = ? , free_maps = ? , used_maps = '', last_update=NOW() WHERE servername = ?;";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);
            ps.setString(1, game);

            String mapsString = Util.arrayToString(maps);
            if (!multimode){
                mapsString = mapsString.replace(",", ":");
            }
            ps.setString(2, mapsString);

            //String servername = Config.getConfig().getString("servername");
            ps.setString(3, servername);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createGame(String game, String[] maps, boolean multimode){
        String CMD = "INSERT INTO gamedata (servername, game, free_maps, used_maps, last_update) " +
                "VALUES (?,?,?,'',NOW())";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);

            //String servername = Config.getConfig().getString("servername");
            Preconditions.checkArgument(servername != null, "Server name cannot be null! Set the servername in" +
                    " the arenas.yml!");
            ps.setString(1, servername);
            ps.setString(2, game);

            String mapsString = Util.arrayToString(maps);
            if (!multimode){
                mapsString = mapsString.replace(",", ":");
            }
            ps.setString(3, mapsString);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static boolean serverExist(){
        String CMD = "SELECT servername FROM gamedata WHERE servername = ?";
        Connection connection = null;
        try {
            connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);
            //String servername = Config.getConfig().getString("servername");
            if (servername == null){ return false; }
            ps.setString(1, servername);

            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String server = rs.getString("servername");
                if (server != null){
                    return true;
                }
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getChosenMap(String player){
        String cmd = "SELECT chosen_map FROM playerdata WHERE player = ?;";
        String map = "";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(cmd);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                map = rs.getString("chosen_map");
            }
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
}
