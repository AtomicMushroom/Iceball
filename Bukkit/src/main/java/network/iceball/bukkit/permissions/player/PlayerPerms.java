package network.iceball.bukkit.permissions.player;

import com.sun.istack.internal.Nullable;
import network.iceball.bukkit.permissions.PPacket;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.permissions.Privilege;
import network.iceball.bukkit.permissions.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Floris on 26-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class PlayerPerms implements Privilege {

    private JavaPlugin plugin;
    private final String name;
    private HashMap<String, Boolean> permissionsMap;
    private String prefix;
    private String suffix;

    public PlayerPerms(PPacket packet, JavaPlugin plugin) {
        this.plugin = plugin;
        this.name = packet.getPlayer();
        permissionsMap = packet.getPermissions() != null ? packet.getPermissions() : new HashMap<>();   //PData.getPermissions(name);
        update();
    }

    public org.bukkit.entity.Player getPlayer(){
        return Bukkit.getPlayer(name);
    }

    @Override
    public void setPermissions(@Nullable HashMap<String, Boolean> permissions) {
        if (permissions == null){
            permissions = new HashMap<>(); //plugin
            for (PermissionAttachmentInfo PermissionInfo: getPlayer().getEffectivePermissions()) {
                PermissionAttachment Attachment = PermissionInfo.getAttachment();
                if (Attachment != null) {
                    Map <String, Boolean> Flags = Attachment.getPermissions();
                    for (String perm: Flags.keySet()) {
                        updatePermissions(perm, false); //in-game
                    }
                }
            }
        }
        this.permissionsMap = permissions; //plugin
        PData.setPermissions(Util.hashMapToString(permissionsMap), name); //database
        update(); //in-game
    }

    @Override
    public void addPermissions(String permissions, boolean value) {
        this.permissionsMap.put(permissions, value); //plugin
        PData.addPermissions(permissions, value, name); //database
        updatePermissions(permissions, value); //in-game
    }

    @Override
    public void deletePermissions(String permissions) {
        this.permissionsMap.remove(permissions); //plugin
        PData.deletePermissions(permissions, name); //database
        updatePermissions(permissions, false); //in-game
    }

    @Override
    public HashMap<String, Boolean> getPermissions() {
        return permissionsMap;
    }

    @Override
    public void update() {
        Set<Map.Entry<String, Boolean>> set = permissionsMap.entrySet();
        for (Map.Entry<String, Boolean> me : set) {
            updatePermissions(me.getKey(), me.getValue());
        }
        activate();
    }

    public void addOfflinePermissions(String permissions, boolean value) {
        this.permissionsMap.put(permissions, value); //plugin
        updatePermissions(permissions, value); //in-game
        //not added into database
    }
    public void addOfflinePermissions(HashMap<String, Boolean> permissionsMap) {
        if (permissionsMap != null)
        for (Map.Entry<String, Boolean> entry : permissionsMap.entrySet()){
            String permissions = entry.getKey();
            Boolean value = entry.getValue();
            this.permissionsMap.put(permissions, value); //plugin
            updatePermissions(permissions, value); //in-game
        }
        //not added into database
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public String getPrefix() {
        if (prefix == null) { prefix = Permissions.getInstance().getGroupManager().getGroupByPlayer(name).getPrefix();}
        if (prefix == null) { return "";}
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String getSuffix() {
        if (suffix == null) { suffix = Permissions.getInstance().getGroupManager().getGroupByPlayer(name).getSuffix();}
        if (suffix == null) { return "";}
        return ChatColor.translateAlternateColorCodes('&', suffix);
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private void updatePermissions(String permission, boolean value){
        PermissionAttachment permissionAttachment = getPlayer().addAttachment(plugin, permission, value);
        permissionAttachment.setPermission(permission, value);
    }

    public void activate() {
        new PermissionsReader(this);
    }
}
