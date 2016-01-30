package net.iceball.mgfw.impl;

import net.iceball.mgfw.impl.game.signs.GameSignManager;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by Floris on 21-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
class EventManager {

    private MinigameFramework plugin;
    private BukkitTask task = null;
    private Config config;

    public EventManager(MinigameFramework plugin) {
        this.plugin = plugin;
        config = new Config(ConfigFile.config_yml, plugin);
        String servername = config.getConfig().getString("servername");
        if (servername == null) {
            plugin.getLogger().info("Servername is not set! Set the servername property in config.yml.");
            return;
        }
        boolean gamesigns = config.getConfig().getBoolean("gamesigns");
        if (gamesigns) {
            task = Bukkit.getScheduler().runTaskTimer(plugin, new GameSignManager(plugin), 25L, 40L);
            return;
        }
        plugin.getLogger().info("Gamesigns disabled!");
    }
}
