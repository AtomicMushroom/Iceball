package network.iceball.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import network.iceball.Iceball;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * Created by Floris on 20-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class Database implements ConnectionProvider {

    private static final Database INSTANCE;
    private final HikariDataSource[] dataSource = new HikariDataSource[1];
    private Boolean isInitialized;

    private HikariConfig config;
    private Properties properties;
    private Iceball plugin; //Warning is Async

    private String database;
    private String username;
    private String password;
    private String hostname;
    private String port;


    static //class initializer:
    {
        INSTANCE = new Database();
    }

    private Database() {
        dataSource[0] = new HikariDataSource();
        isInitialized = false;
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void init(Properties properties, Iceball plugin) {
        this.plugin = plugin;
        this.properties = properties;
        database = properties.getProperty("dataSource.databaseName");
        username = properties.getProperty("dataSource.user");
        password = properties.getProperty("dataSource.password");
        hostname = properties.getProperty("dataSource.serverName");
        port = properties.getProperty("dataSource.portNumber");
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (!isInitialized)
            throw new UnsupportedOperationException("Not initialized.");
        return dataSource[0].getConnection();
    }

    @Override
    public boolean isInitialised() {
        return isInitialized;
    }

    @Override
    public void run() {
        initialise();
        isInitialized = true;
        synchronized (this) {
            notify();
        }
    }

    public void initialise() {
        String isEnabled = "enabled";
        boolean connect = Boolean.valueOf(properties.getProperty(isEnabled));
        properties.remove(isEnabled); //HikariCP is going to cry when I give him things he doesn't need.

        if (connect) {
            config = new HikariConfig(properties);
            plugin.getLogger().info("Connecting to " + properties.getProperty("dataSource.serverName") + "..");
            dataSource[0] = new HikariDataSource(config);
            loadDatabase();
        } else {
            plugin.getLogger().log(Level.WARNING, "Database has been disabled, you can enable it in the config.");
            for (Handler handler : plugin.getLogger().getHandlers()) {
                handler.close();
            }
            plugin.getProxy().getPluginManager().unregisterListeners(plugin);
            plugin.getExecutorService().shutdown();
        }
    }

    public void loadDatabase() {
        try {
            setupDatabase();
            setupPlayerDataTable();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    private void setupDatabase() throws SQLException {
        String cmd = "CREATE DATABASE IF NOT EXISTS " + database + ";";
        plugin.getLogger().log(Level.FINEST, "Success! 5");
        Connection connection = dataSource[0].getConnection();
        Statement st = connection.createStatement();
        st.execute(cmd);
        st.close();
        connection.close();
        plugin.getLogger().log(Level.FINEST, "Success! 6");

        HikariConfig hikariConfig = config;
        hikariConfig.setCatalog(database);
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?user=" + username + "&password=" + password + "&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
        hikariConfig.setJdbcUrl(jdbcUrl);
        dataSource[0].close();
        dataSource[0] = null;
        plugin.getLogger().log(Level.FINEST, "Success! 7");
        dataSource[0] = new HikariDataSource(hikariConfig);
    }


    private void setupPlayerDataTable() throws SQLException {
        Statement st;
        String cmd = "CREATE TABLE IF NOT EXISTS playerdata"
                + "  (id INT NOT NULL AUTO_INCREMENT,"
                + "   uuid VARCHAR(36) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + "   player VARCHAR(16) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + "   ip INT UNSIGNED DEFAULT NULL COLLATE utf8_general_ci,"
                + "   coins INT DEFAULT 0,"
                + "   networkbooster TINYINT UNSIGNED DEFAULT 1,"
                + "   level TINYINT UNSIGNED DEFAULT 1,"
                + "   karma TINYINT UNSIGNED DEFAULT 0,"
                + "   permissions TEXT CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,"
                + "   rank VARCHAR(4) DEFAULT 'NRML',"
                + "   chosen_map VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_general_ci,"
                + "   joined_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "   last_seen TIMESTAMP,"
                + "   PRIMARY KEY(id),"
                + "   UNIQUE KEY(uuid),"
                + "   UNIQUE KEY(player));";
        Connection connection = dataSource[0].getConnection();
        st = connection.createStatement();
        st.execute(cmd);
        st.close();
        connection.close();
    }
}