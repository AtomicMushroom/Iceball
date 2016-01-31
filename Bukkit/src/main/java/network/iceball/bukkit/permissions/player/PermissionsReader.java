package network.iceball.bukkit.permissions.player;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.group.GroupPerms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 26-11-15.
 * Email: florisgra@gmail.com
 * <p>
 * Magic. Do not touch.
 */

public class PermissionsReader {

    public PermissionsReader(PlayerPerms pp) {

        if (pp.getPermissions().containsKey("donate.rankpromote")) {
            Player player = Bukkit.getPlayer(pp.getPlayer().getName());
            if (player != null) {
                String name = player.getName();
                String group = " the next rank";
                GroupPerms GP = Permissions.getInstance().getGroupManager().getGroupByPlayer(name);
                if (GP != null) {
                    group = GP.getName();
                }
                player.sendMessage(Iceball.pluginPrefix + "Congratulations! You were promoted to " + Iceball.pluginSuffixMARK + group + Iceball.pluginSuffixNRML + "!");
                pp.deletePermissions("donate.rankpromote");
            }
        }

        if (pp.getPermissions().containsKey("*")){
            pp.getPlayer().setOp(true);
        } else {
            pp.getPlayer().setOp(false);
        }
    }
}
