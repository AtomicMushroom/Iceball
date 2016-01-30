package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.api.arena.SpawnType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import net.iceball.mgfw.impl.MinigameFramework;

import java.util.List;

/**
 * Created by Floris on 23-07-15.
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class is full with static               //
//  methods and allows editing your teams!       //
/////////////////////////////////////////////////*/
class TeamsModifier {

    /**
     * Sets the new public spawnpoint over all teams that have {@link SpawnType#PUBLIC}.
     * @param arenaImpl Arena instance
     * @param spawnpoint the new spawnpoint for all teams that have {@link SpawnType#PUBLIC}.
     * @param number The index number of the spawnpoint. <b>Note: the counting starts at 1, not 0!</b>
     */
    public static void setPublicSpawnpoint(ArenaImpl arenaImpl, Location spawnpoint, int number) {
        for (TeamImpl teamImpl : arenaImpl.teams){
            switch (teamImpl.spawntype){
                case PLAYER:
                    break;
                case TEAM:
                    break;
                case PUBLIC:
                    teamImpl.spawns[(number-1)] = spawnpoint;
                    break;
            }
            ArenaLoader.getLoader().overwrite(arenaImpl);
        }
    }

    /**
     * Gets the amount maximum amount of public spawns, used for synchronisation of public spawns.
     * @param arenaImpl Arena instance
     * @return the maximum amounts of spawns or 0 when not found.
     */
    public static int publicSpawnsMaximum(ArenaImpl arenaImpl){
        int i = 0;
        for (TeamImpl teamImpl : arenaImpl.teams){
            if (teamImpl.spawntype == SpawnType.PUBLIC){
                return teamImpl.spawns.length;
            }
        }
        return i;
    }

    /**
     * Makes a string that contains all teams marked with their color. Red means it's not ready to be saved, green means it is ready!
     * @param teamImpls List of teams, I recommend you to use {@link ArenaImpl#getTeams()} method }
     * @return String of all teams, separated with a comma (",").
     */
    public static String teamsInfo(List<TeamImpl> teamImpls){
        if (teamImpls == null || teamImpls.isEmpty() ){
            return ChatColor.RED + "There are no teams defined!";
        }
        String s = "";
        String n = MinigameFramework.suffix;
        for (TeamImpl teamImpl : teamImpls){
            if (TeamModifier.isTeamReady(teamImpl)){
                s = s + ChatColor.GREEN + teamImpl.name + n + ", ";
            } else {
                s = s + ChatColor.RED + teamImpl.name + n + ", ";
            }
        }
        return s;
    }



}
