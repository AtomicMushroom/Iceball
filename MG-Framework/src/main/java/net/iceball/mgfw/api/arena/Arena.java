package net.iceball.mgfw.api.arena;

import com.sun.istack.internal.Nullable;
import net.iceball.mgfw.api.game.GameStatus;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

/**
 * Created by Floris on 13-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public interface Arena {

    /**
     * Gets the location where the player will spawn once he or she is declared as dead.
     *
     * @return Location when the player is dead, or has no lifes left (depends).
     */
    Location getSpectator();

    /**
     * Gets the location where the player will spawn if the game is not started, yet.
     *
     * @return the location where the players will be teleported when the game is still waiting for other players.
     */
    Location getLobby();

    /**
     * Gets the specific team by name from the list of the arena.
     *
     * @param name The name of the team.
     * @return Team instance, otherwise null.
     */
    @Nullable
    MGTeam getTeam(String name);

    /**
     * Gets all the teams of the arena.
     *
     * @return List of all Teams
     */
    List<MGTeam> getTeams();

    /**
     * Gets the maximum amount of players that can be in the arena.
     * It is calculated based of all the teams maximum players together.
     *
     * @return The maximum players that can be in the teams.
     */
    int getMaxPlayers();

    /**
     * Sets the location where the players will spawn when the game is not started, yet.
     *
     * @param location the location of the lobby
     */
    void setLobby(Location location);

    /**
     * Sets the location where the player will spawn if he or she died.
     * <b>Note: </b> changes are not saved if the arena is not in edit mode or if there is a game running.
     *
     * @param location the location of the spectator
     */
    void setSpectator(Location location);

    /**
     * Gets the status of the arena. Whether the arena is being used or not. In if it is in use, then
     * how is the game looking?
     *
     * @return status of the arena
     */
    GameStatus getStatus();

    /**
     * Sets the new status of the arena. Do not call this method to see what it does!
     *
     */
    void setStatus(GameStatus status);

    /**
     * Gets the list of custom blocks that were set in the arena.
     * It is up to the developer to say what type of use the blocks will get.
     *
     * @return a list of all the blocks!
     */
    List<Block> getBlocks();

    /**
     * Gets the name of the arena!
     *
     * @return the name of the arena
     */
    String getName();

    /**
     * Whether the arena is complete and ready to be saved or is not complete and not ready to be saved.
     * @return true when the arena is ready to be saved, otherwise false.
     */
    boolean isComplete();
}
