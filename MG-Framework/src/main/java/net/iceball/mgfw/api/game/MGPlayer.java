package net.iceball.mgfw.api.game;

import net.iceball.mgfw.api.arena.Arena;
import net.iceball.mgfw.api.arena.MGTeam;

/**
 * Created by Floris on 24-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public abstract class MGPlayer {

    final Game game;
    final MGTeam team;
    final String name;

    protected MGPlayer(String name, Game game, MGTeam team){
        this.name = name;
        this.game = game;
        this.team = team;
    }

    /**
     * Gets the game the player is registered in!
     * @return the game the player currently is in.
     */
    abstract Game getGame();

    /**
     * Gets the team the player is in!
     * @return the team.
     */
    abstract MGTeam getTeam();

    /**
     * Gets the arena the game is using!
     * @return the arena the player is using.
     */
    abstract Arena getArena();
}
