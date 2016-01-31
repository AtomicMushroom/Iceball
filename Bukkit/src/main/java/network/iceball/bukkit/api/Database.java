package network.iceball.bukkit.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Floris on 19-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class Database implements ConnectionProvider {

    private static ConnectionProvider INSTANCE;
    private final HikariDataSource[] dataSource = new HikariDataSource[1];
    private static boolean isInitialized;

    private HikariConfig config;

    public String database;
    public String username;
    public String password;
    public String hostname;
    public String port;

    private Database() {
        dataSource[0] = new HikariDataSource();
        isInitialized = false;
    }

    public static ConnectionProvider getInstance(){
        if (INSTANCE == null){
            synchronized (Database.class){
                if (INSTANCE == null){
                    INSTANCE = new Database();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public synchronized void init(Properties properties) {
        if (!isInitialized)
        config = new HikariConfig(properties);
        this.username = properties.getProperty("dataSource.user");
        this.password = properties.getProperty("dataSource.password");
        this.hostname = properties.getProperty("dataSource.serverName");
        this.port =     properties.getProperty("dataSource.portNumber");
        this.database = properties.getProperty("dataSource.databaseName");
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        if (!isInitialized)
            throw new UnsupportedOperationException("Not initialized.");
        return dataSource[0].getConnection();
    }

    @Override
    public void run() {
        dataSource[0] = new HikariDataSource(config);
        try {
            String cmd = "CREATE DATABASE IF NOT EXISTS " + database + ";";
            Connection connection = dataSource[0].getConnection();
            Statement st = connection.createStatement();
            st.execute(cmd);
            st.close();
            connection.close();
        } catch (SQLException e) {
            Thread t = Thread.currentThread();
            t.getUncaughtExceptionHandler().uncaughtException(t, e);
        }
        HikariConfig hikariConfig = config;
        String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?user=" + username + "&password=" +
                password + "&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
        hikariConfig.setCatalog(database);
        hikariConfig.setJdbcUrl(jdbcUrl);
        dataSource[0].close();
        dataSource[0] = null;
        dataSource[0] = new HikariDataSource(hikariConfig);
        isInitialized = true;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void disconnect() {
        dataSource[0].close();
    }
}
