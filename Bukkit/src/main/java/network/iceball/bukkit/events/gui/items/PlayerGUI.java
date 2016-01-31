package network.iceball.bukkit.events.gui.items;

import network.iceball.bukkit.commands.CommandManager;
import network.iceball.bukkit.data.Coins;
import network.iceball.bukkit.Iceball;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Material.COMPASS;

/**
 * Created by Floris on 19-07-15.
 */
public class PlayerGUI {

    private Iceball plugin;

    static Inventory GUI =
            Bukkit.createInventory(null, 54, ChatColor.GOLD + "Click to join a lobby!");

    static ItemStack compass = new ItemStack(COMPASS, 1);
    static ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
    static ItemStack emerald = new ItemStack(Material.EMERALD, 1);
    static ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
    static ItemStack fish = new ItemStack(Material.RAW_FISH, 1, (byte)3);

    ItemMeta compassItemMeta = compass.getItemMeta();
    ItemMeta diamondItemMeta = diamond.getItemMeta();
    ItemMeta emeraldItemMeta = emerald.getItemMeta();

    ItemMeta fishItemMeta = fish.getItemMeta();

    public PlayerGUI(Iceball plugin, final Player player) {
        this.plugin = plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                setLobbyKit(player);
            }
        }.runTaskLater(plugin, 3);
    }

    public void setLobbyKit(Player player) {
        compassItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Lobby Selector " + ChatColor.GRAY + "(Right Click)");
        compass.setItemMeta(compassItemMeta);
        diamondItemMeta.setDisplayName(ChatColor.AQUA + "Achievements " + ChatColor.GRAY + "(Right Click)");
        diamond.setItemMeta(diamondItemMeta);
        emeraldItemMeta.setDisplayName(ChatColor.GREEN + "Shop " + ChatColor.GRAY + "(Right Click)");
        List<String> lore = new ArrayList<>();
        lore.add("");
        int coins = Coins.getCoins(player.getName());
        lore.add(ChatColor.GRAY + "Balance: " + ChatColor.GOLD + coins);
        emeraldItemMeta.setLore(lore);

        emerald.setItemMeta(emeraldItemMeta);


        SkullMeta meta = (SkullMeta)skull.getItemMeta();
        meta.setOwner(player.getName());
        String format = CommandManager.formatName(player.getName());
        meta.setDisplayName(ChatColor.RED + format);
        skull.setItemMeta(meta);

        fishItemMeta.setDisplayName(ChatColor.YELLOW + "Particle Selector " + ChatColor.GRAY + "(Right Click)");
        fish.setItemMeta(fishItemMeta);



        player.getInventory().setItem(0, compass);
        player.getInventory().setItem(2, skull);
        player.getInventory().setItem(3, diamond);
        player.getInventory().setItem(4, emerald);
        player.getInventory().setItem(6, fish);
    }

}
