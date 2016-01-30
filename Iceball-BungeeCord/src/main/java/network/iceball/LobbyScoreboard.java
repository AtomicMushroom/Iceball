package network.iceball;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.api.score.Team;

import java.util.List;
import java.util.Map;

/**
 * Created by Floris on 15-10-15.
 */
class LobbyScoreboard {
    private Scoreboard scoreboard;
    private String title;
    private Map<String, Integer> scores;
    private List<Team> teams;

    public LobbyScoreboard(String title){
        scores = Maps.newLinkedHashMap();
        scoreboard = new Scoreboard();
        //objective = new Objective("lobby", "dummy", "ICEBALL");

        scores.put("ICEBALL", null);
        scores.put(" ", null);
        scores.put("Online: (81/202)", null);
        scores.put("Rank: - ", null);
        scores.put(" ", null);
        scores.put("IceCrowns: 1342", null);
        scores.put("Server: lobby1", null);
        scores.put(" ", null);
        scores.put("Website: ", null);
        scores.put("iceball-gaming.net", null);
        //max 16 lines
    }
    public void addPlayer(String name){
        //team.addPlayer(name);
    }
    public void removePlayer(String name){
        //team.removePlayer(name);
    }

}
