package network.iceball.bukkit.permissions.group;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.api.Database;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.Util;

import java.nio.channels.AlreadyBoundException;
import java.sql.*;
import java.util.*;

/**
 * Created by Floris on 02-11-15.
 * Email: florisgra@gmail.com
 * <p>
 * Magic. Do not touch.
 */
public class GData {

    public static @Nullable GroupPerms getGroup(String name, Iceball plugin){
        String CMD = "SELECT * FROM groupdata WHERE groupname = ?;";
        PreparedStatement ps;
        GroupPerms group = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String prefix = rs.getString("prefix");
                String suffix = rs.getString("suffix");
                String rank = rs.getString("rank");
                HashMap<String, Boolean> permissions = Util.stringToHashMap(rs.getString("old/permissions"));
                int ladder = rs.getInt("ladder");
                String inher = rs.getString("inheritance");
                String[] inheritance = inher != null ? inher.split(",") : null;
                group = new GroupPerms(plugin, name, permissions, prefix, suffix, rank, inheritance, ladder);
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    public static HashMap<String, Boolean> getGroupPermissions(String name){
        String CMD = "SELECT permissions FROM groupdata WHERE groupname = ?;";
        PreparedStatement ps;
        GroupPerms group = null;
        HashMap<String, Boolean> permissions = new HashMap<>();
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                permissions = Util.stringToHashMap(rs.getString("old/permissions"));
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e) {

            //e.printStackTrace();
        }
        return permissions;
    }

    public static @NotNull ArrayList<String> getGroupNames(){
        String CMD = "SELECT groupname FROM groupdata;";
        Statement st = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            Connection connection = Database.getInstance().getConnection();
            st = connection.prepareStatement(CMD);
            ResultSet rs = st.executeQuery(CMD);

            while (rs.next()) {
                String group = rs.getString("groupname");
                if (group != null){
                    names.add(group);
                }
            }
            rs.close();
            st.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static ArrayList<Integer> getAllLadders(){
        String CMD = "SELECT ladder FROM groupdata;";
        PreparedStatement ps = null;
        ArrayList<Integer> ladders = new ArrayList<Integer>();
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ladders.add(rs.getInt("ladder"));
            }

            for (Iterator<Integer> it=ladders.iterator(); it.hasNext();) {
                if (it.next().equals(0))
                    it.remove(); // NOTE: Iterator's remove method, not ArrayList's, is used.
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return ladders;
    }

    public static void createGroup(final String groupName, String rank, String prefix, String suffix, int ladder){
        String CMD = "INSERT INTO groupdata (id, groupname, rank, prefix, suffix, permissions, inheritance, ladder) " +
                                    "VALUES (NULL,       ?,    ?,      ?,      ?,        '',        NULL,      ?);";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, groupName);
            ps.setString(2, rank);
            ps.setString(3, prefix);
            ps.setString(4, suffix);
            ps.setInt(5, ladder);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void setGroupPrefix(String prefix, String groupname){
        String CMD = "UPDATE groupdata SET prefix = ?  WHERE groupname = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, prefix);
            ps.setString(2, groupname);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteGroup(String groupname){
        String CMD = "DELETE FROM groupdata WHERE groupname = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, groupname);
            ps.executeUpdate();
            ps.close();
            connection.close();
            Permissions.getInstance().getGroupManager().removeGroup(groupname);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setGroupSuffix(String suffix, String groupname){
        String CMD = "UPDATE groupdata SET suffix = ?  WHERE groupname = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, suffix);
            ps.setString(2, groupname);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setGroupPermissions(String permissions, String groupname){
        String CMD = "UPDATE groupdata SET permissions = ?  WHERE groupname = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, permissions);
            ps.setString(2, groupname);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void addGroupPermissions(String permissions, boolean value, String groupname){
        HashMap<String, Boolean> perms = getGroupPermissions(groupname);
        perms.put(permissions, value);
        permissions = Util.hashMapToString(perms);
        String CMD = "UPDATE groupdata SET permissions = ? WHERE groupname = ?;";
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, permissions);
            ps.setString(2, groupname);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setGroupLadder(int ladder, String groupname){
        String CMD = "UPDATE groupdata SET ladder = ? WHERE groupname = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setInt(1, ladder);
            ps.setString(2, groupname);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setGroupRank(String newRank, String oldRank, String groupname){
        String CMD = "UPDATE groupdata SET rank = ? WHERE groupname = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, newRank);
            ps.setString(2, groupname);
            ps.executeUpdate();
            CMD = "UPDATE playerdata SET rank = ? WHERE rank = ?;";
            ps = connection.prepareStatement(CMD);
            ps.setString(1, newRank);
            ps.setString(2, oldRank);
            ps.executeUpdate();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void setGroupName(String newName, String oldName){
        String CMD = "UPDATE groupdata SET groupname = ? WHERE groupname = ?;";
        PreparedStatement ps = null;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, newName);
            ps.setString(2, oldName);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void setInheritance(String[] ranks, String group){
        String CMD = "UPDATE groupdata SET inheritance = ? WHERE groupname = ?;";
        PreparedStatement ps = null;

        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            if (ranks != null) {
                ps.setString(1, Util.arrayToString(ranks));
            } else {
                ps.setString(1, null);
            }
            ps.setString(2, group);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void setDefaultGroup(String group) throws AlreadyBoundException {
        if (Permissions.getInstance().getGroupManager().getGroupByName(group).getRank().equals("NRML")){
            throw new AlreadyBoundException();
        }
        String CMD = "SELECT groupname FROM groupdata WHERE rank='NRML';";
        Statement st;
        ResultSet resultSet;
        try {
            Connection connection = Database.getInstance().getConnection();
            st = connection.createStatement();
            resultSet = st.executeQuery(CMD);

            ArrayList<String> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getString("groupname"));
            }
            st.close();
            connection.close();
            list.stream().filter(s -> s != null).forEach(groupname -> {
                String oldrank = Permissions.getInstance().getGroupManager().getGroupByName(groupname).getRank();
                setGroupRank(randomRank(), oldrank, groupname);
            });
            String oldrank = Permissions.getInstance().getGroupManager().getGroupByName(group).getRank();
            setGroupRank("NRML", oldrank, group);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static Set<String> getGroupInfo(String groupName){
        String CMD = "SELECT * FROM groupdata WHERE groupname = ?";
        Set<String> groupinfo = new LinkedHashSet<>();
        PreparedStatement ps;
        try {
            Connection connection = Database.getInstance().getConnection();
            ps = connection.prepareStatement(CMD);
            ps.setString(1, groupName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String Z = Iceball.pluginSuffixMARK;
                String S = Iceball.pluginSuffixNRML;
                groupinfo.add(S + "Group: " + Z + groupName);
                groupinfo.add(S + "Rank: " + Z + rs.getString("rank"));
                String prefix = rs.getString("prefix") != null ? rs.getString("prefix") : "-";
                groupinfo.add(S + "Prefix: " + Z + prefix);
                String suffix = rs.getString("suffix") != null ? rs.getString("suffix") : "-";
                groupinfo.add(S + "Suffix: " + Z + suffix);
                groupinfo.add(S + "Ladder: " + Z + String.valueOf(rs.getInt("ladder")));
                String inheritance = rs.getString("inheritance") != null ? rs.getString("inheritance") : "-";
                groupinfo.add(S + "Inheritance: " + Z + inheritance);
                groupinfo.add(S + "Permissions: ");
                String permissions = rs.getString("old/iceball/network/permissions");
                groupinfo = Util.parsePermssions(groupinfo, permissions);
            }
            rs.close();
            ps.close();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return groupinfo;
    }

    private static String randomRank(){
        String s = "";
        int letterIndex;
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < 4; i++){
            letterIndex = (int) (Math.random() * 26);
            builder.append(letterIndex > 0 && letterIndex < 27 ? String.valueOf((char)(letterIndex + 64)) : null);
            System.out.println(builder.toString());
        }
        String rank = builder.toString();
        if (Permissions.getInstance().getGroupManager().getGroup(rank) != null){ //if rank exist, create new one
            return randomRank();
        }
        return rank;
    }

    public static void setupGroupDataTable() throws SQLException {
        Statement st;
        String cmd = "CREATE TABLE IF NOT EXISTS groupdata"
                + "  (id INT NOT NULL AUTO_INCREMENT,"
                + "   groupname VARCHAR(16),"
                + "   rank TINYTEXT,"
                + "   prefix TINYTEXT,"
                + "   suffix TINYTEXT,"
                + "   permissions TEXT CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + "   inheritance TEXT CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,"
                + "   ladder INT UNSIGNED,"
                + "   PRIMARY KEY(id),"
                + "   UNIQUE KEY(groupname));";
        Connection connection = Database.getInstance().getConnection();
        st = connection.createStatement();
        st.execute(cmd);
        st.close();
        connection.close();
    }
}
