package net.iceball.mgfw.impl.game;

import com.google.common.base.Preconditions;
import net.iceball.mgfw.api.arena.MGTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Floris on 10-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class TeamManager {

    private final GameSettings settings;
    private HashMap<String, MGTeam> teams;
    private JavaPlugin plugin;
    private Scoreboard scoreboard;

    public TeamManager(JavaPlugin plugin, List<MGTeam> teams, GameSettings settings) {
        this.teams = new HashMap<>();
        for (MGTeam team : teams) {
            this.teams.put(team.getName(), team);
        }
        this.plugin = plugin;
        scoreboard = null;
        this.settings = settings;
        setup();
    }

    private void setup() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        teams.values().forEach(this::createTeam);
    }

    /**
     * Sets the player in a random team!
     *
     * @param player Name of player.
     */
    public void joinTeam(String player) {
        for (Team t : scoreboard.getTeams()) {
            if (t.getSize() <= teams.get(t.getName()).getMaximumPlayers()) {
                t.addEntry(player);
            }
        }
        Preconditions.checkArgument(false, "Player cannot join a single team! All teams are full!");
    }

    /**
     * Sets the player in a team!
     *
     * @param player Name of player.
     * @param team   Name of the team.
     */
    public void joinTeam(String player, String team) throws TeamIsFullException {
        if (isAlreadyInTeam(player)) {
            scoreboard.resetScores(player);
        }
        for (Team t : scoreboard.getTeams()) {
            if (t.getName().equals(team)) {
                if (t.getSize() <= teams.get(t.getName()).getMaximumPlayers()) {
                    t.addEntry(player);
                    return;
                }
            }
        }
        throw new TeamIsFullException("Team is full!");
    }

    public void finishTeams(Set<String> players){
        for (String player : players){
            if (isAlreadyInTeam(player)){
                return;
            }
            Player online_player = Bukkit.getPlayer(player);
            if (online_player != null) {
                joinTeam(player);
                online_player.setScoreboard(scoreboard);
            }
        }
    }

    public MGTeam getTeam(String player){
        String name = scoreboard.getEntryTeam(player).getName();
        MGTeam team = teams.get(name);
        if (team != null){
            return team;
        }
        return teams.values().iterator().next();
    }

    private boolean isAlreadyInTeam(String player) {
        return scoreboard.getEntryTeam(player) != null;
    }
    private void createTeam(MGTeam team) {
        String name = team.getName();
        String prefix = team.getPrefix().replaceAll("&", "§");
        String suffix = team.getSuffix().replaceAll("&", "§");
        scoreboard.registerNewTeam(name);

        scoreboard.getTeam(name)
                .setAllowFriendlyFire(team.allowFriendlyFire());
        scoreboard.getTeam(name)
                .setDisplayName(prefix + name);

        scoreboard.getTeam(name)
                .setCanSeeFriendlyInvisibles(settings.canSeeFriendlyInvisibles);
        scoreboard.getTeam(name)
                .setNameTagVisibility(NameTagVisibility.ALWAYS);

        scoreboard.getTeam(name)
                .setPrefix(prefix);
        scoreboard.getTeam(name)
                .setSuffix(suffix);
    }

}

class TeamIsFullException extends Exception {

    public TeamIsFullException() {
        super("Team is full!");
    }

    public TeamIsFullException(String message) {
        super(message);
    }

    public TeamIsFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamIsFullException(Throwable cause) {
        super(cause);
    }

    protected TeamIsFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
