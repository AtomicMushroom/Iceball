package net.iceball.mgfw.impl.game.api;

import net.iceball.mgfw.api.game.config.MGConfiguration;
import net.iceball.mgfw.api.game.events.*;
import net.iceball.mgfw.impl.game.GameManager;
import net.iceball.mgfw.api.game.config.Display;
import net.iceball.mgfw.impl.MinigameFramework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

/**
 * Created by Floris on 29-11-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public class Bomberman implements Listener {

    private MinigameFramework plugin;

    public Bomberman(MinigameFramework plugin){
        plugin.getLogger().log(Level.FINE, "Bomberman started!");
        this.plugin = plugin;
        MGConfiguration config = new MGConfiguration("Bomberman");
        config.setCountdown(30);
        config.setJoinTeamInstantly(false);
        config.setMinimumPlayers(3);

        Display display = new Display();
        display.setDisplayType(Display.DisplayType.PLAYER);
        display.setScoreValue(Display.ScoreValue.CUSTOM);
        config.setDisplay(display);
        config.setLeaveInterval(20);
        new GameManager(config.build(), plugin).start();

    }

    @EventHandler
    public void onSetup(GameSetupEvent event) {
        plugin.getLogger().info("GameSetupEvent");
    }

    @EventHandler
    public void onStart(GameStartEvent event) {
        plugin.getLogger().info("GameStartEvent");
    }

    @EventHandler
    public void onEnd(GameEndEvent event) {
        plugin.getLogger().info("GameEndEvent");
    }

    @EventHandler
    public void onJoin(GameJoinEvent event) {
        plugin.getLogger().info("GameJoinEvent");
    }

    @EventHandler
    public void onLeave(GameLeaveEvent event) {
        plugin.getLogger().info("GameLeaveEvent");
    }
}

