package network.iceball.bukkit.events;

import network.iceball.bukkit.permissions.PPacket;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.scoreboard.ScoreboardAPI;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.events.gui.GUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Random;

/**
 * Created by Floris on 01-07-15.
 */
public final class JoinEvent implements Listener {

    private Iceball plugin;

    private String p = ChatColor.AQUA + "" + ChatColor.BOLD;
    private String s2 = ChatColor.GOLD + "" + ChatColor.BOLD;
    private String s1 = ChatColor.WHITE+ "" + ChatColor.BOLD;

    private String[] scrollName = {
            p + "ICEBALL", //default
                               s2 + "I" + s1 + "C" + p + "EBALL", //1
                     s1 + "I" + s2 + "C" + s1 + "E" + p + "BALL", //2 10
            p + "I" + s1 + "C" + s2 + "E" + s1 + "B" + p + "ALL", //3
            p + "IC" + s1 + "E" + s2 + "B" + s1 + "A" + p + "LL", //4
            p + "ICE" + s1 + "B" + s2 + "A" + s1 + "L" + p + "L", //5
            p + "ICEB" + s1 + "A" + s2 + "L" + s1 + "L",          //6
            p + "ICEBA" + s1 + "L" + s2 + "L",                    //7
            p + "ICEBAL" + s2 + "L",                              //8 18
    };
    static int i = 0;
    private String getNext(){
        i++;
        if (i >= 10){
            if (i == 18){
                i = 0;
            }
            return scrollName[i-8];
        }
        return scrollName[0];
    }


    private ScoreboardAPI sb;

    public JoinEvent(Iceball plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        sb = new ScoreboardAPI(ChatColor.AQUA + "" + ChatColor.BOLD +  "ICEBALL");
        sb.blankLine();
        sb.add(ChatColor.RED + "" + "Online: " + ChatColor.DARK_RED + "73");
        sb.blankLine();
        sb.add(ChatColor.GOLD + "Rank: " + ChatColor.DARK_GRAY + "-");
        sb.add(ChatColor.GREEN + "Coins: " + ChatColor.YELLOW + "40K");
        sb.blankLine();
        sb.add(ChatColor.LIGHT_PURPLE+ "Karma: " + ChatColor.DARK_PURPLE+ "50/550");
        sb.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.AQUA + "2");
        sb.blankLine();
        sb.add(ChatColor.GRAY + "Lobby: " + ChatColor.WHITE + "#2");
        sb.blankLine();
        sb.add(ChatColor.BLUE + "Website:");
        sb.add(ChatColor.AQUA + ""  + "iceball.network");
        sb.add(ChatColor.BLUE + "-------------");
        sb.build();

    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        Permissions.getInstance().add(PPacket.getPacket(event.getPlayer().getName()));

        player.setScoreboard(sb.getScoreboard());
        sb.debug();

        //test(player);
        if (EventManager.isLobby){
            new GUI(this.plugin, player);
        }
        // every 12 seconds, the scoreboard flickkers
    }

    private void test(Player player){
        final Random RAND = new Random();
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        final Objective obj = board.registerNewObjective("test", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        final Team team  = board.registerNewTeam("1");
        final Team team2 = board.registerNewTeam("2");
        final Team team3 = board.registerNewTeam("3");
        final Team team4 = board.registerNewTeam("4");
        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.RED.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));

        obj.getScore(ChatColor.RED.toString()).setScore(1);
        obj.getScore(ChatColor.BLUE.toString()).setScore(2);
        obj.getScoreboard().getEntryTeam("3").setPrefix("kaas");
        obj.getScore("3").setScore(12);
        new BukkitRunnable() {
            ChatColor next = ChatColor.RED;
            @Override
            public void run() {
                obj.setDisplayName("   " + getNext() + "   ");
                team.setPrefix(RAND.nextInt(9) + " Test");
                team2.setPrefix(RAND.nextInt(9) + " Other");

                if(next == ChatColor.RED) {
                    obj.getScore("debug").setScore(1);
                    next = ChatColor.BLUE;
                } else {
                    obj.getScoreboard().resetScores("debug");
                    next = ChatColor.RED;
                }

                team.setSuffix(next.toString());
                team2.setSuffix(next.toString());
            }

        }.runTaskTimer(this.plugin, 20l, 5l);

        player.setScoreboard(board);
    }
}
