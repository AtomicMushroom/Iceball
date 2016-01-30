package network.iceball;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import network.iceball.commands.PMCommand;
import network.iceball.database.Data;

import java.util.HashSet;

public class EventListener implements Listener {
    
    private Iceball plugin;
    private HashSet<String> players;

    public EventListener(Iceball plugin){
        this.plugin = plugin;
        plugin.getProxy().registerChannel("Iceball");
        plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
        players = new HashSet<>();
    }

    @EventHandler
    public void onServerConnected(final ServerConnectedEvent event) {
        //event.getPlayer().sendMessage(new ComponentBuilder("Welcome to server: " + event.getServer().getInfo().getName() + "!").color(ChatColor.GREEN).create());
        String name = event.getPlayer().getName();
        if (!players.contains(name)) {
            Data.storeUUID(event.getPlayer());
            players.add(name);
            plugin.getLogger().info(event.getPlayer().getName() + " joined " + event.getServer().getInfo().getName());
        }
    }
    @EventHandler
    public void onServerLeave(PlayerDisconnectEvent event) {
        String name = event.getPlayer().getName();
        String ip = event.getPlayer().getAddress().getHostString();
        PMCommand.filter.remove(name);
        Data.storeLastSeen(name);
        Data.storeIP(name, ip);
    }
}