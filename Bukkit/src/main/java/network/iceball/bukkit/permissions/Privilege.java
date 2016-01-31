package network.iceball.bukkit.permissions;

import java.util.HashMap;

/**
 * Created by Floris on 20-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public interface Privilege {

    /**
     * Sets the permissions standard for the given instance.
     * @param permissions Map<String, Boolean> permissions, String is the permissions, value negative means the
     *                    instance will not have the permissions.
     */
    void setPermissions(HashMap<String, Boolean> permissions);

    /**
     * Add permissions to the permissions map.
     * @param permissions the permissions that will be added to the user
     * @param value whether the player will have the permissions (true) or will not have the permissions (false)
     */
    void addPermissions(String permissions, boolean value);

    /**
     * Deletes the specific permissions
     * @param permissions the permissions that will be deleted from the map.
     */
    void deletePermissions(String permissions);

    /**
     * The whole sha-bang that contains all the permissions of the given instance
     * @return Map <String,Boolean> permissions, String is the permissions, value negative means the
     *                    instance will not have the permissions.
     */
    HashMap<String, Boolean> getPermissions();

    /**
     * Updates the permissions for the given instance.
     */
    void update();

    String getPrefix();
    String getSuffix();
}
