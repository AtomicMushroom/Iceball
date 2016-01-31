package network.iceball.bukkit.events;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Floris on 17-07-15.
 */
public class AsyncChatEvent implements Listener {
    private Iceball plugin;
    public boolean ChatEnabled = true;
    private static String firstGroup;

    public AsyncChatEvent(Iceball plugin, boolean chatEnabled){
        this.ChatEnabled = chatEnabled;
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    @EventHandler(priority= EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        String format;
        String message = event.getMessage();
        String name = player.getName();

        if (ChatEnabled) {

            if (Permissions.getInstance().getPlayerManager().getPlayer(name) != null) {

                String prefix = Permissions.getInstance().getPlayerManager().getPlayer(name).getPrefix();
                String suffix = Permissions.getInstance().getPlayerManager().getPlayer(name).getSuffix();

                //plugin.getServer().getLogger().log(Level.WARNING, "That took " + (endTime - startTime) + " nanoseconds");
                if (prefix == null || suffix == null){ return; }
                prefix = ChatColor.translateAlternateColorCodes('&', prefix);
                suffix = ChatColor.translateAlternateColorCodes('&', suffix);

                int length = getLetters(ChatColor.stripColor(prefix));
                //plugin.getServer().getLogger().info("2 Prefix: " + prefix + ", length: " + length);
                boolean emptyPrefix = false;

                if (length == 0){
                    format = prefix + name + ChatColor.BLACK +  ": " + suffix + message;
                    emptyPrefix = true;
                } else {
                    format = prefix + " " +  name  + ChatColor.BLACK +  ": " + suffix + message;
                }
                    //plugin.getServer().getLogger().info("Emptyprefix: " + String.valueOf(emptyPrefix));
                if (player.hasPermission("iceball.chatcolors") && message.contains("&")){
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    if (emptyPrefix){
                        format = prefix + name + ChatColor.BLACK +  ": " + suffix + message;
                    } else
                    format = prefix + " " + name + ChatColor.BLACK +  ": " + suffix + message;
                }
                event.setFormat(format);
            }
        } else {
            event.setFormat(ChatColor.YELLOW + name + ": " + ChatColor.WHITE + message);
        }
        //Thradrys: no way..

    }
    public static int getLetters(String s){
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)))
                counter++;
        }
        return counter;
    }
}
