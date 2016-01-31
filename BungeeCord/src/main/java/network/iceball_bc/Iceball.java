package network.iceball_bc;

import net.md_5.bungee.api.plugin.Plugin;
import network.iceball_bc.database.Database;
import network.iceball_bc.util.Config;
import network.iceball_bc.database.ConnectionProvider;

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