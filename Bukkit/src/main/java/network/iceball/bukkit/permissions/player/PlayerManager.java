package network.iceball.bukkit.permissions.player;

import com.sun.istack.internal.Nullable;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.PPacket;
import network.iceball.bukkit.permissions.Permissions;

import java.util.HashMap;

/**
 * Created by Floris on 02-11-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class PlayerManager {

    private HashMap<String, PlayerPerms> players;
    private Iceball plugin;

    public PlayerManager(Iceball plugin, Permissions s){
        this.plugin = plugin;
        players = new HashMap<>();
    }

    public void addPlayer(PPacket packet){
        players.put(packet.getPlayer(), new PlayerPerms(packet, plugin));
    }

    public void removePlayer(String name){
        players.remove(name);}

    public @Nullable PlayerPerms getPlayer(String name){
        return players.get(name);
    }

}
