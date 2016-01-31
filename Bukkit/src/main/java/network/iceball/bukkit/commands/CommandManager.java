package network.iceball.bukkit.commands;

import network.iceball.bukkit.Iceball;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 06-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class CommandManager {

    public Iceball plugin;

    public CommandManager(Iceball plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("coins").setExecutor(new CoinsCommand(this.plugin));
        this.plugin.getCommand("profile").setExecutor(new ProfileCommand(this.plugin));
        this.plugin.getCommand("iplookup").setExecutor(new IPLookupCommand(this.plugin));
        this.plugin.getCommand("p").setExecutor(new PCommand(this.plugin));
        this.plugin.getCommand("islobby").setExecutor(new IsLobbyCommand(this.plugin));
        this.plugin.getCommand("stp").setExecutor(new STPCommand(this.plugin));
    }
    public static boolean hasPermission(CommandSender sender, String perms){
        if (!(sender instanceof Player)){
            return true; //console always must have access
        }
        Player player = (Player) sender;
        if (player.hasPermission(perms) || player.isOp()){
            return true;
        } else {
            sender.sendMessage(Iceball.pluginPrefix + ChatColor.RED + "You don't have " + perms + "!");
            return false;
        }
    }
    public static String formatName(String name){
        if (name.endsWith("s")){
            name = Iceball.pluginSuffixMARK + name + "' profile"  + Iceball.pluginSuffixNRML;
            return name;
        } else {
            name = Iceball.pluginSuffixMARK + name + "'s profile" + Iceball.pluginSuffixNRML;
            return name;
        }
    }
    public static String errorformatName(String name){
        if (name.endsWith("s")){
            name = ChatColor.DARK_RED + "" + name + "' profile"  + ChatColor.RED;
            return name;
        } else {
            name = ChatColor.DARK_RED + "" + name + "'s profile" + ChatColor.RED;
            return name;
        }
    }

}
