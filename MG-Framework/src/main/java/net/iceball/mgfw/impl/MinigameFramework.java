package net.iceball.mgfw.impl;

import net.iceball.mgfw.impl.arena.ArenaLoader;
import net.iceball.mgfw.impl.game.api.Bomberman;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import network.iceball.bukkit.api.Database;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Floris on 21-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class MinigameFramework extends JavaPlugin {

    private MinigameFramework plugin;
    public final static Logger logger = Logger.getLogger("minecraft");
    public static String prefix = ChatColor.GOLD + "[" + ChatColor.DARK_AQUA +"MG-Framework" + ChatColor.GOLD +"] " + ChatColor.YELLOW;
    public static String suffix = ChatColor.YELLOW + "";
    public static String mark = ChatColor.GOLD + "";
    public static String log = "[MG-Framework] ";
    private static boolean isLobby = false;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        plugin = this;
        try {
            createTable();
        } catch (UnsupportedOperationException soe){
            getLogger().log(Level.WARNING, "Could not connect to database!");
            getLogger().log(Level.WARNING, "Disabling plugin..");
            getPluginLoader().disablePlugin(this);
            return;
        }
        new CommandManager(plugin);
        new EventManager(plugin);
        Config config = new Config(ConfigFile.config_yml, plugin);
        isLobby = config.getConfig().getString("servername").contains("lobby");
        ArenaLoader.getLoader().load(plugin);
        new BukkitRunnable(){
            @Override
            public void run() {
                if (!isLobby)
                    new Bomberman(plugin);
            }
        }.runTaskLater(plugin, 80);
    }

    public static boolean isLobby() {
        return isLobby;
    }

    public static void createTable() throws UnsupportedOperationException {
        String CMD = "CREATE TABLE IF NOT EXISTS gamedata (" +
                "last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "servername varchar(32), " +
                "game varchar(16) DEFAULT NULL," +
                "online_players smallint(4) DEFAULT 0," +
                "free_maps varchar(70) DEFAULT NULL," +
                "used_maps varchar(70) NOT NULL," +
                "PRIMARY KEY (servername)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
        try {
            Connection connection = Database.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.execute(CMD);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedOperationException soe){
            throw soe;
        }
    }
}
