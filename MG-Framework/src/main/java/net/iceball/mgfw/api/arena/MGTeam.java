package net.iceball.mgfw.api.arena;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * Created by Floris on 13-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public interface MGTeam extends ConfigurationSerializable {

    /**
     * Gets the name of the team.
     * @return name of team registered team
     */
    String getName();

    /**
     * Gets the spawntype of the team. It tells the game how it should use the spawns with players!
     * @return SpawnType of team.
     */
    SpawnType getSpawntype();

    /**
     * Gets all the registered spawns of the team.
     * @return an array of location with the spawns!
     */
    Location[] getSpawns();

    /**
     * Should the player respawn if he dies? When disabled, the player is automatically moved to the spectator team.
     * @return True means suddendeath, otherwise false.
     */
    boolean isSuddenDeath();

    /**
     * Whether the player is put in spectator mode after he dies.
     * @param suddendeath True to enable suddendeath, otherwise false.
     */
    void isSuddendeath(boolean suddendeath);

    /**
     * Whether all players spawn at the first set location at the game start.
     * @return True for mainstartspawn, otherwise false.
     */
    boolean isMainStartSpawn();

    /**
     * Sets the mainstartspawn!
     *
     * Set this to true if you want all your players to spawn at the first set location at the start of the game!
     * This very usefull when you have multiple spawnpoints, but don't want everyone to spawn in different locations when the game starts.
     * @param mainstartspawn False to disable mainstartspawn, true to enable mainstartspawn.
     */
    void isMainStartSpawn(boolean mainstartspawn);

    /**
     * Gets the prefix of the team. It is used in chat and scoreboards.
     * @return The prefix of the team.
     */
    String getPrefix();

    /**
     * Sets the prefix of the team.
     * @param prefix the new prefix that will be set.
     */
    void setPrefix(String prefix);

    /**
     * Gets the suffix of the team. It is used in chat and player-tags.
     * @return The suffix of the team.
     */
    String getSuffix();

    /**
     * Sets the suffix of the team.
     * @param suffix the new suffix that will be set.
     */
    void setSuffix(String suffix);

    /**
     * Gets the maximum amount of players this team can support.
     * @return The maximum amount players that this team can support.
     */
    int getMaximumPlayers();

    /**
     * Whether the players in this team can hit each other (PVP), or not (PVE).
     * @return True for friendlyfire (PVE), otherwise false (PVP).
     */
    boolean allowFriendlyFire();

    /**
     * Sets this team friendlyfire!
     *
     * Set this to true if you don't want all your players of this team be able to hit each other during the game.
     * By default enabled!
     * @param friendlyfire True to enable friendlyfire (PVE), otherwise false (PVP).
     */
    void setAllowFriendlyFire(boolean friendlyfire);

    /**
     * Adds the score of this team to the scoreboard!
     * @param score Score that will be added.
     */
    void addScore(int score);

    /**
     * Sets the score of this team scoreboard!
     * @param score Score that will be set.
     */
    void setScore(int score);
}