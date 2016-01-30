package net.iceball.mgfw.api.arena;

/**
 * Created by Floris on 20-08-15.
 *
 * Every game has a different spawn mechanism. Sometimes you want that have every player has his own spawnpoint.
 * Or what about a game where there are multiple teams and you want to use ,
 */
public enum SpawnType {

    /**
     * This spawntype means that every player of this team has his own custom spawn location. An example: SurvivalGames.
     */
    PLAYER,

    /**
     * This spawntype means that each player from this team spawns at one of the teams set spawnpoint(s). An example: Paintball
     */
    TEAM,

    /**
     * This spawntype means each player of the team spawns at the public spawnpoint(s).
     * An example: TNTRun.
     */
    PUBLIC
}
