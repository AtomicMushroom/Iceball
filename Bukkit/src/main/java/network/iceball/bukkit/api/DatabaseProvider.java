package network.iceball.bukkit.api;

import network.iceball.bukkit.util.ConfigFile;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by Floris on 06-11-15.
 * Email: florisgra@gmail.com
 * <p>
 * Magic. Do not touch.
 */
public class DatabaseProvider {

    private static final ConnectionProvider connectionProvider;
    private static Thread thread;

    static {
        connectionProvider = Database.getInstance();
    }

    public static void connect(Iceball plugin) {
        Properties properties = getDatabaseConfiguration(plugin);
        String hostname = properties.getProperty("dataSource.serverName");
        Database.getInstance().init(properties);

        thread = new Thread(connectionProvider);
        plugin.getServer().getLogger().log(Level.INFO, "[Iceball] Connecting to " + hostname + "..");
        thread.start();
        synchronized (connectionProvider) {
            try {
                connectionProvider.wait();
                plugin.getServer().getLogger().info("[Iceball] Succesfully connected!");
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getScheduler().runTask(plugin, () -> {
                    plugin.getServer().getLogger().log(Level.WARNING, "An error occured while connecting to database: " + e.getMessage());
                    plugin.getServer().getLogger().log(Level.SEVERE, Iceball.pluginLog + "Disabling plugin!");
                    plugin.getPluginLoader().disablePlugin(plugin);
                });
            }
        }
    }

    public static void disconnect() {
        Database.getInstance().disconnect();
        if (thread != null){
            thread.interrupt();
        }
    }

    private static Properties getDatabaseConfiguration(Iceball plugin) {
        Config config = new Config(ConfigFile.config_yml, plugin);
        FileConfiguration file = YamlConfiguration.loadConfiguration(config.getFile());
        Properties prop = new Properties();
        String user = file.getString("Iceball.mysql.username");
        String port = file.getString("Iceball.mysql.port");
        String password = file.getString("Iceball.mysql.password");
        String database = file.getString("Iceball.mysql.database");
        String server = file.getString("Iceball.mysql.hostname");


        prop.setProperty("dataSource.user", user);
        prop.setProperty("dataSource.password", password);

        prop.setProperty("dataSource.serverName", server);
        prop.setProperty("dataSource.portNumber", port);

        prop.setProperty("dataSource.databaseName", database);

        prop.setProperty("dataSource.url", "jdbc:mysql://" + server + ":" + port + "/?user=" + user + "&password=" + password);
        prop.setProperty("dataSourceClassName", "com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        prop.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        prop.setProperty("dataSource.prepStmtCacheSize", "250");
        prop.setProperty("dataSource.cachePrepStmts", "true");
        return prop;
    }
}
