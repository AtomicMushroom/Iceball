package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.arena.exceptions.ArenaAlreadyExistException;
import net.iceball.mgfw.impl.arena.exceptions.ArenaNullException;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import net.iceball.mgfw.impl.util.ObjectInfo;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Floris on 23-07-15.
 *
 * What does this class do?
 *
 * This class is full with static methods and allows editing an arena.
 * @see ArenaImpl class.
 */
class ArenaModifier {

    /**
     * This class is used to give all information about at runtime, to see what is missing.
     * It is used in the arena setup command.
     *
     * @param arenaImpl Arena instance
     * @return List containing all info about the arena.
     */
    public static List<String> arenaInfo(ArenaImpl arenaImpl) {
        List<String> list = new ArrayList<String>();
        String s = MinigameFramework.suffix;
        String m = MinigameFramework.mark;
        list.add(s + "Arena: " + m + arenaImpl.name);
        list.add(s + "Lobby: " + m + ObjectInfo.locationDescription(arenaImpl.lobby));
        list.add(s + "Spectator: " + m + ObjectInfo.locationDescription(arenaImpl.spectator));
        list.add(s + "Blocks:");
        List<String> blocks = getBlocks(arenaImpl);
        for (int i = 0; i < blocks.size(); i++) {
            list.add(s + "B" + (i + 1) + ": " + m + blocks.get(i));
        }
        if (blocks.isEmpty()) {
            list.add(s + " - ");
        }
        list.add(TeamsModifier.teamsInfo(arenaImpl.teams));
        for (int i = 0; i < list.size(); i++) {
            String old = list.get(i);
            String newString = old.replace("null", "Not defined yet.");
            list.set(i, newString);
        }
        return list;
    }

    /**
     * Renames the arena!
     *
     * @param arenaImpl The arena that will be renamed.
     * @param newName   The new name of the arena.
     * @throws ArenaAlreadyExistException When the arena name already exist!
     */
    public static void renameArena(ArenaImpl arenaImpl, String newName, MinigameFramework plugin) throws ArenaAlreadyExistException {
        deleteArena(arenaImpl, plugin);
        ArenaImpl newArenaImpl = new ArenaImpl(newName, arenaImpl.getLobby(),
                arenaImpl.getSpectator(), arenaImpl.blocks, arenaImpl.teams);
        ArenaLoader.getLoader().add(newArenaImpl.name, newArenaImpl);
    }

    /**
     * Deletes the arena completely, from the ArenaLoader and from the config.
     * Shouldn't be confused with {@link ArenaLoader#removeArena(String)} method!
     *
     * @param arenaImpl Arena instance that will be removed
     */
    public static void deleteArena(ArenaImpl arenaImpl, MinigameFramework plugin) {
        ArenaLoader.getLoader().removeArena(arenaImpl.name);
        Config config = new Config(ConfigFile.arenas_yml, plugin);
        config.deleteArena(arenaImpl.name);
        config.saveConfig();
    }

    /**
     * Checks if the arena is not missing variables that are not set.
     *
     * @param arenaImpl Arena that will be checked.
     * @return True if the arena is ready to be saved, false if the arena is not complete.
     */
    public static boolean isArenaReady(ArenaImpl arenaImpl) {
        if (arenaImpl.name.isEmpty() || arenaImpl.lobby == null || arenaImpl.spectator == null || arenaImpl.blocks == null || arenaImpl.getTeams() == null) {
            return false;
        }
        boolean teamsReady = false;
        for (TeamImpl teamImpl : arenaImpl.teams) {
            if (TeamModifier.isTeamReady(teamImpl)) {
                teamsReady = true;
            } else {
                return false;
            }
        }
        return teamsReady;
    }

    /**
     * Save the arena to config!
     *
     * @param arenaImpl Arena that will be saved
     * @throws ArenaNullException When the arena was not ready to be saved! You can prevent it, by using the {@link #isArenaReady(ArenaImpl)} method.
     */
    public static void saveArena(ArenaImpl arenaImpl, MinigameFramework plugin) throws ArenaNullException {
        arenaImpl.serialize(plugin);
        //new Config(ConfigFile.arenas_yml);
    }

    /**
     * Adds a new block to the block list of the arena
     *
     * @param arenaImpl Arena instance
     * @param block     Location to be added
     */
    public static void addBlock(ArenaImpl arenaImpl, Location block) {
        arenaImpl.blocks.add(block);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Removes a block of the list of the arena
     *
     * @param arenaImpl Arena instance
     * @param block     Location to gets to be removed
     */
    public static void removeBlock(ArenaImpl arenaImpl, Location block) {
        arenaImpl.blocks.remove(block);
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Removes a block from the block list of the arena
     *
     * @param arenaImpl Arena instance
     * @param index     index of block, starts at 0.
     */
    public static void removeBlock(ArenaImpl arenaImpl, int index) {
        if (index >= 0 && index < arenaImpl.blocks.size()) {
            arenaImpl.blocks.remove(index);
            ArenaLoader.getLoader().overwrite(arenaImpl);
        }
    }

    /**
     * Resets all blocks in the arena block list
     *
     * @param arenaImpl Arena instance
     */
    public static void resetBlocks(ArenaImpl arenaImpl) {
        arenaImpl.blocks = new ArrayList<>();
        ArenaLoader.getLoader().overwrite(arenaImpl);
    }

    /**
     * Gets a list Strings containing information of all blocks of the arena.
     *
     * @param arenaImpl Arena instance
     * @return Text of blocks with format: "X:%d, Y:%d, Z:%d" or an empty list
     */
    public static List<String> getBlocks(ArenaImpl arenaImpl) {
        List<String> list = new ArrayList<>();
        for (Location block : arenaImpl.blocks) {
            list.add(String.format("X:%d, Y:%d, Z:%d", block.getBlockX(), block.getBlockY(), block.getBlockZ()));
        }
        return list;
    }
}
