package network.iceball.bukkit.events.gui.events;

import network.iceball.bukkit.Iceball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Created by Floris on 19-07-15.
 */
public class PlayerDrop implements Listener{

    private Iceball plugin;

    public PlayerDrop(Iceball plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler //TODO: split all events to their own classes!
    public void playerDropItem(PlayerDropItemEvent event){
        if (event.getPlayer().hasPermission("iceball.lobby")) {
            event.setCancelled(true);
        }
    }
}
