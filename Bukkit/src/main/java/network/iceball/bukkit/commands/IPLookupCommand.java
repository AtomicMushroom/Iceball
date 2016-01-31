package network.iceball.bukkit.commands;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.data.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Floris on 10-07-15.
 * Email: florisgra@gmail.com-
 *
 * Magic. Do not touch.
 */
class IPLookupCommand implements CommandExecutor {

    private Iceball plugin;

    public IPLookupCommand(Iceball plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("iplookup")) {
            if (args.length == 1) {
                if (!CommandManager.hasPermission(sender, "iceball.iplookup")){
                    return true;
                }
                String target = args[0];
                String format = formatName(target);
                String errFormat = errorformatName(target);

                String ip = new Profile(target).getAddress();
                if (ip.equals("null")){
                    sender.sendMessage(Iceball.pluginPrefix + ChatColor.RED + "We couldn't find the ip of " + errFormat + ".");
                    return true;
                }
                sender.sendMessage(Iceball.pluginPrefix + format + " ip address: "  + ip + Iceball.pluginSuffixMARK + ".");
                return true;
            } else {
                return false;
            }
        }
            return false;
    }
    private String formatName(String name){
        if (name.endsWith("s")){
            name = Iceball.pluginSuffixMARK + name + "' profile"  + Iceball.pluginSuffixNRML;
            return name;
        } else {
            name = Iceball.pluginSuffixMARK + name + "'s profile" + Iceball.pluginSuffixNRML;
            return name;
        }
    }
    private String errorformatName(String name){
        if (name.endsWith("s")){
            name = ChatColor.DARK_RED + "" + name + "' profile"  + ChatColor.RED;
            return name;
        } else {
            name = ChatColor.DARK_RED + "" + name + "'s profile" + ChatColor.RED;
            return name;
        }
    }
}
