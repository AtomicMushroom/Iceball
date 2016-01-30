package net.iceball.mgfw.impl.game;

import net.iceball.mgfw.api.game.config.MGTeamsModifier;
import net.iceball.mgfw.api.game.config.Display;
import net.iceball.mgfw.api.game.config.Locations;
import net.iceball.mgfw.api.game.config.MGConfiguration;

import java.util.List;
import java.util.function.UnaryOperator;

/*
 * Created by Floris on 14-09-15.
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class saves the gamerules as object     //
//  GameSettings. I have MGConfiguration as    //
//  parameter. I did this, because I don't want  //
//  any freaks changing my config at runtime     //
////////////////////////////////////////////////**/
class GameSettings {

    /* Name of the game. */
    public final String name;

    /* The amount of seconds the game will count, once zero. Will start the game. Default: 30 */
    public final int countdown;

    /* The amount of players which are required to start the countdown to start the game. Default: 1 */
    public final int minimumPlayers;

    /* Interval in seconds you can leave and join back in before getting removed from the game.
       Zero means no interval. Default is 300 seconds. (5 Minutes) */
    public final int leaveInterval;

    /* Singlemode: one game in at a time. - Multimode: multiple games in multiple arenas. */
    public final boolean multimode;

    /* Whether the player is automatically placed in a team if the game is started. */
    public final boolean joinTeamInstantly;

    /* The look of the scoreboard. */
    public final Display display;

    /* Whether the other dead players are visible to the others of the team. Default: false */
    public final boolean canSeeFriendlyInvisibles;

    /* Whether the player is automatically teleported to the lobby once joined or not. Default: true */
    public final Locations autoTeleportTo;

    public final List<UnaryOperator<MGTeamsModifier>> team_actions;

    public GameSettings(final MGConfiguration config) {
        name = config.getName();

        if (config.getLeaveInterval() > 300) {
            leaveInterval = 300;
        } else {
            if (config.getLeaveInterval() < 0) {
                leaveInterval = 0;
            } else {
                leaveInterval = config.getLeaveInterval();
            }
        }
        autoTeleportTo = config.autoTeleportTo();
        multimode = config.isMultiMode();
        display = config.getDisplay();
        minimumPlayers = config.getMinimumPlayers();
        countdown = config.getCountdown();
        joinTeamInstantly = autoTeleportTo.equals(Locations.SPAWN) || config.getJoinTeamInstantly();
        canSeeFriendlyInvisibles = config.canSeeFriendlyInvisibles();
        team_actions = config.getTeamOperations();
    }
}
