package network.iceball.bukkit.scoreboard;

/**
 * Created by somebody, idk who. on 09-04-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.istack.internal.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScoreboardAPI {
    private Scoreboard scoreboard;
    private String title;
    private Map<String, Integer> scores;
    private List<Team> teams;

    public ScoreboardAPI(String title)
    {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.title = title;
        this.scores = Maps.newLinkedHashMap();
        this.teams = Lists.newArrayList();
    }

    public void blankLine()
    {
        add(" ");
    }

    public void add(String text)
    {
        add(text, null);
    }

    public void add(String text, @Nullable Integer score)
    {
        Preconditions.checkArgument(text.length() < 48, "text cannot be over 48 characters in length");
        text = fixDuplicates(text);
        this.scores.put(text, score);
    }

    private String fixDuplicates(String text)
    {
        while (this.scores.containsKey(text))
            text = text + "§r";
        if (text.length() > 48)
            text = text.substring(0, 47);
        return text;
    }

    private Map.Entry<Team, String> createTeam(String text) {
        String result = "";
        if (text.length() <= 16)
            return new AbstractMap.SimpleEntry(null, text);
        Team team = this.scoreboard.registerNewTeam("text-" + this.scoreboard.getTeams().size());
        Iterator iterator = Splitter.fixedLength(16).split(text).iterator();
        team.setPrefix((String) iterator.next());
        result = (String) iterator.next();
        if (text.length() > 32)
            team.setSuffix((String) iterator.next());
        this.teams.add(team);
        return new AbstractMap.SimpleEntry(team, result);
    }

    public void build()
    {
        Objective obj = this.scoreboard.registerNewObjective(this.title.length() > 16 ? this.title.substring(0, 15) : this.title, "dummy");
        obj.setDisplayName(this.title);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int index = this.scores.size();

        for (Map.Entry text : this.scores.entrySet())
        {
            Map.Entry team = createTeam((String)text.getKey());
            Integer score = text.getValue() != null ? (Integer) text.getValue() : index;
            OfflinePlayer player = new Message((String)team.getValue());
            if (team.getKey() != null)
                ((Team)team.getKey()).addPlayer(player);
            obj.getScore(player).setScore(score);
            index--;
        }
    }

    public void reset()
    {
        this.title = null;
        this.scores.clear();
        this.teams.forEach(org.bukkit.scoreboard.Team::unregister);
        this.teams.clear();
    }

    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    public void send(Player[] players)
    {
        for (Player p : players)
            p.setScoreboard(this.scoreboard);
    }

    public void debug(){
        for (Team t : teams){
            System.out.println("Name: " + t.getName() + " |  prefix: " + t.getPrefix() + "| suffix: " + t.getSuffix());
            System.out.println("Displayname: " + t.getDisplayName() + " | " + t.getEntries() );
        }
    }

    public static void resetScoreboard(Player p)
    {
        ScoreboardAPI scoreboard = new ScoreboardAPI("");
        scoreboard.build();
        scoreboard.send(new Player[] { p });
        scoreboard.reset();
    }
}