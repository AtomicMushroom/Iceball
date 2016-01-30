package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.api.arena.Arena;
import net.iceball.mgfw.api.game.GameStatus;
import net.iceball.mgfw.api.arena.SpawnType;
import net.iceball.mgfw.api.arena.MGTeam;
import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.arena.exceptions.ArenaAlreadyExistException;
import net.iceball.mgfw.impl.arena.exceptions.ArenaNullException;
import net.iceball.mgfw.impl.arena.exceptions.TeamAlreadyExistException;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import net.iceball.mgfw.impl.util.Serialization;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Floris on 21-07-15.*/
/*//////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class represents the arena              //
//  that is used in the games. Modifying methods //
//  are can be found in the                      //
 @see net.iceball.mgfw.arena.ArenaModifier class  //
/////////////////////////////////////////////////*/

class ArenaImpl implements Arena {

    public final String name;
    protected List<Location> blocks = null; //
    protected List<TeamImpl> teams = null;  //
    private GameStatus status = GameStatus.OFF; //
    org.bukkit.Location lobby = null; //
    org.bukkit.Location spectator = null; //

    /**
     * Super big constructor for creating Arena instance, used for serialization.
     * In this method, the arena is not added to the ArenaLoader.
     *
     * @param name      Name of arena
     * @param lobby     Location of lobby
     * @param spectator Location of spectator
     * @param blocks    List of chest locations
     * @param teams     List of teams
     */
    public ArenaImpl(String name, Location lobby, Location spectator, List<Location> blocks, List<TeamImpl> teams) {
        this.name = name;
        this.lobby = lobby;
        this.spectator = spectator;
        this.blocks = blocks;
        this.teams = teams;
        this.status = GameStatus.OFF;
    }

    /**
     * Constructor to create an arena, and add them to the loader.
     *
     * @param name Name of arena
     * @throws ArenaAlreadyExistException when the name of the arena is already exist in the ArenaLoader.
     */
    public ArenaImpl(String name) throws ArenaAlreadyExistException {
        this.name = name;
        teams = new ArrayList<>();
        blocks = new ArrayList<>();
        ArenaLoader.getLoader().add(name, this);
    }

    /**
     * Sets the location when the player is dead, or has no lifes left (depends).
     *
     * @param spectator Location of specator
     */
    public void setSpectator(Location spectator) {
        this.spectator = spectator;
        ArenaLoader.getLoader().overwrite(this);
    }

    /**
     * Sets the location where the players will be teleported when the game is waiting.
     *
     * @param lobby Location of the lobby.
     */
    public void setLobby(Location lobby) {
        this.lobby = lobby;
        ArenaLoader.getLoader().overwrite(this);
    }


    /**
     * Tells how the game is looking, or if it even is has been started.
     *
     * @return status of the game.
     */
    public GameStatus getStatus() {
        return status;
    }


    /**
     * Sets the status of the game! Do not call this method to see what it does!
     *
     * @param status the stadium of the game.
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }

    /**
     * Tells if the arena is null or not. When the arena is not found, a empty one has been set into place.
     *
     * @return Whether the arena is empty or not.
     */
    public boolean isEmpty() {
        return name.isEmpty();
    }

    /**
     * Gets the list of custom blocks that were set in the arena.
     * It is up to the developer to say what type of use the blocks will get.
     *
     * @return a list of all the blocks!
     */
    public List<Block> getBlocks() {
        return this.blocks.stream().map(Location::getBlock).collect(Collectors.toList());
    }

    /**
     * Gets the name of the arena!
     *
     * @return the name of the arena
     */
    public String getName() {
        return name;
    }

    /**
     * Whether the arena is complete and ready to be saved or is not complete and not ready to be saved.
     * @return true when the arena is ready to be saved, otherwise false.
     */
    public boolean isComplete() {
        return ArenaModifier.isArenaReady(this);
    }

    /**
     * Gets the spectator location of the arena.
     *
     * @return Location when the player is dead, or has no lifes left (depends).
     */
    public Location getSpectator() {
        return spectator;
    }

    /**
     * Gets the lobby location of the arena.
     *
     * @return Location where the players will be teleported when the game is waiting.
     */
    public Location getLobby() {
        return lobby;
    }

    /**
     * Empty constructor used to prevent returning null.
     */
    protected ArenaImpl() {
        name = "";
    }

    /**
     * Gets the maximum amount of players that can be in the arena.
     * It is calculated based of all the teams maximum players together.
     *
     * @return The maximum players that can be in the teams.
     */
    public int getMaxPlayers() {
        int i = 0;
        if (teams != null) {
            for (TeamImpl t : teams) {
                i = t.players + i;
            }
        }
        return i;
    }

    /**
     * Gets the team from the teams list.
     *
     * @param name of team
     * @return Team, else null
     */
    public TeamImpl getTeam(String name) {
        for (TeamImpl t : teams) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Adds or replaces if exist the team to the teams list,
     *
     * @param teamImpl Team instance
     */
    public void addTeam(TeamImpl teamImpl) {
        deleteTeam(teamImpl.name);
        teams.add(teamImpl);
    }

    /**
     * Adds the team to the teams list.
     *
     * @param name      Name of the team
     * @param players   Maximum amount of players in team
     * @param spawnType SpawnType of team, can be PLAYER, TEAM or PUBLIC
     * @param spawns    Maximum amount of spawns
     * @throws TeamAlreadyExistException If the name of the team already exist.
     */
    public void addTeam(String name, int players, SpawnType spawnType, int spawns) throws TeamAlreadyExistException {
        for (TeamImpl temp : teams) {
            if (temp.name.equalsIgnoreCase(name)) {
                throw new TeamAlreadyExistException("Team name is already in use!");
            }
        }
        TeamImpl t = new TeamImpl(name, players, spawnType, spawns);
        TeamImpl other = null;
        boolean publicSpawnsRefresh = false;
        if (spawnType == SpawnType.PUBLIC) {
            for (TeamImpl temp : teams) {
                if (temp.spawntype == SpawnType.PUBLIC) {
                    publicSpawnsRefresh = true;
                    other = temp;
                    break;
                }
            }
        }
        teams.add(t);
        if (publicSpawnsRefresh) {
            TeamModifier.setTeamSpawns(this, t, other.getSpawns());
        }
    }

    /**
     * Tries to delete a team with the given name, even if the team doesn't exist.
     *
     * @param name Name of Team
     */
    public void deleteTeam(String name) {
        if (name == null) {
            return;
        }
        Iterator<TeamImpl> iterator = teams.iterator();
        while (iterator.hasNext()) {
            TeamImpl temp = iterator.next();
            if (temp.name.equalsIgnoreCase(name)) {
                iterator.remove();
                break;
            }
        }
    }

    /**
     * Gets all the teams of the arena.
     *
     * @return List of all Teams
     */
    @SuppressWarnings("unchecked")
    public List<MGTeam> getTeams() {
        List<?> t = (teams);
        return (List<MGTeam>) t;
    }

    /**
     * Gets all the team names in one string.
     *
     * @return All the teams in format: Team1, Team2, etc.
     */
    private String getTeamNames() {
        String list = "";
        for (TeamImpl teamImpl : teams) {
            list = list + teamImpl.name + ",";
        }
        return list;
    }

    /**
     * Saves the arena in the config. If there already exist an arena with that name, the source will be overwritten.
     *
     * @throws ArenaNullException When the arena is not ready to be saved.
     */
    public void serialize(MinigameFramework plugin) throws ArenaNullException {
        if (!ArenaModifier.isArenaReady(this)) {
            throw new ArenaNullException("Arena is not complete!");
        }
        Config config = new Config(ConfigFile.arenas_yml, plugin);
        String prefix = "arenas." + name;
        config.getConfig().set(prefix + ".lobby", Serialization.serializeLocation(lobby));
        config.getConfig().set(prefix + ".spectator", Serialization.serializeLocation(spectator));

        if (!blocks.isEmpty()) {
            for (int i = 0; i < blocks.size(); i++) {
                config.getConfig().set(prefix + ".blocks.B" + (i + 1), Serialization.serializeBlock(blocks.get(i)));
            }
        }
        config.getConfig().set(prefix + ".teams", getTeamNames());
        for (TeamImpl t : teams) {
            config.getConfig().set(prefix + ".teams." + t.name, t.serialize());
        }
        config.saveConfig();
    }

    /**
     * Get the arena back from the config.
     *
     * @param name Name of arena, used to get it from the config
     * @return Arena instance
     */
    public static ArenaImpl deserialize(String name, MinigameFramework plugin) {
        String prefix = "arenas." + name;
        Config config = new Config(ConfigFile.arenas_yml, plugin);
        Location lobby = Serialization.deserializeLocation((String) config.getConfig().get(prefix + ".lobby"));
        Location spectator = Serialization.deserializeLocation((String) config.getConfig().get(prefix + ".spectator"));
        int max = config.getHighestBlock(name);
        List<Location> blocks = new ArrayList<>();
        if (max != 0) {
            for (int i = 0; i < max; i++) {
                blocks.add(Serialization.deserializeBlock((String) config.getConfig().get(prefix + ".blocks.B" + (i + 1))));
            }
        }
        List<TeamImpl> teamImpls = new ArrayList<>();
        Set<String> teamNames = config.getTeamNames(name);
        if (!teamNames.isEmpty()) {
            for (String tName : teamNames) {
                Map<String, Object> map = config.getTeam(name, tName);
                map.put("name", tName);
                teamImpls.add(TeamImpl.valueOf(map));
            }
        }
        return new ArenaImpl(name, lobby, spectator, blocks, teamImpls);
    }
}