package network.iceball;

import net.md_5.bungee.api.plugin.Plugin;
import network.iceball.database.ConnectionProvider;
import network.iceball.database.Database;
import network.iceball.util.Config;

public final class Iceball extends Plugin {

    @Override
    public void onEnable() {
        Config config = new Config(this);

        ConnectionProvider connectionProvider = Database.getInstance();
        ConnectionProvider.activate(connectionProvider, this, config.getDatabaseConfiguration());

        // getLogger().info(" AA DSADSAD ");
        new EventListener(this);
        new CommandManager(this);
    }

}