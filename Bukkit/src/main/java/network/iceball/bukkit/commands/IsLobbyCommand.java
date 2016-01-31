package network.iceball.bukkit.commands;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.util.Config;
import network.iceball.bukkit.util.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by Floris on 19-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
class IsLobbyCommand implements CommandExecutor {

    private Iceball plugin;

    public IsLobbyCommand(Iceball plugin){
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("islobby")) {
            if (args.length == 1){
                String value = args[0];
                Config config = new Config(ConfigFile.config_yml, plugin);
                if (value.equalsIgnoreCase("false")){
                    config.getConfig().set("Iceball.isLobby", false);
                    config.saveConfig();
                    sender.sendMessage(Iceball.pluginPrefix + "Lobby disabled!");
                    return true;
                } else if (value.equalsIgnoreCase("true")){
                    config.getConfig().set("Iceball.isLobby", true);
                    config.saveConfig();
                    sender.sendMessage(Iceball.pluginPrefix + "Lobby enabled");
                    return true;
                } else {
                    sender.sendMessage(Iceball.pluginPrefix + "Argument can only be true or false!");
                    return true;
                }
            } else {
                sender.sendMessage(Iceball.pluginPrefix + ChatColor.RED + "You need arguments!");
                return false;
            }

        }
        return true;
    }
}
