package network.iceball.bukkit.events.gui.items.ColorShop;

import org.bukkit.ChatColor;

/**
 * Created by Floris on 20-07-15.
 */
public class Superwool {
    byte ID;
    String color;
    String name;
    int price;
    String desc;
    boolean isBought;

    public Superwool(int ID, String color, String name, String desc, int price, boolean isBought){
        this.ID = (byte)ID;
        this.color = color;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.isBought = isBought;
    }

    public byte getID() {
        return ID;
    }

    public String getColor() {
        return ChatColor.translateAlternateColorCodes('&',color);
    }

    public String getName() {
        return getColor() + name;
    }
}