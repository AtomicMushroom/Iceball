package net.iceball.mgfw.api.game.config;

import net.iceball.mgfw.impl.game.ConfigBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * Created by Floris on 14-09-15.
 *
 * Every game is different!
 * This class offers all the options to give as much flexibility to make your game even better!
 *
 * You don't have to set all values, because all values already have a default value.
 */
public class MGConfiguration extends ConfigBuilder {

    /* Name of the game. */
    private final String name;

    /* The amount of seconds the game will count, once zero. Will start the game. Default: 30 */
    private int countdown = 30;

    /* The amount of players which are required to start the countdown to start the game. Default: 1 */
    private int minimumPlayers = 1;

    /* Interval in seconds you can leave and join back in before getting removed from the game.
       Zero means no interval. Default is 300 seconds. (5 Minutes) */
    private int leaveInterval = 300;

    /* Singlemode: one game in at a time. - Multimode: multiple games in multiple arenas. */
    private boolean multimode = true;

    /* Whether the player is automatically placed in a team if the game is started. */
    private boolean joinTeamInstantly = true;

    /* The look of the scoreboard. */
    private Display display = new Display();

    /* Whether the other dead players are visible to the others of the team. Default: false */
    private boolean canSeeFriendlyInvisibles = false;

    /* Whether the player is automatically teleported to the lobby once joined or not. Default: true */
    private Locations autoTeleportTo = Locations.LOBBY;

    /* All new settings that have to happen to the teams!  */
    private List<UnaryOperator<MGTeamsModifier>> team_actions = new ArrayList<>();

    /**
     * Constructor to create your own minigame configuration!
     *
     * @param name The name of the game.
     */
    public MGConfiguration(final String name) {
        this.name = name;
    }

    /**
     * Gets the name of the game.
     * @return the name of the game.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the interval to join and leave back in. Zero means no interval. Default is 300 seconds.
     *
     * @param seconds Amount of seconds you can leave and join back in before getting removed from the game.
     */
    public void setLeaveInterval(int seconds) {
        this.leaveInterval = seconds;
    }

    /**
     * Gets the interval to leave and join back in without getting removed.
     * @return Amount of seconds.
     */
    public int getLeaveInterval() {
        return leaveInterval;
    }

    /**
     * Whether the GameManager is allowed to run multiple games at a time or not. If you want to restart the server
     * when the game is finished, you should set this to false.
     *
     * @param multimode True for multiple games, otherwise false.
     */
    public void setMultiMode(boolean multimode) {
        this.multimode = multimode;
    }

    /**
     * Whether there will be multiple games be runned at once or not.
     *
     * @return True for multiple games, otherwise false.
     */
    public boolean isMultiMode() {
        return multimode;
    }

    /**
     * Determines how the scoreboard will look. More information in the Display class.
     *
     * @param display How the scoreboard will look.
     */
    public void setDisplay(Display display) {
        this.display = display;
    }

    /**
     * Determines how the scoreboard will look. More information in the Display class.
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * The amount of players which are required to start the countdown and start the game.
     *
     * @param players The amount of players required to start the game.
     */
    public void setMinimumPlayers(int players) {
        this.minimumPlayers = players;
    }


    /**
     * The amount of players which are required to start the countdown and later start the game.
     *
     * @return The amount of players required to start the game.
     */
    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    /**
     * The amount of seconds the game will count for you, once zero. Will start the game.
     *
     * @param seconds Amount of seconds that you will have to wait before the game starts.
     */
    public void setCountdown(int seconds) {
        this.countdown = seconds;
    }

    /**
     * Gets the amount of seconds the game will count for you, once zero. Will start the game.
     *
     * @return Amount of seconds that you will have to wait before the game starts.
     */
    public int getCountdown() {
        return countdown;
    }

    /**
     * Whether the other dead players are visible to the others of the team. Default: false
     *
     * @param canSeeFriendlyInvisibles True to allow the team players to see others, otherwise false.
     */
    public void setCanSeeFriendlyInvisibles(boolean canSeeFriendlyInvisibles) {
        this.canSeeFriendlyInvisibles = canSeeFriendlyInvisibles;
    }

    /**
     * Whether the other dead players are visible to the others of the team.
     *
     * @return True means you will see the other friendly invisibles, otherwise false.
     */
    public boolean canSeeFriendlyInvisibles() {
        return canSeeFriendlyInvisibles;
    }

    /**
     * Sets the place where the player automatically will be teleported to once he joined the game.
     * Note: If you set this place to spawn, automatic team join will also be enabled. Default: Lobby
     *
     * @param autoTeleportTo the location
     * @see Locations#LOBBY
     */
    public void setAutoTeleportTo(Locations autoTeleportTo) {
        this.autoTeleportTo = autoTeleportTo;
    }

    /**
     * Gets the place where the player automatically will be teleported to once he joined the game.
     *
     * @return the type location
     */
    public Locations autoTeleportTo() {
        return autoTeleportTo;
    }

    /**
     * Whether the player instantly joins a random team when he joined the game.
     * @param joinTeamInstantly True to enable, otherwise false.
     */
    public void setJoinTeamInstantly(boolean joinTeamInstantly) {
        this.joinTeamInstantly = joinTeamInstantly;
    }

    /**
     * Gets whether the player instantly joins a team or not.
     * @return True is means it is enabled, otherwise false.
     */
    public boolean getJoinTeamInstantly() {
        return joinTeamInstantly;
    }

    /**
     * Sets specific options for all teams!
     * For example if you wanted to disable friendly-fire you would do: forAllTeams(MGTeamsModifier::disableFriendlyFire);
     * @param operator The action (property) you want to set to all of the teams.
     */
    public void forAllTeams(UnaryOperator<MGTeamsModifier> operator) {
        team_actions.add(operator);
    }

    /**
     * Gets all actions which still have to be set for the teams.
     * @return list of operations that still have to be done for the teams.
     */
    public List<UnaryOperator<MGTeamsModifier>> getTeamOperations(){
        return team_actions;
    }

    @Override
    public MGConfiguration getConfiguration() {
        return this;
    }

}

