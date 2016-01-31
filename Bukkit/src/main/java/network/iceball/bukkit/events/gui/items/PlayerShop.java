package network.iceball.bukkit.events.gui.items;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.events.gui.GUIStage;
import network.iceball.bukkit.events.gui.items.ColorShop.Superwool;
import network.iceball.bukkit.events.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Floris on 19-07-15.
 */
public class PlayerShop {

    private Iceball plugin;

    static Inventory SHOP_VANITYSHOP_COLORSHOP = Bukkit.createInventory(null, 54, ChatColor.BLUE + "           | ColorShop |");

    ItemStack wool;
    List<Superwool> woolList = new ArrayList<Superwool>();
    Superwool w;




    static Inventory SHOP_VANITYSHOP = Bukkit.createInventory(null, 54, ChatColor.RED + "           | Vanity Shop |");

    ItemStack colorshop = new ItemStack(Material.INK_SACK, 1, (byte)4);
    ItemMeta colorshopMeta = colorshop.getItemMeta();

    static Inventory SHOP_CHOOSE =
            Bukkit.createInventory(null, 54, ChatColor.GREEN + "                |  Shop  |");

    ItemStack chest = new ItemStack(Material.ENDER_CHEST, 1);
    ItemStack flower = new ItemStack(Material.DOUBLE_PLANT, 1, (byte)4);
    ItemStack paper = new ItemStack(Material.PAPER, 1);

    ItemMeta chestMeta = chest.getItemMeta();
    ItemMeta flowerMeta = flower.getItemMeta();
    ItemMeta paperMeta = paper.getItemMeta();


    public PlayerShop(Iceball plugin, Player player, GUIStage NORM ) {
        this.plugin = plugin;

        switch (NORM) {
            case CLEAR:

                return;
            case GAMESELECTOR:
                break;
            case SHOP_CHOOSE_SHOP:
                chestMeta.setDisplayName(ChatColor.GOLD + "Minigames Shop");
                List<String> loreC = new ArrayList<>();
                loreC.add(ChatColor.DARK_AQUA + "Buy all your minigame upgrades here!");
                chestMeta.setLore(loreC);
                chest.setItemMeta(chestMeta);

                flowerMeta.setDisplayName(ChatColor.RED + "Vanity Shop");
                List<String> loreF = new ArrayList<>();
                loreF.add(ChatColor.DARK_AQUA + "Buy all your awesome effects here!");
                flowerMeta.setLore(loreF);
                flower.setItemMeta(flowerMeta);

                paperMeta.setDisplayName(ChatColor.YELLOW + "Go Back");
                paper.setItemMeta(paperMeta);

                SHOP_CHOOSE.setItem(49, paper);
                SHOP_CHOOSE.setItem(21, chest);
                SHOP_CHOOSE.setItem(23, flower);

                player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 0.5f, 2f);
                GUI.guiStageHashMap.put(player.getName(), GUIStage.SHOP_CHOOSE_SHOP);
                player.openInventory(PlayerShop.SHOP_CHOOSE);
                return;
            case SHOP_VANITYSHOP:
                colorshopMeta.setDisplayName(ChatColor.BLUE + "Colored Headnames Shop");
                List<String> ListCS = new ArrayList<>();
                ListCS.add(ChatColor.AQUA + "Buy your own colored displayname here!");
                ListCS.add(" ");
                ListCS.add(ChatColor.YELLOW  + "Note:");
                ListCS.add(ChatColor.YELLOW  + "Works only in lobby and in singleplayer minigames!");
                colorshopMeta.setLore(ListCS);
                colorshop.setItemMeta(colorshopMeta);

                paperMeta.setDisplayName(ChatColor.YELLOW + "Go Back");
                paper.setItemMeta(paperMeta);

                SHOP_VANITYSHOP.setItem(16, colorshop);
                SHOP_VANITYSHOP.setItem(49, paper);
                player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 0.5f, 2f);
                GUI.guiStageHashMap.put(player.getName(), GUIStage.SHOP_VANITYSHOP);
                player.openInventory(PlayerShop.SHOP_VANITYSHOP);

                break;
            case SHOP_VANITYSHOP_COLORSHOP:
                paperMeta.setDisplayName(ChatColor.YELLOW + "Go Back");
                paper.setItemMeta(paperMeta);



                SHOP_VANITYSHOP_COLORSHOP.setItem(49, paper);
                player.playSound(player.getLocation(), Sound.HORSE_SADDLE, 0.5f, 2f);
                GUI.guiStageHashMap.put(player.getName(), GUIStage.SHOP_VANITYSHOP_COLORSHOP);
                player.openInventory(PlayerShop.SHOP_VANITYSHOP_COLORSHOP);
                break;
            case SHOP_MENUSHOP:



                break;
            case PROFILE:
                break;
            case ACHIEVEMENTS:
                break;
            default:
                break;
        }

    }


}
