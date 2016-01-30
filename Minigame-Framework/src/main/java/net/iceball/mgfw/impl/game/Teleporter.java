package net.iceball.mgfw.impl.game;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Floris on 10-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public class Teleporter {

    private JavaPlugin plugin;
    private final String name;

    public Teleporter(JavaPlugin plugin, String name){
        this.plugin = plugin;
        this.name = name;
        registerChannels();
    }

    private void registerChannels() {
        if(!plugin.getServer().getMessenger().isOutgoingChannelRegistered(plugin, "Iceball")) {
            plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "Iceball");
        }
        if(!this.plugin.getServer().getMessenger().isOutgoingChannelRegistered(this.plugin, "BungeeCord")) {
            this.plugin.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
        }
    }

    public void joinLobby() {
        Player player = Bukkit.getPlayer(name);
        if (player != null)
        new BukkitRunnable() {
            @Override
            public void run() {
                sendMessage(player, new String[]{"lobby", player.getName()}, "Iceball");
            }
        }.runTaskLater(plugin, 35);
    }

    public void joinServer(String server){
        Player player = Bukkit.getPlayer(name);
        if (player != null)
        sendMessage(player, new String[]{"Connect", server}, "BungeeCord");
    }

    private void sendMessage(Player player, String[] message, String channel) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for(int i = 0; i < message.length; ++i) {
            out.writeUTF(message[i]);
        }
        player.sendPluginMessage(plugin, channel, out.toByteArray());
    }
}
