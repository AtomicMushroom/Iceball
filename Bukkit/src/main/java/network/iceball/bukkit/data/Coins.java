package network.iceball.bukkit.data;

import network.iceball.bukkit.api.Database;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Floris on 06-07-15.
 */
public class Coins {

    public static String coinsPrefix = ChatColor.DARK_AQUA + "[" + ChatColor.GOLD + "Coins" + ChatColor.DARK_AQUA + "] " + ChatColor.GREEN;
    public static String coinsSuffix = ChatColor.GREEN + "";

    /**
     * Gets the coins through the database
     *
     * @param uuid player UUID
     * @return amount of coins in integer
     */
    public static Integer getCoins(UUID uuid) {
        String CMD = "SELECT * FROM playerdata WHERE uuid = ?;";
        PreparedStatement ps = null;
        int coins = -1;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                coins = rs.getInt("coins");
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }

    /**
     * Gets the coins through the database
     *
     * @param name The name of the player The name of the player
     * @return amount of coins in integer
     */
    public static Integer getCoins(String name) {
        String CMD = "SELECT * FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        int coins = -1;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coins = rs.getInt("coins");
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coins;
    }

    /**
     * Set a new value of coins for a given UUID
     *
     * @param uuid
     * @param coins amount to be set
     */
    public static void setCoins(UUID uuid, int coins) {
        String CMD = "UPDATE playerdata SET coins=? WHERE uuid= ?;";
        PreparedStatement ps = null;
        int Zcoins = -1;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set a new value of coins for a given Playername
     *
     * @param name  The name of the player
     * @param coins coins value to be set
     */
    public static void setCoins(String name, int coins) {
        String CMD = "UPDATE playerdata SET coins = ? WHERE player = ?;";
        PreparedStatement ps = null;

        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setInt(1, coins);
            ps.setString(2, name);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds coins! Prefer this method rather than Get + set, because that are more queries, this method is just 1 query!
     *
     * @param name  The name of the player
     * @param coins amount of coins to be added
     */
    public static void addCoins(String name, int coins) {
        String CMD = "UPDATE playerdata SET coins = coins + ? WHERE player = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setInt(1, coins);
            ps.setString(2, name);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds coins! Prefer this method rather than Get + set, because that are more queries, this method is just 1 query!
     *
     * @param uuid  player UUID
     * @param coins amount of coins to be added
     */
    public static void addCoins(UUID uuid, int coins) {
        String CMD = "UPDATE playerdata SET coins = coins + ? WHERE uuid = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setInt(1, coins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
