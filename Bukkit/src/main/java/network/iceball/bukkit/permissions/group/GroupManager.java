package network.iceball.bukkit.permissions.group;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sun.istack.internal.NotNull;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.PPacket;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.Util;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Floris on 26-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class GroupManager {

    private HashMap<String, GroupPerms> groups; //rank:group
    private HashMap<String, String> players; //player:rank
    private Iceball plugin;
    private boolean isStart = true;

    public boolean isReady() {
        return ready;
    }

    private boolean ready = false;

    public GroupManager(Iceball plugin, Permissions permissions) {
        this.plugin = plugin;
        reload();
        synchronized (this){
            ready = true;
            notify();
        }
    }

    /**
     * Gets all the players with the given rank. The Set is not null, however the if someone leaves while this method is
     * used, the player will be null. So, always check if the player is null before doing something else.
     * @param rank 4 letter keyword used in the permissions.
     * @return Set with the name all players of the group.
     */
    public @NotNull Set<String> getPlayers(String rank){
        Set<String> set = new TreeSet<>();

        Multimap<String, String> multiMap = HashMultimap.create();
        for (Map.Entry<String, String> entry : players.entrySet()) {
            multiMap.put(entry.getValue(), entry.getKey());
        }
        // basically sorting the players based of ranks. <String (rank), Collection<String> (player)
        multiMap.asMap().entrySet().stream().filter(entry -> entry.getKey().equals(rank)).forEach(entry -> {
            set.addAll(entry.getValue().stream().collect(Collectors.toList()));
        });
        return set;
    }


    public void reload() {
        try {
            GData.setupGroupDataTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadGroups();
        //plugin.getServer().getLogger().info("Setting up inheritance");
        setupInheritance();
        players = new HashMap<>();
    }

    private void loadGroups() {
        groups = new HashMap<>();
        ArrayList<String> groupsNames = GData.getGroupNames();
        for (String name : groupsNames){
            GroupPerms group = GData.getGroup(name, plugin);
            groups.put(group.getRank(), group);
            //plugin.getServer().getLogger().info("Permissions: " + group.getSuperPermissions() + "@" + group.getName());
        }
        if (!groups.containsKey("NRML")){
            GData.createGroup("Normal", "NRML", "&7", "&8", 1);
            loadGroups();
        }
    }

    private void setupInheritance(){
        GroupPerms[] groupLadder = groups.values().toArray(new GroupPerms[groups.values().size()]); //convert to array
        Arrays.sort(groupLadder, GroupPerms.onLadder); //sort to ladder to add permissions
        /*for (GroupPerms group : groupLadder){
            plugin.getServer().getLogger().info(group.getName() + ":" + group.getLadder());
        }*/

        for (int i=0; i < groupLadder.length; i++){ //Ladder Inheritance
            GroupPerms now = groupLadder[i];
            GroupPerms previous;
            if (i != 0) {
                previous = groupLadder[i-1];
                if (previous.getLadder() != 0 && now.getLadder() != 0)
                //plugin.getServer().getLogger().info("Adding: " + previous.getName() + " to " + now.getName());

                now.addOfflinePermissions(previous.getPermissions());
            }
        }

        for (GroupPerms group : groupLadder){ //Custom Inheritance
            if (group.getInheritance() != null && group.getInheritance().length != 0){
                for (String rank : group.getInheritance()){
                    if (rank.contains("-")){
                        rank = rank.substring(1, rank.length());
                        HashMap<String, Boolean> perms = groups.containsKey(rank) ?
                                Util.stringToHashMap(groups.get(rank).getSuperPermissions()) : new HashMap<>();

                        for (Map.Entry<String, Boolean> entry : perms.entrySet()){
                            perms.put(entry.getKey(), false); //remove the permissions
                        }

                        //plugin.getServer().getLogger().info(Util.hashMapToString(perms) + " from " + rank + " removed from " + group.getName()); //remove group perms
                        group.addOfflinePermissions(perms);
                    } else {
                        HashMap<String, Boolean> perms = groups.containsKey(rank) ?
                                groups.get(rank).getPermissions() : new HashMap<>();
                        group.addOfflinePermissions(perms);            //add group perms
                        //plugin.getServer().getLogger().info(Util.hashMapToString(perms) + " from " + rank + " added to " + group.getName());
                    }
                }
            }
        }

        groups = new HashMap<>();
        for (GroupPerms group : groupLadder){ //convert back for class
            groups.put(group.getRank(), group);
        }
    }

    public GroupPerms getGroupByPlayer(String name) {
        String rank = players.get(name);
        if (rank != null && groups.get(rank) != null) {
            return groups.get(rank);
        }
        return null;
    }

    public GroupPerms getGroup(String rank){
        return groups.get(rank);
    }
    public GroupPerms getGroupByName(String name){
        for (GroupPerms group : groups.values()){
            if (group.getName().equals(name)){
                return group;
            }
        }
        return null;
    }

    public void removeGroup(String name){
        if (getGroupByName(name)!= null){
            String rank = getGroupByName(name).getRank();
            groups.remove(rank);
        }
    }

    public void take(PPacket packet){
        players.put(packet.getPlayer(), packet.getRank());
        if (getGroupByPlayer(packet.getPlayer()) != null) {
            HashMap<String,Boolean> perms = getGroupByPlayer(packet.getPlayer()).getPermissions();
            Permissions.getInstance().getPlayerManager().getPlayer(packet.getPlayer()).addOfflinePermissions(perms);
        }
        // take the player data and use it.
    }

    public void remove(String name){
        players.remove(name);
    }

    public String getDefault() {
        if (groups.containsKey("NRML")) {
            return "NRML";
        } else if (!groups.isEmpty()) {
            return groups.keySet().iterator().next();
        }
        Preconditions.checkArgument(false, "Couldn't find any groups!");
        return null;
    }
}
