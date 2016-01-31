package net.iceball.mgfw.impl.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Floris on 10-09-15.
 */
public class Serialization {

    public static String serializeLocation(Location loc){
        String location = "";
        if (loc == null){
            return "Not defined yet.";
        }
        String pitch = Float.toString(loc.getPitch());
        String yaw = Float.toString(loc.getYaw());
        String world = loc.getWorld().getName();

        location = String.format("X: %d, Y: %d, Z: %d, Yaw: %s, Pitch: %s, World: %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), pitch, yaw, world);
        return location;
    }

    public static Location deserializeLocation(String s){
        if (s == null || s.isEmpty()){
            return null;
        }
        String[] sort = s.split(",");

        String[] values = new String[6];
        for (int i=0; i < values.length; i++){
            values[i] = sort[i].split(":")[1].trim();
        }
        int x = Integer.valueOf(values[0]);
        int y = Integer.valueOf(values[1]);
        int z = Integer.valueOf(values[2]);
        Float pitch = Float.parseFloat(String.valueOf(values[3]));
        Float yaw = Float.parseFloat(String.valueOf(values[4]));
        World world = Bukkit.getServer().getWorld(String.valueOf(values[5]));
        return new Location(world, x, y, z, pitch, yaw);
    }

    public static String serializeBlock(Location loc) {
        String location = "";
        if (loc == null){
            return "Not defined yet.";
        }
        String world = loc.getWorld().getName();
        location = String.format("X: %d, Y: %d, Z: %d, World: %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), world);
        return location;
    }

    public static Location deserializeBlock(String s){
        if (s == null || s.isEmpty()){
            return null;
        }
        String[] sort = s.split(",");

        String[] values = new String[4];
        for (int i=0; i < values.length; i++){
            values[i] = sort[i].split(":")[1].trim();
        }
        int x = Integer.valueOf(values[0]);
        int y = Integer.valueOf(values[1]);
        int z = Integer.valueOf(values[2]);
        World world = Bukkit.getServer().getWorld(String.valueOf(values[3]));
        return new Location(world, x, y, z);
    }
}

