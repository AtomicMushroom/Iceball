package network.iceball.bukkit.events;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.events.gui.GUI;
import network.iceball.bukkit.util.Config;
import network.iceball.bukkit.util.ConfigFile;

/**
 * Created by Floris on 07-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class EventManager {
    private Iceball plugin;
    public static Boolean Chat;
    public static Boolean isLobby;
    private AsyncChatEvent chatEvent;

    public EventManager(Iceball plugin){
        this.plugin = plugin;
        Config config = new Config(ConfigFile.config_yml, plugin);

        new LeaveEvent(plugin);
        new JoinEvent(plugin);

        Chat = Boolean.parseBoolean(config.getConfig().getString("Iceball.Lobby-chat"));
        if (Chat) {
            chatEvent = new AsyncChatEvent(plugin, true);
            Iceball.logger.info("[Iceball] Lobby-chat enabled!");
        } else {
            Iceball.logger.info("[Iceball] Lobby-chat disabled!");
            chatEvent = new AsyncChatEvent(plugin, false);
        }

        isLobby = Boolean.parseBoolean(config.getConfig().getString("Iceball.Lobby"));
        if (isLobby){
            Iceball.logger.info("[Iceball] Lobby enabled!");
            new GUI(this.plugin);
        } else {
            Iceball.logger.info("[Iceball] Lobby disabled!");
        }
    }
}
