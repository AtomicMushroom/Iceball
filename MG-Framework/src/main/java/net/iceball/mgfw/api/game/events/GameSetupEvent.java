package net.iceball.mgfw.api.game.events;

import net.iceball.mgfw.api.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Floris on 15-09-15.
 */
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  GameSetupEvent is called once the arena is   //
// setting up his things..                       //
///////////////////////////////////////////////////
public final class GameSetupEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Game game;

    public GameSetupEvent(Game game) {
        this.game = game;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Game getGame() {
        return game;
    }
}