package net.iceball.mgfw.impl.arena;


import net.iceball.mgfw.api.arena.SpawnType;
import net.iceball.mgfw.api.game.GameStatus;
import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.arena.exceptions.ArenaAlreadyExistException;
import net.iceball.mgfw.impl.arena.exceptions.ArenaNullException;
import net.iceball.mgfw.impl.arena.exceptions.TeamAlreadyExistException;
import net.iceball.mgfw.impl.util.BaseCommand;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import net.iceball.mgfw.impl.util.ObjectInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * Created by Floris on 21-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
///////////////////////////////////////////////////
//          What does this class do?             //
///////////////////////////////////////////////////
//  This is class represents the arena creation  //
//  command                                      //
///////////////////////////////////////////////////
class MGCommand implements BaseCommand {
    /*
        Command Reference, find command by arguments length.
    0 /mg
    1 /mg arenas
    1 /mg spawntypes
    1 /mg mainstartspawn
    1 /mg <arena>
    2 /mg help <number>
    2 /mg <arena> create
    2 /mg <arena> delete
    2 /mg <arena> edit
    2 /mg <arena> save
    2 /mg <arena> setlobby
    2 /mg <arena> setspectator
    2 /mg <arena> ready
    2 /mg <arena> teams
    3 /mg <arena> rename <name>
    2 /mg <arena> blocks
    3 /mg <arena> block add
    3 /mg <arena> blocks reset
    3 /mg <arena> block remove
    3 /mg <arena> team <team>
    4 /mg <arena> block remove (number)
    4 /mg <arena> team <team> delete
    4 /mg <arena> team <team> setspawn
    5 /mg <arena> team <team> setspawn (number)
    5 /mg <arena> team <team> name <value>
    5 /mg <arena> team <team> prefix <value>
    5 /mg <arena> team <team> suffix <value>
    5 /mg <arena> team <team> suddendeath <value>
    5 /mg <arena> team <team> players <value>
    5 /mg <arena> team <team> spawntype <value>
    5 /mg <arena> team <team> mainstartspawn <value>
    5 /mg <arena> team <team> spawns <value>
    7 /mg <arena> team <team> create <max players> <spawntype> <spawns> */
    private MinigameFramework plugin;
    private Config config;
    private final String command = "mg";

    MGCommand(MinigameFramework plugin) {
        this.plugin = plugin;
        config = new Config(ConfigFile.arenas_yml, plugin);
    }

    // Dear maintainer:
    //
    // Once you are done trying to 'optimize' this command,
    // and have realized what a terrible mistake that was,
    // please increment the following counter as a warning
    // to the next guy:
    //
    // total_hours_wasted_here = 1;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(command)) {
            String p = MinigameFramework.prefix;
            String s = MinigameFramework.suffix;
            String z = MinigameFramework.mark;
            String r = ChatColor.RED + "";
            TeamImpl teamImpl;
            List<String> list;
            String option;
            String name;
            String value;
            ArenaImpl arenaImpl = null;
            if(args.length >= 1){
                arenaImpl = ArenaLoader.getLoader().getArena(args[0]);
                if (arenaImpl.getStatus() != GameStatus.OFF){
                    sender.sendMessage(p + "You can only edit arenas in edit-mode!");
                    return true;
                }
            }
            assert arenaImpl != null;
            switch (args.length) {
                case 0:
                    helpReference(1, sender);
                    return true;
                case 1:
                    option = args[0].toLowerCase();
                    name = args[0];
                    switch (option) {
                        case "arenas":
                             list = ArenaLoader.getLoader().getArenasNames();
                            if (list.isEmpty()){
                                sender.sendMessage(p + ChatColor.RED + "There are no arenas registered.");
                                return true;
                            }
                            String text;
                            if (list.size() == 1){
                                sender.sendMessage(p + "Total of 1 arena.");
                                text = s + "Arena: " + z;
                            } else {
                            sender.sendMessage(p + "Total of " + list.size() + " arenas.");
                                text = s + "Arenas: " + z;
                            }

                            for (int i=0; i < list.size(); i++){
                                String temp = list.get(i);
                                if ((i+1) == list.size()){
                                    text = text + temp + s + ".";
                                } else {
                                    text = text + temp + s + ", " + z;
                                }
                            }
                            sender.sendMessage(text);
                            return true;
                        case "spawntypes":
                            sender.sendMessage(p + "Many games have different spawn options,\n" +
                                    " sometimes you want a player specific spawn point (" + z + "PLAYER" + s + "),\n" + s +
                                    "other times team specific (" + z + "TEAM" + s + ")" +
                                    ", and last but not least if you want" + s +
                                    " everyone to spawn in the same area, use public (" + z + "PUBLIC" + s + ")." );
                            sender.sendMessage(z + "Notice: You can have multiple public/team spawns, the spawnpoint " +
                                    "will be chosen randomly, unless (see: mainStartSpawn).");
                            return true;
                        case "help":
                            helpReference(1, sender);
                            return true;
                        case "mainstartspawn":
                           sender.sendMessage(p + "Set this to true if you want all your players to spawn at the first set location, " +
                                   "this is handy when you have multiple spawnpoints, but don't want everyone to spawn in different" +
                                   " locations when the status starts, only after the start. Example: a Paintball game");
                            return true;
                        default:
                            if (!arenaImpl.isEmpty()){
                                sender.sendMessage(p + "All info about Arena " + z + "'" + arenaImpl.name + "'" + s + ".");
                                List<String> l = ArenaModifier.arenaInfo(arenaImpl);
                                for (String temp : l){
                                    sender.sendMessage(temp);
                                }
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                            return true;
                    }
                case 2:
                    name = args[0];
                    option = args[1].toLowerCase();
                    switch (option) {
                        case "create":
                            if (name.contains(":") || name.contains(",")){
                                sender.sendMessage(p + ChatColor.DARK_RED + "Sorry, but I don't want a name with symbols.");
                                return true;
                            }
                            try {
                                arenaImpl = new ArenaImpl(name);
                            } catch (ArenaAlreadyExistException e) {
                                sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + "already exists!");
                                return true;
                            }
                            sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + "created!");
                            return true;
                        case "delete":
                            ArenaModifier.deleteArena(arenaImpl, plugin);
                            sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + "deleted.");
                            return true;
                        case "edit":
                            sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + " is now in edit-mode.");
                            return true;
                        case "save":
                            if (arenaImpl.isEmpty()) {
                                sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                                return true;
                            }
                            if (!ArenaModifier.isArenaReady(arenaImpl)){
                                sender.sendMessage(p + "Sorry, but the arena is not ready, you're missing some things.");
                                return true;
                            }
                            try {
                                ArenaModifier.saveArena(arenaImpl, plugin);
                            } catch (ArenaNullException e) {
                                sender.sendMessage(p + r + e.getLocalizedMessage());
                                return true;
                            }
                            sender.sendMessage(p + "Arena " + z + "'" + name + "'" + s + " saved!");
                            return true;
                        case "setlobby":
                            if (isPlayer(sender)) {
                                Player player = (Player) sender;
                                Location loc = player.getLocation();
                                if (arenaImpl.isEmpty()) {
                                    sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                                    return true;
                                }
                                arenaImpl.setLobby(loc);
                                //ArenaModifier.setLobby(arenaImpl, loc);
                                player.sendMessage(p + "Lobby spawnpoint has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                            }
                            return true;
                        case "setspectator":
                            if (isPlayer(sender)) {
                                Player player = (Player) sender;
                                Location loc = player.getLocation();
                                if (arenaImpl.isEmpty()) {
                                    sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                                    return true;
                                }
                                arenaImpl.setSpectator(loc);
                                //ArenaModifier.setSpectator(arenaImpl, loc);
                                player.sendMessage(p + "Spectator spawnpoint has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                            }
                            return true;
                        case "ready": // still doesnt work
                            if (arenaImpl.isEmpty()) {
                                sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                                return true;
                            }
                            if (ArenaModifier.isArenaReady(arenaImpl)){
                                sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + "is ready to be saved!");
                                return true;
                            }
                            sender.sendMessage(p + "Arena " + z + "'" + name + "' " + s + "isn't ready to be saved!");
                            List<String> l = ArenaModifier.arenaInfo(arenaImpl);
                            for (String temp : l){
                                sender.sendMessage(temp);
                            }
                            return true;
                        case "teams":
                            if (!arenaImpl.isEmpty()) {
                                int max = arenaImpl.getTeams().size();
                                if (max == 1) {
                                    sender.sendMessage(p + "Total of 1 team.");
                                } else {
                                    sender.sendMessage(p + "Total of " + max + " teams.");
                                }
                                int i = 0;
                                for (TeamImpl t : arenaImpl.teams){
                                    i++;
                                    sender.sendMessage(s + "Team " + i + ": " + t.name);
                                }
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                            return true;
                        case "blocks":
                            if (!arenaImpl.isEmpty()) {
                                list = ArenaModifier.getBlocks(arenaImpl);
                                if (list.size() == 0){
                                    sender.sendMessage(p + r + "No blocks defined!");
                                    return true;
                                } else if (list.size() == 1){
                                    sender.sendMessage(p + "Total of 1 block.");
                                } else {
                                    sender.sendMessage(p + "Total of " + list.size() + " blocks.");
                                }
                                for (int i=0; i < list.size(); i++){
                                    sender.sendMessage(s + "Block " + (i+1) + ": " + list.get(i));
                                }
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exist!");
                            return true;
                        default:
                            if (name.equalsIgnoreCase("help")){
                                try {
                                    Integer i = Integer.valueOf(args[1]);
                                    helpReference(i, sender);
                                } catch (NumberFormatException nfe){
                                    sender.sendMessage(p + r + "Oops, I needed a number between 1 and 4.");
                                }
                                return true;
                            }
                            return false;
                    }
                case 3:
                    name = args[0];
                    option = args[1].toLowerCase();
                    value = args[2];

                    switch (option){
                        case "rename":
                            if (!arenaImpl.isEmpty()) {
                                try {
                                    ArenaModifier.renameArena(arenaImpl, value, plugin);
                                } catch (ArenaAlreadyExistException e) {
                                    sender.sendMessage(p + r + "Name " + ChatColor.DARK_RED + "'" + value + "'" + r + " already exist!");
                                    return true;
                                }
                                sender.sendMessage(p + "Arena " + z + "'" + name + "'" + s + " renamed to " + z + "'" + value + "' " + s + ".");
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                            return true;
                        case "team":
                            if (!arenaImpl.isEmpty()) {
                                   teamImpl = arenaImpl.getTeam(value);
                                if (teamImpl != null) {
                                    list = TeamModifier.teamInfo(teamImpl);
                                    sender.sendMessage(p + "Team Information:");
                                    for (String temp : list){
                                        sender.sendMessage(temp);
                                    }
                                    return true;
                                }
                                sender.sendMessage(p + r + "Team " + ChatColor.DARK_RED + "'" + value + "'" + r + " doesn't exists!");
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                            return true;
                        case "block":
                            if (!arenaImpl.isEmpty()) {
                                value = value.toLowerCase();
                                switch (value){
                                    case "add":
                                        if (isPlayer(sender)){
                                            Player player = (Player) sender;
                                            Set<Material> materials = null;
                                            Block b = player.getTargetBlock(materials, 12);
                                            Location location = b.getLocation();

                                            ArenaModifier.addBlock(arenaImpl, location);
                                            String item = location.getBlock().getType().name();
                                            sender.sendMessage(p + item + " added! " + z + ObjectInfo.blockDescription(location));
                                            return true;
                                        }
                                        return true;
                                    case "remove":
                                        if (isPlayer(sender)){
                                            Player player = (Player) sender;
                                            Set<Material> materials = null;
                                            Block b = player.getTargetBlock(materials, 12);
                                            Location location = b.getLocation();

                                            ArenaModifier.removeBlock(arenaImpl, location);
                                            String item = location.getBlock().getType().name();
                                            sender.sendMessage(p + item + " removed! " + z + ObjectInfo.blockDescription(location));
                                            return true;
                                        }
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                            return true;
                        case "blocks":
                            value = value.toLowerCase();
                            if (value.equalsIgnoreCase("reset")){
                                ArenaModifier.resetBlocks(arenaImpl);
                                sender.sendMessage(p + "Resetted blocks!");
                                return true;
                            }
                            return false;
                        default:
                            return false;
                    }
                case 4:
                    name = args[0];
                    option = args[3].toLowerCase();
                    value = args[2];
                    String err = "";
                    boolean blocksCMD = false;
                       if (args[1].toLowerCase().equalsIgnoreCase("block")) {
                            option = args[2].toLowerCase();
                            value = args[3].toLowerCase();
                            blocksCMD = true;
                            err = ChatColor.RED + "/mg <arena> block remove (number)";
                       } else if (args[1].toLowerCase().equalsIgnoreCase("team")) {
                            err = ChatColor.RED + "/mg <arena> team <team> delete|setspawn";
                       } else {
                           return false;
                       }
                    switch (option){
                        case "delete":
                            if (blocksCMD){
                                break;
                            }
                            if (!arenaImpl.isEmpty()) {
                                arenaImpl.deleteTeam(value); //it was to late when I discovered this. :p
                                sender.sendMessage(p  + "Team " + z + "'" + value + "'" + s + " deleted!");
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                            return true;
                        case "setspawn":
                            if (blocksCMD){
                                break;
                            }
                            if (!arenaImpl.isEmpty()) {
                                teamImpl = arenaImpl.getTeam(value);
                                if (teamImpl != null) {
                                    if (isPlayer(sender)) {
                                        Player player = (Player) sender;
                                        Location loc = player.getLocation();
                                        int auto = TeamModifier.getSpawnFreeNumber(arenaImpl, teamImpl);
                                        if (auto == 0) {
                                            sender.sendMessage(p + "All spawns have been set for team " + z + "'" + teamImpl.name + "'" + s + "!");
                                            return true;
                                        }
                                        TeamModifier.setTeamSpawn(arenaImpl, loc, teamImpl, auto);
                                        int max = teamImpl.getSpawns().length;
                                        switch (teamImpl.spawntype) {
                                            case PLAYER:
                                                player.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " player spawnpoint " + auto + " of " + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                                return true;
                                            case TEAM:
                                                player.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " team spawnpoint " + auto + " of " + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                                return true;
                                            case PUBLIC:
                                                player.sendMessage(p + "Teams public spawnpoint " + auto + " of " + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                                return true;
                                            default:
                                                break;
                                        }
                                        return false;
                                    }
                                    sender.sendMessage(p + r + "You have to be a player to use this command!");
                                }
                                sender.sendMessage(p + r + "Team " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                                return true;
                            }
                            sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                            return true;
                        case "remove":
                            if (!blocksCMD){
                                break;
                            }
                            int i = 0;
                             try {
                                 i = Integer.valueOf(value);
                             } catch (NumberFormatException nfe){
                                 sender.sendMessage(p + r + "Has to be a number!");
                                 return true;
                             }
                            if(arenaImpl.getBlocks().size() < i){
                                sender.sendMessage(p + r + "Number too big!");
                                return true;
                            }
                            ArenaModifier.removeBlock(arenaImpl, (i - 1));
                            sender.sendMessage(p + "Removed block " + z + i + ".");
                            return true;
                        default:
                            break;
                    }
                    if (err != null){
                    sender.sendMessage(err);
                    return true;
                    }
                    return false;
                case 5:
                    name = args[0];
                    option = args[3].toLowerCase();
                    value = args[4];
                    teamImpl = arenaImpl.getTeam(args[2]);
                    if (arenaImpl.isEmpty()){
                    sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                    return true;
                    } else if (teamImpl == null){ sender.sendMessage(p + r + "Team " + ChatColor.DARK_RED + "'" + args[2] + "'" + r + " doesn't exists!");
                        return true;
                        }

                        switch (option){
                            case "setspawn":
                                int spawnNumber;
                                if (isPlayer(sender)) {
                                    Player player = (Player) sender;
                                    int max = teamImpl.getSpawns().length;
                                    try {
                                      spawnNumber = Integer.valueOf(value);
                                      if (spawnNumber > max || spawnNumber < 1){
                                          throw new NumberFormatException();
                                      }
                                    } catch (NumberFormatException nfe){
                                       sender.sendMessage(p + ChatColor.RED + "Expected a number between 1 and " + max);
                                       return true;
                                    }
                                    Location loc = player.getLocation();
                                    TeamModifier.setTeamSpawn(arenaImpl, loc, teamImpl, spawnNumber);
                                    switch (teamImpl.spawntype) {
                                        case PLAYER:
                                            player.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " player spawnpoint " + spawnNumber + " of "  + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                            break;
                                        case TEAM:
                                            player.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " team spawnpoint " + spawnNumber + " of "  + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                            break;
                                        case PUBLIC:
                                            player.sendMessage(p + "Teams public spawnpoint " + spawnNumber + " of "  + max + " has been set: " + z + "(" + ObjectInfo.locationDescription(loc) + ")");
                                            break;
                                    }
                                    return true;
                                }
                                sender.sendMessage(p + r + "Team " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                                return true;
                            case "name":
                                String oldName = teamImpl.name;
                                TeamModifier.setTeamName(arenaImpl, teamImpl, value);
                                sender.sendMessage(p + "Team " + z + "'" + oldName + "'" + s + " renamed to " + z + "'" + value + "'" + s + ".");
                                return true;
                            case "prefix":
                                if (value.length() > 2){
                                    sender.sendMessage(p + r + "The prefix has to be in chatcolor format. Example: &a");
                                    return true;
                                }
                                TeamModifier.setTeamPrefix(arenaImpl, teamImpl, value);
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " prefix set to " + z + "'" + value + "'" + s + ".");
                                return true;
                            case "suffix":
                                if (value.length() > 2){
                                    sender.sendMessage(p + r + "The suffix has to be in chatcolor format. Example: &a");
                                    return true;
                                }
                                TeamModifier.setTeamSuffix(arenaImpl, teamImpl, value);
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " suffix set to " + z + "'" + value + "'" + s + ".");
                                return true;
                            case "suddendeath":
                                value = value.toLowerCase();
                                if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true")){
                                    sender.sendMessage(p + r + "Value can only be true or false!");
                                    return true;
                                }
                                boolean suddendeath = Boolean.parseBoolean(value);
                                TeamModifier.setTeamSuddendeath(arenaImpl, teamImpl, suddendeath);
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " suddendeath has been set to " + z + value + s + ".");
                                return true;
                            case "players":
                                int players;
                                try {
                                    players = Integer.valueOf(value);
                                } catch (NumberFormatException nfe){
                                    sender.sendMessage(p + r + "Expected a number.");
                                    return true;
                                }
                                if (players > 1000 || players <= 1){
                                    sender.sendMessage(p + r + "Dude, normal numbers, stop shitting me.");
                                    return true;
                                }
                                TeamModifier.setTeamPlayers(arenaImpl, teamImpl, players);
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "' " + s + " players set to " + z + players + s + ".");
                                return true;
                            case "spawntype":
                                switch (value){
                                    case "player":
                                        TeamModifier.setTeamSpawnType(arenaImpl, teamImpl, SpawnType.PLAYER);
                                        break;
                                    case "team":
                                        TeamModifier.setTeamSpawnType(arenaImpl, teamImpl, SpawnType.TEAM);
                                        break;
                                    case "public":
                                        TeamModifier.setTeamSpawnType(arenaImpl, teamImpl, SpawnType.PUBLIC);
                                        break;
                                    default:
                                        sender.sendMessage(ChatColor.RED + "/mg <arena> team <team> spawntype player|team|public");
                                        return true;
                                }
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " spawnType set to " + z + "'" + value + "'" +
                                        "" + s + ".");
                                return true;
                            case "mainstartspawn":
                                if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true")){
                                    sender.sendMessage(p + r + "Value can only be true or false!");
                                    return true;
                                }
                                boolean b = Boolean.parseBoolean(value);
                                TeamModifier.setTeamMainstartspawn(arenaImpl, teamImpl, b);
                                sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "' " + s + " main-startspawn has been set to " + z + value + " " + s + ".");
                                return true;
                            case "spawns":
                                int spawns;
                                if (teamImpl.spawntype == SpawnType.PLAYER){
                                    sender.sendMessage(p + r + "Players have fixed spawnpoints, you can't change the amount of the player spawns, change the amount of players or change their spawntype.");
                                    return true;
                                }
                                try {
                                    spawns = Integer.valueOf(value);
                                } catch (NumberFormatException nfe){
                                    sender.sendMessage(p + r + "Expected a number.");
                                    return true;
                                }
                                if (spawns > 300 || spawns <= 0){
                                    sender.sendMessage(p + r + "Dude, normal numbers, stop shitting me.");
                                    return true;
                                }
                                TeamModifier.setSpawnsAmount(arenaImpl, teamImpl, spawns);
                                SpawnType type = teamImpl.spawntype;

                                switch (type){
                                    case TEAM:
                                        sender.sendMessage(p + "Team " + z + "'" + teamImpl.name + "'" + s + " spawns amount set to " + z + spawns + s + ".");
                                        break;
                                    case PUBLIC:
                                        sender.sendMessage(p + "Public spawns amount set to " + z + spawns + s + ".");
                                        break;
                                    default:
                                        break;
                                }
                                return true;
                            default:
                                sender.sendMessage(ChatColor.RED + "/mg <arena> team <team> name|suffix|prefix|suddendeath|players|spawns|spawntype|mainstartspawn <value>");
                                return true;
                        }
                case 7:
                    name = args[0];
                    option = args[5].toLowerCase();
                    value = args[6];
                    String teamname = args[2];
                    if (arenaImpl.isEmpty()){
                        sender.sendMessage(p + r + "Arena " + ChatColor.DARK_RED + "'" + name + "'" + r + " doesn't exists!");
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("team") && args[3].equalsIgnoreCase("create")) {
                        int spawns = stringParser(value, sender, "spawns");
                        if (spawns == 0) {
                            return true;
                        }
                        int players = stringParser(args[4], sender, "players");
                        if (players == 0){
                            return true;
                        }
                        String optionalmsg = "";
                        SpawnType type;
                        try {
                            switch (option) {
                                case "player":
                                    type = SpawnType.PLAYER;
                                    spawns = players;
                                    TeamModifier.createTeam(arenaImpl, teamname, players, SpawnType.PLAYER, spawns);
                                    optionalmsg = "Each player has his own spawn, so I created " + z + players + s + " spawns for you.";
                                    break;
                                case "team":
                                    type = SpawnType.TEAM;
                                    TeamModifier.createTeam(arenaImpl, teamname, players, SpawnType.TEAM, spawns);
                                    break;
                                case "public":
                                    type = SpawnType.PUBLIC;
                                    int maxpublicspawns = TeamsModifier.publicSpawnsMaximum(arenaImpl);
                                    if (maxpublicspawns != 0) {
                                        if (spawns != maxpublicspawns)
                                            spawns = maxpublicspawns;
                                        optionalmsg = s + "There was already an other team with public spawn(s), amount is reset to " + z + maxpublicspawns + s + "!"; //TODO: dont show this message if 10=10;
                                    } else {
                                        optionalmsg = s + "Public team spawns are all automatically synchronized!";
                                    }

                                    TeamModifier.createTeam(arenaImpl, teamname, players, SpawnType.PUBLIC, spawns);
                                    break;
                                default:
                                    z = ChatColor.DARK_RED + "";
                                    s = ChatColor.RED + "";
                                    sender.sendMessage(p + ChatColor.RED + "Expected a SpawnType, choose between " + z + "player" + s + ", " + z + "team" + s + " and " + z + "public" + s + "!");
                                    return true;
                            }
                            sender.sendMessage(p + "Team " + z + "'" + teamname + "'" + s + ", with SpawnType (" + z + type + s +  ") and " + z + spawns + s + " spawns, is succesfully created! ");
                            if (!optionalmsg.isEmpty()){
                                sender.sendMessage(z + "Note: " + s + optionalmsg);
                            }

                            return true;
                        } catch (TeamAlreadyExistException taee){
                            sender.sendMessage(p + r + "We already have a team with that name!");
                            return true;
                        }
                    }
                    sender.sendMessage(ChatColor.RED + "/mg <arena> team <team> create <max players> <spawntype> <spawns>");
                    return true;

            }

            return false;
        }
        return false;
    }

    private boolean isPlayer(CommandSender sender){
        if (sender instanceof Player){
            return true;
        } else {
            sender.sendMessage(MinigameFramework.prefix + ChatColor.RED + "You've to be on the server to use this command!");
            return false;
        }
    }
    private ChatColor getChatColor(String color) {
        for(ChatColor c : ChatColor.class.getEnumConstants()) {
            if(color.equalsIgnoreCase(String.valueOf(color))) {
                return c;
            }
        }
        return null;
    }
    private int stringParser(String s, CommandSender sender, String err){
        int i = 1;
        try {
            i = Integer.valueOf(s);
        } catch (NumberFormatException nfe){
            sender.sendMessage(MinigameFramework.prefix + ChatColor.RED + "Expected a number for input \"" + err + "\".");
            i = 0;
        }
        if (i > 300 || i < 1){
            sender.sendMessage(MinigameFramework.prefix + ChatColor.RED + "Expected a normal number for input \"" + err + "\".");
            i = 0;
        }
        return i;
    }

    private void helpReference(Integer helpNumber, CommandSender sender){
        String p = MinigameFramework.prefix;
        String s = MinigameFramework.suffix;
        String z = MinigameFramework.mark;
        String b = net.md_5.bungee.api.ChatColor.DARK_AQUA + "";
        switch (helpNumber){
            case 1:
                sender.sendMessage(p + b + " Command Reference, Page 1 of 4");
                sender.sendMessage(s + "/mg arenas " + z + "Shows all arenas.");
                sender.sendMessage(s + "/mg <arena> " + z + "Shows information about the given arena.");
                sender.sendMessage(s + "/mg spawntypes " + z + "Shows all the spawntypes, and their definiton.");
                sender.sendMessage(s + "/mg mainstartspawn " + z + "Definition of boolean main-startspawn.");
                sender.sendMessage(s + "/mg <arena> blocks " + z + "Shows all registered blocks.");
                sender.sendMessage(s + "Type " + z + "/mg help 2 " + s + "for the next page.");
                break;
            case 2:
                sender.sendMessage(p + b + " Command Reference, Page 2 of 4");
                sender.sendMessage(s + "/mg <arena> create|delete|edit|save " + z + "Basic arena commands.");
                sender.sendMessage(s + "/mg <arena> setlobby|setspectator " + z + "Set the specific arena locations.");
                sender.sendMessage(s + "/mg <arena> ready " + z + "See if arena is ready to be saved.");
                sender.sendMessage(s + "/mg <arena> rename <name> " + z + "Rename the arena.");
                sender.sendMessage(s + "Type " + z + "/mg help 3 " + s + "for the next page.");
                break;
            case 3:
                sender.sendMessage(p + b + " Command Reference, Page 3 of 4");
                sender.sendMessage(s + "/mg <arena> blocks reset " + z + "Resets all blocks.");
                sender.sendMessage(s + "/mg <arena> block add|remove [number] " + z + "Blocks registration commands.");
                sender.sendMessage(s + "/mg <arena> teams " + z + "Shows all teams of the arena.");
                sender.sendMessage(s + "/mg <arena> team <team> " + z + "Information about the given team.");
                sender.sendMessage(s + "/mg <arena> team <team> create <max players> <spawntype> <spawns> " + z + "Creates a team.");
                sender.sendMessage(s + "Type " + z + "/mg help 4 " + s + "for the next page.");
                break;
            case 4:
                sender.sendMessage(p + b + " Command Reference, Page 4 of 4");
                sender.sendMessage(s + "/mg <arena> team <team> delete " + z + "Deletes a team.");
                sender.sendMessage(s + "/mg <arena> team <team> setspawn (number) " + z + "Setspawn of team, you don't need a number when you only have 1 spawn.");
                sender.sendMessage(s + "/mg <arena> team <team> name|suffix|prefix|suddendeath|players|spawns|spawntype|mainstartspawn <value> " + z + "Set values for team.");
                break;
            default:
                sender.sendMessage(p + "Whoops, that we don't recognise that number, it is only 1 - 4 .");
                break;
        }
    }

    @Override
    public String getName() {
        return command;
    }
}

