package network.iceball.bukkit.permissions.group;

import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.Privilege;
import network.iceball.bukkit.permissions.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Created by Floris on 26-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class GroupPerms implements Privilege {

    static Comparator<GroupPerms> onLadder = (o1, o2) -> Integer.compare(o1.ladder,o2.getLadder());

    private JavaPlugin plugin;
    private HashMap<String, Boolean> permissionsMap = null;
    private String superPermissions;
    private String name;
    private String prefix;
    private String suffix;
    private String rank;
    private String[] inheritance;
    private int ladder;

    public GroupPerms(JavaPlugin plugin, String name, HashMap<String, Boolean> permissionsMap,
                      String prefix, String suffix, String rank, String[] inheritance, int ladder) {
        this.name = name;
        this.plugin = plugin;
        this.permissionsMap = permissionsMap == null ? new HashMap<>() : permissionsMap;
        this.superPermissions = Util.hashMapToString(permissionsMap);
        this.prefix = prefix;
        this.suffix = suffix;
        this.rank = rank;
        this.inheritance = inheritance;
        this.ladder = ladder;
    }

    @Override
    public void setPermissions(HashMap<String, Boolean> permissions) {
        if (permissions != null)
        GData.setGroupPermissions(Util.hashMapToString(permissions), name); //database

        HashMap<String, Boolean> perms = permissionsMap;
        this.permissionsMap = permissions; // plugin

        Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
        for (String player : set){
            for (String zPermissions : perms.keySet()) // players
            Permissions.getInstance().getPlayerManager().getPlayer(player).addOfflinePermissions(zPermissions, false);
        }
        update();
    }

    @Override
    public void addPermissions(String permissions, boolean value) {
        this.permissionsMap.put(permissions, value); // plugin
        GData.addGroupPermissions(permissions, value, name);

        Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
        for (String player : set){
            Permissions.getInstance().getPlayerManager().getPlayer(player).addOfflinePermissions(permissions, value);
        }
    }

    @Override
    public void deletePermissions(String permissions) {
        this.permissionsMap.remove(permissions);
        HashMap<String, Boolean> perms = Util.stringToHashMap(this.superPermissions);
        perms.remove(permissions);
        superPermissions = Util.hashMapToString(perms);
        GData.setGroupPermissions(superPermissions, name);
        Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
        for (String player : set){
            Permissions.getInstance().getPlayerManager().getPlayer(player).addOfflinePermissions(permissions, false);
        }
    }

    public void addOfflinePermissions(HashMap<String, Boolean> permissionsMap){
        if (permissionsMap == null){return;}
        Iterator it = permissionsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            addOfflinePermissions((String)pair.getKey(), (Boolean) pair.getValue());
        }
    }

    public void addOfflinePermissions(String permissions, boolean value){
        if (permissions != null) {
            permissionsMap.put(permissions, value);
            update();
        }
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return permissionsMap;
    }

    @Override
    public void update() {
        if (Permissions.getInstance().getPlayerManager() != null) {
            Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
            for (String player : set) {
                Set<Map.Entry<String, Boolean>> smap = permissionsMap.entrySet();
                for (Map.Entry<String, Boolean> me : smap) {
                    Permissions.getInstance().getPlayerManager().getPlayer(player).addOfflinePermissions(me.getKey(), me.getValue());
                }
            }
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        GData.setGroupSuffix(suffix, name);
        Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
        for (String player : set){
            Permissions.getInstance().getPlayerManager().getPlayer(player).setSuffix(suffix);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        GData.setGroupPrefix(prefix, name);

        Set<String> set = Permissions.getInstance().getGroupManager().getPlayers(rank);
        for (String player : set){
            Permissions.getInstance().getPlayerManager().getPlayer(player).setPrefix(prefix);
        }
    }


    public int getLadder() {
        return ladder;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        GData.setGroupRank(rank, this.rank, name);
        this.rank = rank;
    }

    public String[] getInheritance() {
        return inheritance;
    }

    public void setInheritance(String[] ranks) {
        if (ranks == null){
            this.inheritance = new String[0];
            GData.setInheritance(null, name);
            return;
        }
        this.inheritance = inheritance;
        GData.setInheritance(ranks, name);
        Permissions.getInstance().reload();
    }

    public String getName() {
        return name;
    }

    public String getSuperPermissions(){
        return superPermissions;
    }
}
