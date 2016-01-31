package network.iceball.bukkit.commands;

import network.iceball.bukkit.data.Profile;
import network.iceball.bukkit.exceptions.TooBigNumberException;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.group.GData;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.Util;
import network.iceball.bukkit.permissions.group.GroupPerms;
import network.iceball.bukkit.permissions.player.PData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Floris on 27-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
class PCommand implements CommandExecutor {

    private Iceball plugin;

    public PCommand(Iceball plugin){
        this.plugin = plugin;
    }
    /*   Permissions Command Reference sorted by args.length
       1 p reload
       1 p help

       1 p groups
       2 p help <number>
       2 p user <user>
       2 p group <group>
       2 p rank <rank>

       3 p users defaultgroup <group>
       3 p group <group> delete

       4 p user <user> setgroup <group>
       4 p group <group> rank <rank>
       4 p group <group> rename <name>
       4 p group <group> setprefix <prefix>
       4 p group <group> setsuffix <suffix>
       4 p group <group> setladder <ladder>
       4 p group <group> inheritance reset

       5 p user <user> perms set  <permissions> //
       5 p user <user> perms add <permissions> (add "-" without the quotes or spaces in front of your permissions to add as negative)
       5 p user <user> perms remove <permissions>

       5 p group <group> perms set  <permissions>
       5 p group <group> perms add <permissions>
       5 p group <group> perms remove <permissions>
       5 p group <group> inheritance set <group>

       7 p group create <name> <rank> <prefix> <suffix> [ladder]

     */
    private String P = Iceball.pluginPrefix;
    private String S = Iceball.pluginSuffixNRML;
    private String Z = Iceball.pluginSuffixMARK;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("p")) {// If the player typed /p then do the following...

            switch (args.length){
                case 0:
                    return false;
                case 1:
                    switch (args[0].toLowerCase()){
                        case "reload":
                            Permissions.getInstance().reload();
                            sender.sendMessage(P + "Permissions reloaded!");
                           return true;
                        case "help":
                            helpReference(sender, 1);
                            return true;
                        case "user":
                            sender.sendMessage(S + "/p user <user> ");
                            return true;
                        case "group":
                            sender.sendMessage(S + "/p group <group> ");
                            return true;
                        case "groups":
                            ArrayList<String> list = GData.getGroupNames();
                            if (list.isEmpty()){
                                sender.sendMessage(P + ChatColor.RED + "No groups where found!");
                                return true;
                            }
                            sender.sendMessage(P + "Groups: (preview - rank - name)");
                            for (String s2 : list){
                                String rank = Permissions.getInstance().getGroupManager().getGroupByName(s2).getRank();
                                String prefix = Permissions.getInstance().getGroupManager().getGroupByName(s2).getPrefix();
                                String colorpreview = ChatColor.stripColor((
                                        prefix != null ? prefix : ""));

                                if(s2.length() <= colorpreview.length()){
                                        colorpreview = ChatColor.translateAlternateColorCodes('&',
                                                Permissions.getInstance().getGroupManager().getGroupByName(s2).getPrefix());

                                        sender.sendMessage(S + colorpreview + S + " | " + Z + rank + S + " (" +  Z + s2 + S + ")");
                                } else {
                                sender.sendMessage(Z + s2 + S + " | " + Z + rank);
                                }
                            }
                            return true;
                        default:
                            return false;
                    }
                case 2:
                    switch (args[0].toLowerCase()){
                        case "help":
                            int number;
                            try{
                                number = Integer.valueOf(args[1]);
                            } catch (NumberFormatException e){
                                sender.sendMessage(P + ChatColor.RED + "Expected a number!");
                                return true;
                            }
                            helpReference(sender, number);
                            return true;
                        case "user":
                            String player = args[1];
                            Set<String> list = PData.getPlayerPermissionsInfo(player);
                            if (list == null){
                                sender.sendMessage(P + ChatColor.RED + "Oops, something went wrong.");
                                return true;
                            }
                            list.forEach(sender::sendMessage);
                            return true;
                        case "group":
                            Set<String> set = GData.getGroupInfo(args[1]);
                            if (set.isEmpty()){
                                sender.sendMessage(P + ChatColor.RED + "Group " + args[1] + " not found!");
                                return true;
                            }
                            sender.sendMessage(P + "All information about " + Z + args[1] + S + ".");
                            set.forEach(sender::sendMessage);
                            return true;
                        case "rank":
                            GroupPerms groupPerms = Permissions.getInstance().getGroupManager().getGroup(args[1]);
                            String group;
                            if (groupPerms == null){
                                sender.sendMessage(P + ChatColor.RED + "A group with rank " + args[1] + " was not found!");
                                return true;
                            }
                            group = groupPerms.getName();
                            set = GData.getGroupInfo(group);
                            sender.sendMessage(P + "All information about " + Z + group + S + ".");
                            set.forEach(sender::sendMessage);
                            return true;
                        default:
                            return false;
                    }
                case 3:
                    switch (args[0].toLowerCase()){
                        case "users":
                            switch (args[1].toLowerCase()){
                                case "defaultgroup":
                                        String group = args[2];
                                        if (Permissions.getInstance().getGroupManager().getGroupByName(group) != null){
                                            try{
                                                GData.setDefaultGroup(group);
                                            } catch (AlreadyBoundException ebe){
                                                sender.sendMessage(P + ChatColor.RED + "This group is already the default!");
                                                return true;
                                            }
                                            sender.sendMessage(P + "The defaultgroup is now " + Z + group + S + ".");
                                            Permissions.getInstance().reload();
                                            return true;
                                        } else {
                                            sender.sendMessage(P + ChatColor.RED + "Group " + group + " not found!");
                                            return true;
                                        }
                                default:
                                    return false;
                            }
                        default:
                            return false;
                        case "group":
                            String group = args[1];
                            if (Permissions.getInstance().getGroupManager().getGroupByName(group) == null){
                                sender.sendMessage(P + ChatColor.RED + "Group " + group + " not found!");
                                return true;
                            }
                            switch (args[2].toLowerCase()){
                                case "delete":
                                    GData.deleteGroup(group);
                                    sender.sendMessage(P + "Group " + Z + group + S + " deleted!");
                                    return true;
                                default:
                                    return false;
                            }
                    }
                case 4:
                    switch (args[0].toLowerCase()){
                        case "user":
                            switch (args[2].toLowerCase()){
                                case "setgroup":
                                    String user = args[1];
                                    String group = args[3];
                                    if (new Profile(user).getUUID() != null){
                                        GroupPerms GP = Permissions.getInstance().getGroupManager().getGroupByName(group);
                                        if (GP != null){
                                            PData.setPlayerRank(GP.getRank(), user);
                                            sender.sendMessage(P + "Player " + Z + user + S +
                                                    " has been promoted to rank " + Z + group + S + ".");
                                            return true;
                                        } else {
                                            sender.sendMessage(P + ChatColor.RED + "Group " + group + " not found!");
                                            return true;
                                        }
                                    } else {
                                        sender.sendMessage(P + ChatColor.RED + "Player " + user + " doesn't exist!");
                                        return true;
                                    }
                            } //4 p group <group> inheritance reset
                        case "group":
                            String group = args[1];
                            if (Permissions.getInstance().getGroupManager().getGroupByName(group) == null){
                                sender.sendMessage(P + ChatColor.RED + "Group " + group + " not found!");
                                return true;
                            }
                            String value = args[3];
                            switch (args[2].toLowerCase()){
                                case "inheritance":
                                    if (value.equals("reset")){
                                        Permissions.getInstance().getGroupManager().getGroupByName(group).setInheritance(null);
                                        sender.sendMessage(P + "Group " + Z + group + S + "'s inheritance has been reset.");
                                        return true;

                                    }
                                    return false;
                                case "rank":
                                    if (!(value.length() <= 4)){
                                        sender.sendMessage(P + ChatColor.RED + "Rank value has maximum of 4 letters");
                                        return true;
                                    }
                                    for (int i=0; i < value.length(); i++){
                                        if (!Character.isLetter(value.charAt(i))) {
                                            sender.sendMessage(P + ChatColor.RED + "Rank value can only contain letters!");
                                            return true;
                                        }
                                    }
                                    if (value.equals("NRML")){
                                        sender.sendMessage(P + ChatColor.RED + "Please have a look after: " + Z + "/p user defaultgroup <group> ");
                                        return true;
                                    }
                                    Permissions.getInstance().getGroupManager().getGroupByName(group).setRank(value);
                                    Permissions.getInstance().reload();
                                    sender.sendMessage(P + "Group " + Z + group + S + "'s rank has been set to " + Z + value);
                                    return true;
                                case "rename":
                                    for (int i=0; i < value.length(); i++){
                                        if (!Character.isLetterOrDigit(value.charAt(i))) {
                                            sender.sendMessage(P + ChatColor.RED + "Name can only contain letters and numbers!");
                                            return true;
                                        }
                                    }
                                    GData.setGroupName(value, group);
                                    Permissions.getInstance().reload();
                                    sender.sendMessage(P + "Group " + Z + group + S + " has been renamed to " + Z + value + S + ".");
                                    return true;
                                case "setprefix":
                                    Permissions.getInstance().getGroupManager().getGroupByName(group).setPrefix(value);
                                    sender.sendMessage(P + "Group " + Z + group + S + "'s prefix has been set to " + value + " | "
                                            + ChatColor.translateAlternateColorCodes('&', value) + ".");

                                    return true;
                                case "setsuffix":
                                    Permissions.getInstance().getGroupManager().getGroupByName(group).setSuffix(value);
                                    sender.sendMessage(P + "Group " + Z + group + S + "'s suffix has been set to " + value + " | "
                                            + ChatColor.translateAlternateColorCodes('&', value) + ".");
                                    return true;
                                case "setladder":
                                    int ladder = 0;
                                    try {
                                        ladder = stringToInteger(value);
                                    } catch (TooBigNumberException e) {
                                        sender.sendMessage(P + ChatColor.RED + "The number you just gave, is way too big!");
                                        return true;
                                    } catch (NumberFormatException e) {
                                        sender.sendMessage(P + ChatColor.RED + "The amount must be in numbers!");
                                        return true;
                                    }
                                    GData.setGroupLadder(ladder, group);
                                    Permissions.getInstance().reload();
                                    sender.sendMessage(P + "Group " + Z + group + S + "'s ladder has been set to " + Z + ladder + S + ".");
                                    return true;
                                default:
                                    return false;
                            }
                        default:
                            return false;
                    }
                case 5:
                    String permissions = args[4];
                    boolean positive = true;
                    switch (args[0].toLowerCase()){
                        case "user":
                            if (!args[2].toLowerCase().equals("perms")){
                                return false;
                            }

                            String user = args[1];
                            if (new Profile(user).getUUID() != null){
                                switch (args[3].toLowerCase()){
                                    case "set":
                                        if (Bukkit.getPlayer(user) == null) {
                                            PData.setPermissions(permissions, user);
                                        } else {
                                            Permissions.getInstance().getPlayerManager()
                                                    .getPlayer(user).setPermissions(Util.stringToHashMap(permissions));
                                        }
                                        String negative = "";
                                        if (permissions.startsWith("-")){
                                            negative = " negative";
                                        }
                                        sender.sendMessage(P + CommandManager.formatName(user) + "old/iceball/network/permissions " +
                                                "has been set to " + negative + Z + permissions + S + ".");
                                        return true;
                                    case "add":
                                        negative = "";
                                        if (permissions.startsWith("-")){
                                            positive = false;
                                            negative = " negative";
                                        }
                                        if (Bukkit.getPlayer(user) == null) {
                                            PData.addPermissions(permissions, positive, user);
                                        } else {
                                            Permissions.getInstance().getPlayerManager()
                                                    .getPlayer(user).addPermissions(permissions, positive);
                                        }
                                        sender.sendMessage(P + "Node " + Z + permissions + S + " has been added to " + Z +
                                                CommandManager.formatName(user) + S + negative + " permissions.");
                                        return true;
                                    case "remove":
                                        if (Bukkit.getPlayer(user) == null) {
                                            PData.deletePermissions(permissions, user);
                                        } else {
                                            Permissions.getInstance().getPlayerManager()
                                                    .getPlayer(user).deletePermissions(permissions);
                                        }
                                        sender.sendMessage(P + "Node " + Z + permissions + S + " has been removed from " + Z +
                                                CommandManager.formatName(user) + S + " permissions.");
                                        return true;
                                    default:
                                        return false;
                                }
                            } else {
                                sender.sendMessage(P + ChatColor.RED + "Player " + user + " doesn't exist!");
                                return true;
                            }
                        case "group":
                            String group = args[1];
                            if (Permissions.getInstance().getGroupManager().getGroupByName(group) == null) {
                                sender.sendMessage(P + ChatColor.RED + "Group " + group + " doesn't exist!");
                                return true;
                            }
                            if (args[2].toLowerCase().equals("inheritance")){
                                String[] ranks = args[4].split(",");
                                switch (args[3].toLowerCase()){
                                    case "set":
                                        int i = 0;
                                        while (i < ranks.length) {
                                            String rank = ranks[i];
                                            if (rank.startsWith("-")) {
                                                String real = rank.replaceFirst("-", "");
                                                if (Permissions.getInstance().getGroupManager().getGroup(real) == null) {
                                                    sender.sendMessage(P + ChatColor.RED + "Rank " + real + " is not a rank!");
                                                    return true;
                                                }
                                            } else {
                                                if (Permissions.getInstance().getGroupManager().getGroup(rank) == null) {
                                                    sender.sendMessage(P + ChatColor.RED + "Rank " + rank + " is not a rank!");
                                                    return true;
                                                }
                                            }
                                            i++;
                                        }

                                        Permissions.getInstance().getGroupManager().getGroupByName(group).setInheritance(ranks);
                                        sender.sendMessage(P + "Group " + Z + group + S +
                                                "'s inheritance has been set to " + Z + Arrays.toString(ranks) + S + ".");
                                        return true;
                                }
                            }

                            if (args[2].toLowerCase().equals("perms")) {
                                    switch (args[3].toLowerCase()) {
                                        case "set":
                                            Permissions.getInstance().getGroupManager().getGroupByName(group)
                                                    .setPermissions(Util.stringToHashMap(permissions));
                                            String negative = "";
                                            if (permissions.startsWith("-")) {
                                                negative = " negative";
                                            }
                                            sender.sendMessage(P + "Group " + group + "old/iceball/network/permissions " +
                                                    "has been set to " + negative + Z + permissions + S + ".");
                                            return true;
                                        case "add":
                                            negative = "'s";
                                            if (permissions.startsWith("-")) {
                                                positive = false;
                                                negative = "'s negative";
                                            }
                                            Permissions.getInstance().getGroupManager().getGroupByName(group)
                                                    .addPermissions(permissions, positive);

                                            sender.sendMessage(P + "Node " + Z + permissions + S + " has been added to group " + Z +
                                                    group + S + negative + " permissions.");
                                            return true;
                                        case "remove":
                                            Permissions.getInstance().getGroupManager().getGroupByName(group)
                                                    .deletePermissions(permissions);
                                            sender.sendMessage(P + "Node " + Z + permissions + S + " has been removed from " + Z +
                                                    group + S + "'s permissions.");
                                            return true;
                                        case "reset":
                                            Permissions.getInstance().getGroupManager().getGroupByName(group)
                                                    .setPermissions(Util.stringToHashMap(""));
                                            sender.sendMessage(P + "Group " + group + "old/iceball/network/permissions " +
                                                    "has been reset.");
                                            return true;
                                        default:
                                            return false;
                                    }
                            }
                        default:
                            return false;
                    }
                case 7:
                    //       7 p group create <name> <rank> <prefix> <suffix> [ladder]
                    if (!(args[0].equals("group")) && (args[1].equals("create"))){
                        return false;
                    }
                    final String name = args[2];
                    for (int i=0; i < name.length(); i++){
                        if (!Character.isLetterOrDigit(name.charAt(i))) {
                            sender.sendMessage(P + ChatColor.RED + "Name can only contain letters and numbers!");
                            return true;
                        }
                    }
                    if (Permissions.getInstance().getGroupManager().getGroupByName(name) != null){
                        sender.sendMessage(P + ChatColor.RED + "Group " + name + " already exists!");
                        return true;
                    }
                    String rank = args[3];
                    if (!(rank.length() <= 4)){
                        sender.sendMessage(P + ChatColor.RED + "Rank value has maximum of 4 letters");
                        return true;
                    }
                    for (int i=0; i < rank.length(); i++){
                        if (!Character.isLetter(rank.charAt(i))) {
                            sender.sendMessage(P + ChatColor.RED + "Rank value can only contain letters!");
                            return true;
                        }
                    }
                    if (rank.equals("NRML")){
                        sender.sendMessage(P + ChatColor.RED + "NRML as rank isn't allowed right now," +
                         " use " + ChatColor.DARK_RED + "/p users defaultgroup <group>" + ChatColor.RED + " after the creation to set this as the default group. ");
                        return true;
                    }
                    if (Permissions.getInstance().getGroupManager().getGroup(rank) != null){
                        sender.sendMessage(P + ChatColor.RED + "There is already a group with " + rank + " as rank!");
                        return true;
                    }
                    int ladder = 0;
                    try {
                        ladder = stringToInteger(args[6]);
                    } catch (TooBigNumberException e) {
                        sender.sendMessage(P + ChatColor.RED + "The number you just gave, is way too big!");
                        return true;
                    } catch (NumberFormatException e) {
                        sender.sendMessage(P + ChatColor.RED + "The amount must be in numbers!");
                        return true;
                    }
                    String prefix = args[4];
                    String suffix = args[5];
                    GData.createGroup(name, rank, prefix, suffix, ladder);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                        Permissions.getInstance().reload();
                        }
                    }.runTaskLater(plugin, 20);
                    sender.sendMessage(P + "Group " + Z + name + S + " succesfully created!");
                    return true;
                default:
                    sender.sendMessage(P + ChatColor.RED + "Too many arguments!");
                    return true;
            }
        }
        return false;
    }

    private void helpReference(CommandSender sender, int page){
        switch (page){
            case 1:
                sender.sendMessage(P + "Command Reference Page 1 of 5");
                sender.sendMessage(S + "/p reload " + Z  + "Reloads the permissions core.");
                sender.sendMessage(S + "/p help [page] " + Z  + "Command reference help.");
                sender.sendMessage(S + "/p user <user> " + Z  + "Shows information about the user.");
                sender.sendMessage(S + "/p group <group> " + Z  + "Shows information about the group.");
                sender.sendMessage(S + "/p help 2 " + Z  + "For the next page!");
                return;
            case 2:
                sender.sendMessage(P + "Command Reference Page 2 of 5");
                sender.sendMessage(S + "/p rank <rank> " + Z  + "Shows information about the group (found by rank).");
                sender.sendMessage(S + "/p users defaultgroup <group> " + Z  + "Sets the new default group.");
                sender.sendMessage(S + "/p user <user> setgroup <group> " + Z  + "Sets the user in a new group.");
                sender.sendMessage(S + "/p user <user> perms set|add|remove <permissions> (add \"-\" without the quotes" +
                        " or spaces in front of your permissions to add/set as negative permissions)" + Z);
                sender.sendMessage(S + "/p help 3 " + Z  + "For the next page!");
                return;
            case 3:
                sender.sendMessage(P + "Command Reference Page 3 of 5");
                sender.sendMessage(S + "/p groups " + Z  + "Shows all the groups.");
                sender.sendMessage(S + "/p group <group> delete " + Z  + "Deletes the group.");
                sender.sendMessage(S + "/p group <group> rename <name> " + Z  + "Rename the group.");
                sender.sendMessage(S + "/p help 4 " + Z  + "For the next page!");
                return;
            case 4:
                sender.sendMessage(P + "Command Reference Page 4 of 5");
                sender.sendMessage(S + "/p group <group> rank <rank> " + Z + "Sets the new 4-letter rank word.");
                sender.sendMessage(S + "/p group <group> setprefix <prefix> " + Z + "Sets the new prefix.");
                sender.sendMessage(S + "/p group <group> setsuffix <suffix> " + Z + "Sets the new suffix.");
                sender.sendMessage(S + "/p group <group> setladder <ladder> " + Z + "Sets the new ladder. Anything above zero to accept inheritance. ");
                sender.sendMessage(S + "/p group <group> perms set|add|remove|reset <permissions>" + Z  + "Sets the permissions alias for the group. \"-\" in front to add as negative permissions.");
                sender.sendMessage(S + "/p help 5 " + Z  + "For the next page!");
                return;
            case 5:
                sender.sendMessage(P + "Command Reference Page 5 of 5");
                sender.sendMessage(S + "/p group <group> inheritance set <rank(s)> " + Z  + " Sets or resets a specific group for inheritance. ");
                sender.sendMessage(Z + "Add \"-\" in front to add as negative rank. Ranks are seperated by comma.");
                sender.sendMessage(S + "/p group <group> inheritance reset " + Z + "Resets the inheritance of the group.");
                sender.sendMessage(S + "/p group create <name> <rank> <prefix> <suffix> <ladder> " + Z  + "Creates a new group! Note: Rank cannot be longer than 4 letters and has to be unique.");
                sender.sendMessage(S + "This was the last page. Go back to the first page with " + Z + "/p help 1");
                return;
            default:
                sender.sendMessage(P + "Oops, the last page is number 5. You should look there.");
                break;
        }
    }
    private int stringToInteger(String s) throws TooBigNumberException, NumberFormatException {
        long i;
        try {
            i = Long.valueOf(s);
            if (i > 5000){
                throw new TooBigNumberException("Number is too big!");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a number!");
        }
        return (int) i;
    }
}
