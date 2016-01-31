package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.api.arena.MGTeam;
import net.iceball.mgfw.api.arena.SpawnType;
import net.iceball.mgfw.impl.arena.exceptions.TeamsNotInitiliasedException;
import net.iceball.mgfw.impl.util.Serialization;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Floris on 21-07-15.
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This class represents a team, you can only   //
//  create your team here, modifying happens in  //
//  in the Team(s)Modifier.class                 //
/////////////////////////////////////////////////*/
@SerializableAs("Team")
class TeamImpl implements MGTeam {

    /* Name of team. */
    public final String name;

    /* Prefix of team. */
    public String prefix = "";

    /* Suffix of team. */
    public String suffix = "";

    /* Max players the team can have. */
    public int players;

    /* Whether the player is moved to the spectator team after his first death or not. Default: true */
    public boolean suddenDeath = true;

    /*
     * Set this to true if you want all your players to spawn at the first set location, this is
     * handy when you have multiple spawnpoints, but don't want everyone to spawn in different locations when the status
     * starts, but only after the start. Example: Paintball. Default: true */
    public boolean mainStartSpawn = true;

    /* Whether the players in this team can hit each other (PVP), or not (PVE). */
    public boolean allowFriendlyfire = true;

    /* SpawnType of team:
    * PLAYER: Every player has his own custom spawn location  Example: SurvivalGames
    * PUBLIC: Each player spawns at the public spawnpoint(s) Example: TNTRun
    * TEAM: Each player spawns at the teams set spawnpoint(s) Example: Paintball    */
    public SpawnType spawntype = null;

    /* Spawns of team */
    protected Location[] spawns = null;

    protected void setSpawns(Location[] spawns) { this.spawns = spawns; }

    /** Used to create a teams for arena, you shouldn't use this method, unless you know what you're doing.
     * This constructor returns a half naked team, this team is not ready to be used or saved.
     * Using the super team constructor, will help you making sure it works!
     * @param name Name of player.
     * @param players Amount of players
     * @param spawnType Personal spawnType of team
     * @param spawns Amount of spawns. Overridden when spawnType equals Player.
     */
    public TeamImpl(String name, int players, SpawnType spawnType, int spawns) {
        super();
        this.name = name;
        this.players = players;
        this.spawntype = spawnType;
        suddenDeath = true;
        mainStartSpawn = true;
        switch (spawnType) {
            case PUBLIC:
                this.spawns = new Location[spawns];
                break;
            case TEAM:
                this.spawns = new Location[spawns];
                break;
            case PLAYER:
                this.spawns = new Location[players];
                break;
        }
    }

    /**
     * This is a super constructor for creating teams, used to mostly used to deserialize Team instance.
     *
     * @param name Name of team.
     * @param prefix Prefix of team, can be used in chats.
     * @param suffix Suffix of team, can be used in chats.
     * @param players Amount of players in team, you shouldn't and cannot change players once in this stage.
     * @param suddenDeath Wheter the player is moved to the spectator team after his first death or not.
     * @param mainStartSpawn When true all your players to spawn at the first set location, this is
     * handy when you have multiple spawnpoints, but don't want everyone to spawn in different locations when the game
     * starts, but only after the start.
     * @param spawntype Spawntype of team
     * @param spawns Amount of spawns
     */
    public TeamImpl(String name, String prefix, String suffix, int players, Boolean suddenDeath, Boolean mainStartSpawn, SpawnType spawntype, Location[] spawns) {
        super();
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.players = players;
        this.suddenDeath = suddenDeath;
        this.mainStartSpawn = mainStartSpawn;
        this.spawntype = spawntype;
        this.spawns = spawns;
    }

    /**
     * This constructor is used to rename the team.
     * @param teamImpl Team that will be renamed.
     * @param newName The new name.
     */
    public TeamImpl(TeamImpl teamImpl, String newName){
        super();
        this.name = newName;
        this.prefix = teamImpl.prefix;
        this.suffix = teamImpl.suffix;
        this.players = teamImpl.players;
        this.suddenDeath = teamImpl.suddenDeath;
        this.mainStartSpawn = teamImpl.mainStartSpawn;
        this.spawntype = teamImpl.spawntype;
        this.spawns = teamImpl.spawns;
    }

    /**
     * Team implements ConfigurationSerializable, this method is automatically called when you save your arena.
     * You shouldn't use this method, only if you handle the saving.
     * @see org.bukkit.configuration.serialization.ConfigurationSerializable
     * @return Map with all values of the class instance.
     */
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new TreeMap<>();
        map.put("prefix", prefix);
        map.put("suffix", suffix);
        map.put("players", players);
        map.put("suddendeath", suddenDeath);
        map.put("mainstartspawn", mainStartSpawn);
        map.put("spawntype", spawntype.toString());
        for (int i=0; i < spawns.length; i++){
            map.put("S" + (i+1), Serialization.serializeLocation(spawns[i]));
        }
        return map;
    }

    /**
     * Deserialize Team from config, this method is called by Bukkit itself.
     * @param map From ConfigurationSection, with all the values of one team.
     * @return a Team instance. With an empty name!
     */
    public static TeamImpl deserialize(Map<String, Object> map){
        if (map == null){
            return null;
        }
        String prefix = (String) map.get("prefix");
        String suffix = (String) map.get("suffix");
        int players = (Integer) map.get("players");
        boolean suddendeath = (Boolean) map.get("suddendeath");
        boolean mainstartspawn = (Boolean) map.get("mainstartspawn");
        SpawnType type = (SpawnType.valueOf((String)map.get("spawntype")));
        int spawnsamount=0;
        for (String key : map.keySet()){
            if (key.contains("S")){
                spawnsamount++;
            }
        }
        Location[] spawns = new Location[spawnsamount];
        for (int i=0; i > spawns.length; i++){
            spawns[i] = Serialization.deserializeLocation((String)map.get("S"+(i+1)));
        }
        return new TeamImpl("", prefix, suffix, players, suddendeath, mainstartspawn, type, spawns);
    }

    /**
     * Look at: deserialize(Map<String, Object> map) for more information.
     * Returns Team from values of the Map given.
     * @param map The map with the given team values. The key: "name" has to be included!
     * @return a Team instance, with name.
     */
    public static TeamImpl valueOf(Map<String, Object> map){
        if (map == null){
            return null;
        }
        String name = (String) map.get("name");
        String prefix = (String) map.get("prefix");
        String suffix = (String) map.get("suffix");
        int players = (Integer) map.get("players");
        boolean suddendeath = (Boolean) map.get("suddendeath");
        boolean mainstartspawn = (Boolean) map.get("mainstartspawn");
        SpawnType type = (SpawnType.valueOf((String)map.get("spawntype")));

        int spawnsamount=0;
        for (String key : map.keySet()){
            if (key.contains("S")){
                spawnsamount++;
            }
        }
        Location[] spawns = new Location[spawnsamount];
        for (int i=0; i < spawns.length; i++){
            spawns[i] = Serialization.deserializeLocation((String)map.get("S"+(i+1)));
        }
        return new TeamImpl(name, prefix, suffix, players, suddendeath, mainstartspawn, type, spawns);
    }

    public String getName() {
        return name;
    }

    public SpawnType getSpawntype() {
        return spawntype;
    }

    public Location[] getSpawns() { return spawns; }

    public boolean isSuddenDeath() {
        return suddenDeath;
    }

    public void isSuddendeath(boolean suddendeath) {
        this.suddenDeath = suddendeath;
    }

    public boolean isMainStartSpawn() {
        return mainStartSpawn;
    }

    public void isMainStartSpawn(boolean mainstartspawn) {
        this.mainStartSpawn = mainstartspawn;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public int getMaximumPlayers() {
        return players;
    }

    public boolean allowFriendlyFire() {
        return allowFriendlyfire;
    }

    public void setAllowFriendlyFire(boolean friendlyfire) {
        this.allowFriendlyfire = friendlyfire;
    }

    /**
     * @param score Score that will be added.
     */
    public void addScore(int score) {

    }

    public void setScore(int score){

    }

    public Team getTeam() throws TeamsNotInitiliasedException {

        //GameManager.getInstance() //TODO: this bullshit
        return null;
    }
}
