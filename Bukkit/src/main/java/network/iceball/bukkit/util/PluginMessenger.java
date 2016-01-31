package network.iceball.bukkit.util;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import network.iceball.bukkit.Iceball;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Created by Floris on 13-07-15.
 */
public class PluginMessenger implements PluginMessageListener {

    private Iceball plugin;

    private String name;
    private String ipaddress_player;

    public PluginMessenger(Iceball plugin) {
        this.plugin = plugin;
        registerChannels();
    }

    public void registerChannels(){
        if (!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, "BungeeCord")){
            this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        }
        if (!this.plugin.getServer().getMessenger().isIncomingChannelRegistered(this.plugin, "BungeeCord")){
            this.plugin.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, "BungeeCord", this);
        }
    }
    public void RequestIP(Player player) {
        name = player.getName();
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("IP");
        player.sendPluginMessage(this.plugin, "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
       /* String out = null;
     try {
            out = new String(message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.plugin.getLogger().info("Got Plugin Message on " + channel + " from " + player.getName() + " message was: " + out);*/
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("IP")) {
            ipaddress_player = in.readUTF();
            plugin.getLogger().info("Found ipaddress [" + ipaddress_player + "] of player '" + name + "'!");
        }
    }
}

