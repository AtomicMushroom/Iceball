package network.iceball.bukkit.permissions;

import network.iceball.bukkit.permissions.group.GroupManager;
import network.iceball.bukkit.permissions.player.PlayerManager;
import network.iceball.bukkit.Iceball;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 26-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class Permissions {

    private static Permissions singleton = null;
    private Permissions(){}

    public static Permissions getInstance(){
        if (singleton == null){
            synchronized (Permissions.class){
                if (singleton == null){
                    singleton = new Permissions();
                }
            }
        }
        return singleton;
    }

    private GroupManager groupManager;
    private PlayerManager playerManager;
    private Iceball plugin;

    public void load(Iceball plugin){
        this.plugin = plugin;
        groupManager = new GroupManager(plugin, this);
        plugin.getServer().getLogger().info("[Iceball] Setting up IB-Permissions..");
        playerManager = new PlayerManager(plugin, this);
        for (Player player : Bukkit.getOnlinePlayers()){
            add(PPacket.getPacket(player.getName()));
        }
    }

    public void reload() {
        playerManager = new PlayerManager(plugin, this);
        groupManager = new GroupManager(plugin, this);
        while (!groupManager.isReady()) {
            synchronized (groupManager) {
                try {
                    groupManager.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()){
            add(PPacket.getPacket(player.getName()));
        }
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void add(PPacket packet){
        if (packet != null)
        playerManager.addPlayer(packet);
        groupManager.take(packet);
        playerManager.getPlayer(packet.getPlayer()).activate();
    }

    public void removePlayer(String name){
        playerManager.removePlayer(name);
        groupManager.remove(name);
    }

}
