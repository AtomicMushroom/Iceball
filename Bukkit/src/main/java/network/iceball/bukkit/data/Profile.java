package network.iceball.bukkit.data;

import network.iceball.bukkit.api.Database;
import network.iceball.bukkit.util.IP;
import network.iceball.bukkit.util.JavaHttpUrlConnectionReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.ocpsoft.prettytime.PrettyTime;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Floris on 01-07-15.
 */
public class Profile {

    public final String name;

    public Profile(String name){
        this.name = name;
    }

    /**
     * Get's the Offline UUID through the registered database in the config.yml file. Returns "null", when it is not found!
     *
     * @return String UUID
     */
    public UUID getUUID() {
        String CMD = "SELECT * FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        UUID uuid = null; //UUID.fromString("null");
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                uuid = UUID.fromString(rs.getString("uuid"));
                rs.close();
                ps.close();
                return uuid;
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    /** Gets the profile about the player.
     *
     * @return List<String> data about the player.
     */
    public String[] getProfile() {
        String CMD = "SELECT * FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        List<String> pData = new ArrayList<String>();
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String G = ChatColor.GREEN + "";
                String Y = ChatColor.GRAY + "";

                String ip = IP.longToIp(rs.getLong("ip"));
                pData.add(String.valueOf(Y + "IP address: " + G + ip));
                JavaHttpUrlConnectionReader r = new JavaHttpUrlConnectionReader(ip);
                String country = r.results.isNull("country") ? null : r.results.getString("country");
                String countryCode = r.results.isNull("countryCode") ? null : r.results.getString("countryCode");

                if ((country != null && !country.isEmpty()) && (countryCode != null && !countryCode.isEmpty())) {
                    pData.add(String.valueOf(Y + "Location: " + G + countryCode + ", The " + country));
                }
                String timezone = r.results.isNull("timezone") ? null : r.results.getString("timezone");
                if (timezone != null && !timezone.isEmpty()) {
                    pData.add(String.valueOf(Y + "Timezone: " + G + timezone));
                }
                pData.add(String.valueOf(Y + "Rank: ") + G + rs.getString("rank"));
                pData.add(String.valueOf(Y + "UUID: " + G + rs.getString("uuid")));
                pData.add(String.valueOf(Y + "Coins: " + G + rs.getInt("coins")));
                pData.add(String.valueOf(Y + "Networkbooster: " + G + rs.getInt("networkbooster")));
                pData.add(String.valueOf(Y + "Level " + G + rs.getInt("level")));
                pData.add(String.valueOf(Y + "Karma: ") + G + rs.getInt("karma"));

                Timestamp joinedTS = rs.getTimestamp("joined_on");
                PrettyTime pt = new PrettyTime(Locale.ENGLISH);
                pData.add(String.valueOf(Y + "Joined " + G + pt.format(new Date(joinedTS.getTime())) + Y + "."));

                Timestamp lastseen = rs.getTimestamp("last_seen");
               //TODO: VERY HIGH PRIORTY if (lastseen.ax), fix the error when this is 0.000: 0:0:0:0, and the if statement
                Player player = Bukkit.getPlayerExact(name);
                if (player == null) {
                    pData.add(String.valueOf(Y + "Last seen " + G + pt.format(new Date(lastseen.getTime())) + Y + "."));
                    pData.add(String.valueOf(G + name + Y + " was not found on this server!"));
                } else {
                    pData.add(String.valueOf(G + name + Y + " is online on this server!"));
                }
            } else {
                return null;
            }
            rs.close();
            ps.close();
            connection.close();
            return pData.stream().toArray(String[]::new);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddress() {
        String CMD = "SELECT * FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        String ip = "null";
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ip = IP.longToIp(rs.getLong("ip"));
                return  ip;
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * Gets the player's name based of the UUID registered in the database.
     *
     * @param uuid UUID of player
     * @return String Player's name
     */
    public static String getName(UUID uuid) {
        String CMD = "SELECT * FROM playerdata WHERE uuid = ?;";
        PreparedStatement ps = null;
        String name = "null";
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                name = rs.getString("player");
                rs.close();
                ps.close();
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return name;
    }
}


