package network.iceball.bukkit.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.iceball.bukkit.Iceball;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 27-11-15.
 * Email: florisgra@gmail.com
 * <p>
 * Magic. Do not touch.
 */
class STPCommand implements CommandExecutor{
    private Iceball plugin;
    private final String channel = "Iceball";

    public STPCommand(Iceball plugin) {
        this.plugin = plugin;
        registerChannels();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arguments) {
        if (cmd.getName().equalsIgnoreCase("stp")) {
            if (sender instanceof Player){
                Player player = (Player) sender;
                if (arguments.length == 1){
                    String[] msg = {"stp", arguments[0], player.getName()};
                    sendMessage(player, msg);
                    return true;
                }
                return false;
            } else {
            sender.sendMessage(Iceball.pluginPrefix + ChatColor.RED + "You have to be player for this command!");
            return true;
            }
        }
            return false;
    }

    public void registerChannels() {
        if (!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, channel)) {
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
