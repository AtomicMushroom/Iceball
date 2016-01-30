package net.iceball.mgfw.impl;

import net.iceball.mgfw.impl.arena.ArenaLoader;
import net.iceball.mgfw.impl.util.BaseCommand;

/**
 * Created by Floris on 21-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
class CommandManager {

    private MinigameFramework plugin;

    public CommandManager(MinigameFramework plugin) {
        this.plugin = plugin;
        for (BaseCommand command : ArenaLoader.getLoader().getCommands(plugin)){
            plugin.getCommand(command.getName()).setExecutor(command);
        }
    }
}
