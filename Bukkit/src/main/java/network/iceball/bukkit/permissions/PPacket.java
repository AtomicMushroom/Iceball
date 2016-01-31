package network.iceball.bukkit.permissions;

import network.iceball.bukkit.api.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Floris on 02-11-15.
 * Email: florisgra@gmail.com
 * <p>
 * Magic. Do not touch.
 */

/**
 * This class is class is used as object to pass around the data of a specific player, so we don't have to
 * do multiple queries.
 */
public class PPacket {

    final HashMap<String, Boolean> permissions;
    final String rank;
    final String player;

    public PPacket(HashMap<String, Boolean> permissions, String rank, String player) {
        this.permissions = permissions;
        this.rank = rank;
        this.player = player;
    }


    public HashMap<String, Boolean> getPermissions() {
        return permissions;
    }

    public String getRank() {
        return rank;
    }

    public String getPlayer(){
        return player;
    }

    public static PPacket notNull(String player) {
        String rank = Permissions.getInstance().getGroupManager().getDefault();
        return new PPacket(new HashMap<>(), rank, player);
    }

    public static PPacket getPacket(String player) {
        String CMD = "SELECT * FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        PPacket pPacket = null;
        String rank = null;
        HashMap<String, Boolean> perms = null;

        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rank = rs.getString("rank");
                perms = Util.stringToHashMap(rs.getString("old/iceball/network/permissions"));
                pPacket = new PPacket(perms, rank, player);
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (pPacket == null){
            pPacket = notNull(player);
        }
        return pPacket;
    }
}
