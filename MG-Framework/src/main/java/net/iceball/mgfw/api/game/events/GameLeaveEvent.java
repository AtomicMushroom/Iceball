package net.iceball.mgfw.api.game.events;

import net.iceball.mgfw.api.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Floris on 10-08-15.
 */
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  Difference between PlayerJoinEvent:          //
//  1. Only starts when joining an arena.        //
//  2. Also contains Game object, which you can  //
//  do cool things with!                         //
///////////////////////////////////////////////////
public final class GameLeaveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Game game;
    private boolean cancelled;

    public GameLeaveEvent(Player player, Game game) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Game getGame(){ return game; }
}
