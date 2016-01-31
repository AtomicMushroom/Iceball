package network.iceball.bukkit.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.data.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 09-07-15.
 */
class ProfileCommand implements CommandExecutor {
    private Iceball plugin;
    private final String channel = "Iceball";

    public ProfileCommand(Iceball plugin){
        this.plugin = plugin;
        registerChannels();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arguments) {
        if (cmd.getName().equalsIgnoreCase("profile")) {
           if (!CommandManager.hasPermission(sender, "iceball.profile")) {
                return true;
            }

            if (arguments.length == 1) {
                String target = arguments[0];

                String format = CommandManager.formatName(target);
                String errFormat = CommandManager.errorformatName(target);

                String[] info = new Profile(target).getProfile();
                if (info == null || info[0].isEmpty()) {
                    sender.sendMessage(Iceball.pluginPrefix + ChatColor.RED + "We couldn't find " + errFormat + " in our database. :(");
                    return true;
                }
                Player player = null;
                sender.sendMessage(Iceball.pluginPrefix + "We have the following information about " + format + ":");
                boolean isPlayer = false;
                if (sender instanceof Player){
                    player = (Player) sender;

                    String[] clone = info;
                    info = new String[clone.length -2];
                    for (int i=0; i < info.length; i++){
                        info[i] = clone[i];
                    }

                }
                for (int i=0; i < info.length; i++){
                    if (!(isPlayer && i==info.length-2)){
                    sender.sendMessage(info[i]);
                    }
                }
                sender.sendMessage("\n");
                    if (player != null){ String[] msg = { "profile", target, player.getName() };
                        sendMessage(player, msg); }
                return true;
            } else {
                return false;
            }

        }
        return false;
    }
    public void registerChannels(){
        if (!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, channel)){
            this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, channel);
        }
    }
    private void sendMessage(Player player, String[] message) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (int i=0; i < message.length; i++){
        out.writeUTF(message[i]);
        }
        player.sendPluginMessage(this.plugin, channel, out.toByteArray());
    }
}
