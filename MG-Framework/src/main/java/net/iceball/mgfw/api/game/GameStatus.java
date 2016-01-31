package net.iceball.mgfw.api.game;

import net.iceball.mgfw.impl.game.GameManager;

/**
 * Created by Floris on 07-08-15.
 *
 * Tells us the status of arena. Perhaps it is in use? Or maybe is somebody editing it? You never know.
 * */
public enum GameStatus{

    /**
     * Cannot be used, the arena is not complete!
     */
    OFF,

    /**
     * All arenas that are loaded from config will have this as default value.
       @see GameManager#loadWorkingArenas() grabs all the arenas with the SETUP value, and will start the game!
     */
    SETUP,

    /**
     * Game is started, the arena is still waiting for people to join.
     */
    WAITING,

    /**
     * This one is too obvious. The game is started!
     */
    STARTED,

    /**
     * The game is ended, save the stuff and restart :D
     */
    ENDED
}
