package network.iceball.bukkit.permissions.player;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import network.iceball.bukkit.permissions.PPacket;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.api.Database;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.Util;
import network.iceball.bukkit.permissions.group.GroupPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Floris on 27-10-15.
 */
public class PData {

    private static String P = Iceball.pluginPrefix;
    private static String S = Iceball.pluginSuffixNRML;
    private static String Z = Iceball.pluginSuffixMARK;

    public static @NotNull HashMap<String, Boolean> getPermissions(String player) {
        String CMD = "SELECT permissions FROM playerdata WHERE player = ?;";
        PreparedStatement ps;
        HashMap<String, Boolean> perms = new HashMap<>();
        String permissions = "null";
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                permissions = rs.getString("old/permissions");
            }
            if (!permissions.equalsIgnoreCase("null")) {
                perms = Util.stringToHashMap(permissions); //
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perms;
    }

    public static void setPermissions(@Nullable String permissions, String player) {
        String CMD = "UPDATE playerdata SET permissions = ?  WHERE player = ?;";
        PreparedStatement ps = null;
        if (permissions == null){
            permissions = "";
        }
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, permissions);
            ps.setString(2, player);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void addPermissions(String permissions, boolean value, String player) {
        HashMap<String, Boolean> perms = getPermissions(player);
        perms.put(permissions, value);
        permissions = Util.hashMapToString(perms);
        String CMD = "UPDATE playerdata SET permissions = ? WHERE player = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, permissions);
            ps.setString(2, player);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deletePermissions(String permissions, String player) {
        HashMap<String, Boolean> perms = getPermissions(player);
        perms.remove(permissions);
        permissions = Util.hashMapToString(perms);

        String CMD = "UPDATE playerdata SET permissions = ? WHERE player = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, permissions);
            ps.setString(2, player);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void setPlayerRank(String rank, String name) {
        String CMD = "UPDATE playerdata SET rank = ? WHERE player = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, rank);
            ps.setString(2, name);
            ps.executeUpdate();
            ps.close();
            connection.close();
            addPermissions("donate.rankpromote", true, name);

            Player player = Bukkit.getPlayer(name);
            if (player != null){
                Permissions.getInstance().removePlayer(name);
                Permissions.getInstance().add(PPacket.getPacket(name));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static String getRank(String player) {
        String CMD = "SELECT rank  FROM playerdata WHERE player = ?;";
        PreparedStatement ps = null;
        String rank = "null";
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, player);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rank = rs.getString("rank");
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rank;
    }

    public static Set<String> getPlayerPermissionsInfo(String player){
        PPacket packet = PPacket.getPacket(player);
        Set<String> list = new LinkedHashSet<>();
        if (packet == null){
            list.add(P + ChatColor.RED + "We couldn't find " + player + " in our database.");
            return list;
        }
        GroupPerms group = Permissions.getInstance().getGroupManager().getGroup(packet.getRank());
        list.add(P + "Information about " + Z + player + S + ".");
        list.add(S + Util.formatName(player) + " permissions: ");
        list = Util.parsePermssions(list, Util.hashMapToString(packet.getPermissions()));
        list.add(S + "Rank: " + Z + packet.getRank());
        if (group != null){
            list.add(S + "Group: " + Z + group.getName());
            list.add(S + "Inheritance: " + Z + Util.arrayToString(group.getInheritance()));
            list.add(S + "Group permissions: ");
            list = Util.parsePermssions(list, group.getSuperPermissions());
        } else {
            list.add(S + "Group: " + ChatColor.RED + "Doesn't exist. ");
        }
        return list;
    }


}
