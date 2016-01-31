package network.iceball.bukkit.scoreboard;

import com.google.common.base.Preconditions;
import network.iceball.bukkit.Iceball;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Map;

/**
 * Created by Floris on 03-07-15.
 */
public class IScoreboard {
    public Iceball plugin;

    final String[] text;

    public IScoreboard(Iceball plugin, final int maxLength, Map<String, Integer> build) {
        this.plugin = plugin;
        Preconditions.checkArgument(maxLength < 15, "scoreboard cannot be longer than 15 lines");
        text = new String[maxLength];
    }

    public void updateText(int index, String text){
        Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
        Preconditions.checkArgument(index < (text.length()-1), "index cannot be longer than the maximum scoreboard length");
    }
    public void update(){

    }





    public void sendScoreboard(Player[] players){

    }



    ScoreboardManager manager = Bukkit.getScoreboardManager();
    Scoreboard sb = manager.getNewScoreboard();
    Objective objective = sb.registerNewObjective("test", "dummy");
    /*MAKE A super scoreboard
     * Displays the following:
     * see notes and: https://bukkit.org/threads/tutorial-scoreboards-teams-with-the-bukkit-api.139655/
     */

}
