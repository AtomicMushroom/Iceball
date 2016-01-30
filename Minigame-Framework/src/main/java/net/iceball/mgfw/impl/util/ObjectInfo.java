package net.iceball.mgfw.impl.util;

import org.bukkit.Location;

/**
 * Created by Floris on 22-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class ObjectInfo {

    public static String locationDescription(Location loc){
        String location = "";
        if (loc == null){
            return "Not defined yet.";
        }
        String pitch = Float.toString(loc.getPitch()).substring(0, 4);
        String yaw = Float.toString(loc.getYaw()).substring(0, 4);
        location = String.format("X: %d, Y: %d, Z: %d, Yaw: %s, Pitch: %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), yaw, pitch);

        return location;
    }
    public static String blockDescription(Location loc){
        String location = "";
        if (loc == null){
            return "Not defined yet.";
        }
        location = String.format("X: %d, Y: %d, Z: %d", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        return location;
    }



}
