package network.iceball.database;

import network.iceball.Iceball;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Floris on 19-10-15. */
public interface ConnectionProvider extends Runnable {
    void init(Properties properties, Iceball plugin);
    Connection getConnection() throws SQLException;
    boolean isInitialised();

    static void activate(final ConnectionProvider connectionProvider, final Iceball plugin, Properties config){
        connectionProvider.init(config, plugin);
        plugin.getProxy().getScheduler().runAsync(plugin, connectionProvider);

        synchronized (connectionProvider) {
            while (!connectionProvider.isInitialised()) {
                try {
                    connectionProvider.wait();
                    plugin.getLogger().info("Success!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}