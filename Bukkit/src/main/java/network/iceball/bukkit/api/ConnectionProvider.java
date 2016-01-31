package network.iceball.bukkit.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Floris on 19-10-15. */
public interface ConnectionProvider extends Runnable {

    void init(Properties properties);
    Connection getConnection() throws SQLException;
    void disconnect();
}

