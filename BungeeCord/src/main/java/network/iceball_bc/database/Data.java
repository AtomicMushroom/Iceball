package network.iceball_bc.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import network.iceball_bc.Iceball;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Floris on 16-10-15.
 */
public class Data {

    private Iceball plugin;

    private Data(){}

    public static void storeUUID(ProxiedPlayer player) {
        boolean a = false;
        String CMD = "SELECT * FROM playerdata WHERE uuid = ?";

        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);
            String name = player.getName();
            UUID uuid = player.getUniqueId();

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) { //UUID is empty, so register it.
                //  plugin.getLogger().info("REGISTERING! Name is " + name + ", UUID is " + U.getUUID());
                ps.close();
                rs.close();
                connection.close();
                registerUUID(name, uuid);
            } else {
                //UUID is not empty, but is the name the same?
                //    plugin.getLogger().info("UPDATING! Name is " + name + ", UUID is " + U.getUUID());
                ps.close();
                rs.close();
                connection.close();
                updateUUID(name, uuid); //Update, no matter what.
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeIP(String name, String ip) {
        String CMD = "UPDATE playerdata SET ip = ? WHERE player = ?;";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);
            ps.setLong(1, IP.ipToLong(ip));
            ps.setString(2, name);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void storeLastSeen(String name) {
        String CMD = "UPDATE playerdata SET last_seen = NOW() WHERE player = ?;";
        try {
            Connection connection = Database.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateUUID(String name, UUID uuid) throws SQLException {
        String CMD = "UPDATE playerdata SET player=? WHERE uuid=?;";
        Connection connection = Database.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(CMD);
        ps.setString(1, name);
        ps.setString(2, uuid.toString());
        ps.executeUpdate();

        ps.close();
        connection.close();
    }

    private static void registerUUID(String name, UUID uuid) throws SQLException {
        String CMD = "INSERT INTO playerdata (uuid, player) VALUES(?, ?)" +
                " ON DUPLICATE KEY UPDATE uuid=VALUES(uuid), player=VALUES(player)," +
                " ip=NULL, coins=0, level=1, networkbooster=1," +
                " karma=0, joined_on=NOW(), last_seen=NOW(), rank='NRML';";
        Connection connection = Database.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement(CMD);
        ps.setString(1, uuid.toString());
        ps.setString(2, name);
        ps.executeUpdate();
        ps.close();
        connection.close();

        String CMD2 = "UPDATE playerdata SET permissions = ?, chosen_map = ? WHERE uuid = ?";
        connection = Database.getInstance().getConnection();
        ps = connection.prepareStatement(CMD2);
        ps.setString(1, " ");
        ps.setString(2, " ");
        ps.setString(3, uuid.toString());
        ps.executeUpdate();
        ps.close();
        connection.close();

    }
}
