package network.iceball.bukkit.events.gui.events;

import network.iceball.bukkit.events.gui.GUI;
import network.iceball.bukkit.events.gui.GUIStage;
import network.iceball.bukkit.events.gui.items.PlayerShop;
import network.iceball.bukkit.Iceball;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Floris on 19-07-15.
 */
public class ClickEvent implements Listener {

    private Iceball plugin;


    public ClickEvent(Iceball plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (player.hasPermission("iceball.lobby")) {

            ItemStack clicked = event.getCurrentItem(); // The item that was clicked
            String Item = clicked.getType().name().toUpperCase();
          //  player.sendMessage(ChatColor.YELLOW + "Item: |" + Item + "|" + " Stage: |" + GUI.guiStageHashMap.get(player.getName()) + "|");

            switch (GUI.guiStageHashMap.get(player.getName())){
                case CLEAR:
                    switch (Item) {
                        case "COMPASS":
                            player.sendMessage("compass");
                            event.setCancelled(true);
                            break;
                        case "DIAMOND":
                            player.sendMessage("diamond");
                            event.setCancelled(true);
                            player.getOpenInventory();
                            break;
                        case "EMERALD":
                            player.sendMessage("emerald");
                            new PlayerShop(plugin, player, GUIStage.SHOP_CHOOSE_SHOP);
                            event.setCancelled(true);
                            break;
                        default:
                            event.setCancelled(false);
                            break;
                    }
                    break;
                case GAMESELECTOR:
                    break;
                case SHOP_CHOOSE_SHOP:
                    switch (Item) {
                        case "ENDER_CHEST":
                            player.sendMessage("ender_chest");
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "Still under development!");
                            player.closeInventory();
                            break;
                        case "DOUBLE_PLANT":
                            player.sendMessage("double_plant");
                            event.setCancelled(true);
                            new PlayerShop(plugin, player, GUIStage.SHOP_VANITYSHOP);
                            break;
                        case "PAPER":
                            player.sendMessage("paper");
                            event.setCancelled(true);
                            player.closeInventory();
                            break;
                        default:
                            event.setCancelled(false);
                            break;
                    }
                    break;
                case SHOP_VANITYSHOP:
                    switch (Item) {
                        case "INK_SACK":
                            player.sendMessage("INK_sack");

                            event.setCancelled(true);
                            new PlayerShop(plugin, player, GUIStage.SHOP_VANITYSHOP_COLORSHOP);
                            break;
                        case "PAPER":
                            player.sendMessage("paper");
                            event.setCancelled(true);
                            new PlayerShop(plugin, player, GUIStage.SHOP_CHOOSE_SHOP);
                            break;
                        default:
                            event.setCancelled(false);
                            break;
                    }
                    break;
                case SHOP_VANITYSHOP_COLORSHOP:
                    switch (Item) {
                        case "PAPER":
                            player.sendMessage("paper");
                            event.setCancelled(true);
                            new PlayerShop(plugin, player, GUIStage.SHOP_VANITYSHOP);
                            break;
                        default:
                            event.setCancelled(false);
                            break;
                    }
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
    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();
        String current = player.getItemInHand().getType().name();
      //  event.getPlayer().sendMessage("Current name: |" + current + "|");
                switch (current) {
                    case "COMPASS":
                        break;
                    case "DIAMOND":
                        break;
                    case "EMERALD":
                        new PlayerShop(plugin, player, GUIStage.SHOP_CHOOSE_SHOP);
                        break;
                    case "SKULL_ITEM":
                        break;
                    case "RAW_FISH":
                        break;
                    default:
                        break;

                }



    }
}
