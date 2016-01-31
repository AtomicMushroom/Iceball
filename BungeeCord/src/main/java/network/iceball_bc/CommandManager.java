package network.iceball_bc;

import network.iceball_bc.commands.PMCommand;
import network.iceball_bc.commands.PluginCommandListener;
import network.iceball_bc.commands.LeaveCommand;

/**
 * Created by Floris on 20-10-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public class CommandManager {

    private Iceball plugin;

    public CommandManager(Iceball plugin){
        this.plugin = plugin;
        new PluginCommandListener(plugin);
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, new LeaveCommand());
        this.plugin.getProxy().getPluginManager().registerCommand(this.plugin, new PMCommand());
    }
}
