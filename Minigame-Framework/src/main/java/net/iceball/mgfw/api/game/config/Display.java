package net.iceball.mgfw.api.game.config;

/**
 * Created by Floris on 14-09-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class saves information about the       //
//  scoreboard. The "real" scoreboard is in      //
//  ScoreboardManager.java                       //
///////////////////////////////////////////////////
public class Display {

    private boolean remove_key_when_score_zero;
    private ScoreValue scoreValue;
    private DisplayType displayType;

    public Display(){
        remove_key_when_score_zero = false;
        scoreValue = ScoreValue.AMOUNTOFPLAYERS;
        displayType = DisplayType.PLAYER;
    }

    public boolean removeKeyWhenScoreZero() {
        return remove_key_when_score_zero;
    }

    public ScoreValue getScoreValue() {
        return scoreValue;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    /**
     * When true, it will remove the team when the score is zero.
     * @param removeKeyWhenScoreZero I recommend yes when you have many teams or have a FFA-type like game.
     */
    public void removeKeyWhenScoreZero(boolean removeKeyWhenScoreZero) {
        remove_key_when_score_zero = removeKeyWhenScoreZero;
    }

    /**
     * What will the score be for the teams/players? {@link Display.ScoreValue#AMOUNTOFPLAYERS} ?
     * or {@link Display.ScoreValue#CUSTOM}
     * @param scoreValue
     */
    public void setScoreValue(ScoreValue scoreValue) {
        this.scoreValue = scoreValue;
    }

    /**
     * What the scoreboard will display or if you don't want it to display anything at all.
     * EMPTY: No scoreboard.
     * PLAYER: All (good) players have their own place on the scoreboard.
     * TEAM: All teams are shown.
     * @param displayType What should it text display?
     */
    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    /**
     * AMOUNTOFPLAYERS: the title says it already.
     * CUSTOM: it is up to you.
     */
    public enum ScoreValue {
        AMOUNTOFPLAYERS, CUSTOM;
    }

    /**
     * EMPTY: No scoreboard.
     * PLAYER: All (good) players have their own place on the scoreboard.
     * TEAM: All teams are shown.
     */
    public enum DisplayType {
        EMPTY,
        PLAYER,
        TEAM
    }
}
