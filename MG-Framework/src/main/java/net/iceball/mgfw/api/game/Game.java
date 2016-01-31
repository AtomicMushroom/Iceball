package net.iceball.mgfw.api.game;

import net.iceball.mgfw.api.arena.Arena;
import net.iceball.mgfw.api.game.GameStatus;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Floris on 16-12-15.
 *
 * This class is the one and only Game.java! Everything that matters is already settled in this class.
 *
 * Only use the methods if there should happen something that doesn't happen!
 */
public interface Game {

    /**
     * Force-starts the game!
     */
    void start();

    /**
     * Force-ends the game! All players will get their experience and will be sent back to the lobby!
     */
    void end();

    /**
     * Adds the player to the game, assuming that this is the first time that they join the game.
     *
     * <b>Note: </b> this method only makes sense if the game is still waiting for other players!
     *
     * The player will be moved to the spectator team when he is not onlineand a message will be shown that the
     * @see GameStatus
     * @param name Name of player.
     * @param uuid UUID of player.
     */
    void join(String name, UUID uuid);

    /**
     * Removes the player from the game. If the player is still playing he will be automatically sent back to the lobby!
     * @param name The name of the player.
     */
    void leave(String name);

    /**
     * Gets the arena where the game is playing in.
     * @return The arena which the game is using.
     */
    Arena getArena();

    /**
     * Gets all players UUID's registered in the game.
     * @return All registered UUIDs of the players in the game.
     */
    Set<UUID> getPlayers();

    /**
     * Gets all the online players of this game!
     * @return A set of names of the online players of the game!
     */
    Set<String> getOnlinePlayers();

    /** Send a message to all players in the arena. */
    /**
     * Sends an message to all players who are online and in the game.
     * @param message the text that will be sent.
     */
    void broadcast(String message);
}
