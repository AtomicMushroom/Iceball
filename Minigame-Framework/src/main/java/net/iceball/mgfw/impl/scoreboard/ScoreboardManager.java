package net.iceball.mgfw.impl.scoreboard;

import net.iceball.mgfw.api.game.config.Display;
import net.iceball.mgfw.api.game.Game;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Floris on 15-09-15.
 */
public class ScoreboardManager extends BukkitRunnable {

    private final JavaPlugin plugin;
    private net.iceball.mgfw.api.game.config.Display Display;
    private Game game;
    private int counter;

    public ScoreboardManager(Display display, Game game, JavaPlugin plugin) {
        this.plugin = plugin;
        this.Display = Display;
        this.game = game;
        //game.getArena().getTeams().get(1);

    }

    @Override
    public void run() {

    }
}
