package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.api.arena.SpawnType;
import net.iceball.mgfw.impl.arena.exceptions.TeamAlreadyExistException;
import net.iceball.mgfw.impl.util.ObjectInfo;
import net.iceball.mgfw.impl.MinigameFramework;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Floris on 23-07-15.
 ///////////////////////////////////////////////////
 //          What does this class do?             //
 ///////////////////////////////////////////////////
 //  This class is full with static               //
 //  methods and allows editing your team.        //
 /////////////////////////////////////////////////*/
class TeamModifier {

    /**
     * Rename the team.
     * @param arenaImpl Arena instance
     * @param teamImpl Team that will be renamed
     * @param name The new name of team.
     */
    public static void setTeamName(ArenaImpl arenaImpl, TeamImpl teamImpl, String name)  {
        arenaImpl.deleteTeam(teamImpl.name);
        TeamImpl newTeamImpl = new TeamImpl(teamImpl, name);
        arenaImpl.addTeam(newTeamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Creates a new team for the arena.
     * @param arenaImpl Arena instance
     * @param name Name of the team
     * @param players Maximum amount of players
     * @param spawnType SpawnType of Team
     * @param spawns Amount of spawns the player has
     * @throws TeamAlreadyExistException
     */
    public static void createTeam(ArenaImpl arenaImpl, String name, int players, SpawnType spawnType, int spawns)
            throws TeamAlreadyExistException {
        arenaImpl.addTeam(name, players, spawnType, spawns);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets a new array of spawns for the given team in the given arena. You can make the array empty, or fill it with
     * your own values.
     * @param arenaImpl Arena instance
     * @param teamImpl Team that will have the new array values.
     * @param spawns The new values that will be set.
     */
    public static void setTeamSpawns(ArenaImpl arenaImpl, TeamImpl teamImpl, Location[] spawns){
        teamImpl.setSpawns(spawns);
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the Team spawn of Arena
     * @param arenaImpl Arena instance
     * @param spawn The new spawnpoint
     * @param teamImpl Team that will have the new spawnpoint
     * @param number The index number of the spawn. <b>Note: the counting starts at 1, not 0.</b>
     * @throws IndexOutOfBoundsException when you give this method a stupid number.
     */
    public static void setTeamSpawn(ArenaImpl arenaImpl, Location spawn, TeamImpl teamImpl, int number){
        switch (teamImpl.spawntype){
            case PLAYER:
                teamImpl.spawns[(number-1)] = spawn;
                break;
            case TEAM:
                teamImpl.spawns[(number-1)] = spawn;
                break;
            case PUBLIC:
                TeamsModifier.setPublicSpawnpoint(arenaImpl, spawn, number);
                return;
        }
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the new value suddendeath for the given team.
     * @param arenaImpl Arena instance
     * @param teamImpl Team whose values are about to change ;D
     * @param suddendeath Whether the player is moved to the spectator team after his first death or not.
     */
    public static void setTeamSuddendeath(ArenaImpl arenaImpl, TeamImpl teamImpl, boolean suddendeath) {
        arenaImpl.deleteTeam(teamImpl.name);
        teamImpl.suddenDeath = suddendeath;
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the new amount of players for the given team.
     * @param arenaImpl Arena instance
     * @param teamImpl Team with the new amount of players
     * @param players Max players the team can have.
     */
    public static void setTeamPlayers(ArenaImpl arenaImpl, TeamImpl teamImpl, int players) {
        teamImpl.players = players;
        switch (teamImpl.spawntype) {
            case PLAYER:
                Location[] clone = teamImpl.spawns.clone();
                teamImpl.spawns = new Location[players];
                int length = Math.min(teamImpl.spawns.length, clone.length);

                for (int i = 0; i < length; i++) {
                    teamImpl.spawns[i] = clone[i];
                }
                arenaImpl.addTeam(teamImpl);
                ArenaLoader.getLoader().overwrite(arenaImpl);
        }
    }

    /**
     * Sets the new Team spawntype.
     * @param arenaImpl Arena instance
     * @param teamImpl Team with the new SpawnType
     * @param type Have a look at the {@link SpawnType } class
     */
    public static void setTeamSpawnType(ArenaImpl arenaImpl, TeamImpl teamImpl, SpawnType type) {
        teamImpl.spawntype = type;
        Location[] clone;
        int length;
        switch (type) {
            case PLAYER:
                clone = teamImpl.spawns.clone();
                teamImpl.spawns = new Location[teamImpl.players];
                length = Math.min(teamImpl.spawns.length, clone.length);
                for (int i = 0; i < length; i++) {
                    teamImpl.spawns[i] = clone[i];
                }
                break;
            case TEAM:
                break;
            case PUBLIC:
                for (TeamImpl temp : arenaImpl.teams) {
                    if (temp.spawntype.equals(SpawnType.PUBLIC)) {
                        teamImpl.spawns = temp.spawns;
                        break;
                    }
                }
        }
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Checks if the team is ready to be used for the game.
     * @param teamImpl Team that will be checked
     * @return True when the team is ready to be saved, false when the team is missing some values.
     */
    public static boolean isTeamReady(TeamImpl teamImpl){
        if (!(teamImpl.name == null || teamImpl.name.isEmpty())){ // I like brackets, problem?
            if (!(teamImpl.prefix == null || teamImpl.prefix.isEmpty())){
                if (!(teamImpl.suffix == null || teamImpl.suffix.isEmpty())){ // Okay maybe, this wasn't that smart.
                    if (teamImpl.players != 0){
                        if (teamImpl.spawns != null){ //It can be more worse, can it?
                            if (teamImpl.spawntype != null){
                                return true;
                            } // I
                        } //am
                    } //almost
                } //there
            } //come on
        } //yeah I feel it.
        return false;
        } //OOW YEASH

    /**
     * Gets a list of strings that has all information about the given team
     * @param teamImpl The given team
     * @return Lots of information abou the given team
     */
    public static List<String> teamInfo(TeamImpl teamImpl) {
        List<String> list = new ArrayList<String>();
        String z = MinigameFramework.mark;
        String s = MinigameFramework.suffix;

        list.add(s + "Name: " + z + teamImpl.name);
        list.add(s + "Prefix: " + z + teamImpl.prefix + " | " + convertColors(teamImpl.prefix));
        list.add(s + "Suffix: " + z + teamImpl.suffix + " | " + convertColors(teamImpl.suffix));
        list.add(s + "Suddendeath: " + z + teamImpl.suddenDeath);
        list.add(s + "Max players: " + z + teamImpl.players);
        list.add(s + "Main-startspawn: " + z + teamImpl.mainStartSpawn);
        list.add(s + "Spawntype: " + z + teamImpl.spawntype);
        if (teamImpl.spawns == null) {
            list.add("Spawns not defined.");
        } else {
            switch (teamImpl.spawntype) {
                case PUBLIC:
                    list.add(s + "Public Spawns:");
                    for (int i = 0; i < teamImpl.spawns.length; i++) {
                        list.add(s + "PB" + (i + 1) + ": " + z + ObjectInfo.locationDescription(teamImpl.spawns[i]));
                    }
                    break;
                case PLAYER:
                    list.add(s + "Player Spawns:");
                    for (int i = 0; i < teamImpl.spawns.length; i++) {
                        list.add(s + "PL" + (i + 1) + ": " + z + ObjectInfo.locationDescription(teamImpl.spawns[i]));
                    }
                    break;
                case TEAM:
                    list.add(s + "Team Spawns:");
                    for (int i = 0; i < teamImpl.spawns.length; i++) {
                        list.add(s + "TM" + (i + 1) + ": " + z + ObjectInfo.locationDescription(teamImpl.spawns[i]));
                    }
                    break;
            }
        }
        for (int i=0; i < list.size(); i++){
            String old = list.get(i);
            String newString = old.replace("null", "Not defined yet.");
            list.set(i, newString);
        }
        return list;
    }

    /**
     * Sets the new value of mainstartspawn of to true or false.
     * @param arenaImpl Arena instance
     * @param teamImpl Team that will have the new values.
     * @param mainstartspawn Set this to true if you want all your players to spawn at the first set location, this is
     * handy when you have multiple spawnpoints, but don't want everyone to spawn in different locations when the status
     * starts, but only after the start. Example: Paintball. Otherwise, set it to false.
     */
    public static void setTeamMainstartspawn(ArenaImpl arenaImpl, TeamImpl teamImpl, boolean mainstartspawn) {
        teamImpl.mainStartSpawn = mainstartspawn;
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the new prefix of the team. Can be used in chats.
     * @param arenaImpl Arena instance
     * @param teamImpl Team instance
     * @param prefix The new prefix of the given team.
     */
    public static void setTeamPrefix(ArenaImpl arenaImpl, TeamImpl teamImpl, String prefix) {
        teamImpl.prefix = prefix;
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the new suffix of the team. Can be used in chats.
     * @param arenaImpl Arena instance
     * @param teamImpl Team instance
     * @param suffix The new suffix of the given team.
     */
    public static void setTeamSuffix(ArenaImpl arenaImpl, TeamImpl teamImpl, String suffix) {
        teamImpl.suffix = suffix;
        arenaImpl.addTeam(teamImpl);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Sets the new spawns amount of the team.
     * @param arenaImpl Arena instance
     * @param teamImpl Team instance
     * @param spawns The amount of spawns for the given team.
     */
    public static void setSpawnsAmount(ArenaImpl arenaImpl, TeamImpl teamImpl, int spawns) {
        Location[] clone = teamImpl.spawns.clone(); //because otherwise we lose the results :d
        int length;
        switch (teamImpl.spawntype) {
            case PLAYER:
                break;
            case TEAM:
                clone = teamImpl.spawns.clone();
                teamImpl.spawns = new Location[spawns];
                length = Math.min(teamImpl.spawns.length, clone.length);

                for (int i = 0; i < length; i++) {
                    teamImpl.spawns[i] = clone[i];
                }
                arenaImpl.addTeam(teamImpl);
                break;
            case PUBLIC:
                Iterator<TeamImpl> iterator = arenaImpl.teams.iterator();
                List<TeamImpl> newTeamImpls = new ArrayList<>();

                while (iterator.hasNext()){
                    TeamImpl temp = iterator.next();
                    if (temp.spawntype == SpawnType.PUBLIC) {
                        temp.spawns = new Location[spawns];
                        length = Math.min(spawns, clone.length);
                        for (int i = 0; i < length; i++) {
                            temp.spawns[i] = clone[i];
                        }
                        newTeamImpls.add(temp);
                        iterator.remove();
                    }
                }
                for (TeamImpl temp : newTeamImpls) {
                    arenaImpl.addTeam(temp);
                }
                ArenaLoader.getLoader().overwrite(arenaImpl);
        }
    }

    private static String convertColors(String s){
        if (s == null){
            return "null";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    /**
     * Gets the next index number used to fill the next spawn automatically.
     * @param arenaImpl Arena instance
     * @param teamImpl Team that will be checked
     * @return 0 or the (index number + 1)
     */
    public static int getSpawnFreeNumber(ArenaImpl arenaImpl, TeamImpl teamImpl) {
        for (int i = 0; i < teamImpl.spawns.length; i++) {
            if (teamImpl.spawns[i] == null) {
                return (i+1);
            }
        }
        return 0;
    }
}
