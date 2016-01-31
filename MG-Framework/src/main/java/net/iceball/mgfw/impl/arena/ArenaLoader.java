package net.iceball.mgfw.impl.arena;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.arena.exceptions.ArenaAlreadyExistException;
import net.iceball.mgfw.impl.util.BaseCommand;
import net.iceball.mgfw.impl.util.Config;
import net.iceball.mgfw.impl.util.ConfigFile;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h1>ArenaLoader</h1>
 * Created by Floris on 22-07-15.
 *
 * This class keeps watch on all the arenas.
 * <b>Note: </b> This class doesn't start the games! It only keeps the arenas saved at runtime.
 * */
public final class ArenaLoader {

    /* List that contains all arenas! */
    private HashSet<ArenaImpl> arenaImpls = new LinkedHashSet<>(); //We don't need a HashMap, it is overrated
    // and the name is already defined in the arena
    private static final ArenaLoader instance = new ArenaLoader(); // Runtime initialization

    protected ArenaLoader() { /* We don't want more than 1 instance */ }

    public static ArenaLoader getLoader() {
        return instance; // By default ThreadSafe
    }

    /**
     * Gets the arena with the specific name.
     *
     * @param name of Arena
     * @return Always an arena instance. If the instance was not found, an empty one is been set.
     * To check if the arena is empty, use the {@link ArenaImpl#isEmpty()} method.
     */
    public @NotNull ArenaImpl getArena(String name) {
        ArenaImpl arenaImpl = new ArenaImpl();
        for (ArenaImpl temp : arenaImpls) {
            if (temp.name.equalsIgnoreCase(name)) {
                arenaImpl = temp;
            }
        }
        return arenaImpl;
    }

    /**
     * Deserialize all the arenas, and load them back in the arena list.
     *
     * @param plugin {@link MinigameFramework} plugin
     */
    public void load(MinigameFramework plugin) {
        ConfigurationSerialization.registerClass(TeamImpl.class);
        Config config = new Config(ConfigFile.arenas_yml, plugin);
        Set<String> arenas = config.arenasList();
        if (arenas.isEmpty()) {
            MinigameFramework.logger.info(MinigameFramework.log + "No arenas found!");
            return;
        }
        for (String name : arenas) {
            MinigameFramework.logger.info(MinigameFramework.log + "Loading " + name + "..");
            try {
                ArenaLoader.getLoader().add(name, ArenaImpl.deserialize(name, plugin));
                MinigameFramework.logger.info(MinigameFramework.log + "Loaded " + name + "!");
            } catch (ArenaAlreadyExistException e) {
                MinigameFramework.logger.info(MinigameFramework.log + "Yeah, you fucked up the config did you? I only allow unique names. \n ");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            } catch (NullPointerException npe) {
                MinigameFramework.logger.info(MinigameFramework.log + "Yeah, you fucked up the config did you? Fix it dude. \n ");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

    /**
     * Get all arenas names!
     *
     * @return List of all arenas names that where set.
     */
    public List<String> getArenasNames() {
        return arenaImpls.stream().map(arena -> arena.name).collect(Collectors.toList());
    }

    /**
     * Adds an new Arena to the arenas list.
     *
     * @param name      of Arena instance
     * @param arenaImpl Arena instance to be added
     * @throws ArenaAlreadyExistException when the Arena name is already in use.
     */
    protected void add(String name, ArenaImpl arenaImpl) throws ArenaAlreadyExistException {
        List<String> arenasNames = getArenasNames();
        for (String temp : arenasNames) {
            if (name.equalsIgnoreCase(temp)) {
                throw new ArenaAlreadyExistException("Arena name is already in use!");
            }
        }
        arenaImpls.add(arenaImpl);
    }

    /**
     * Adds the arena to the list, no matter what. If the arena name is already in use, the arena will be overwritten.
     *
     * @param arenaImpl Arena instance to be overwritten.
     */
    protected void overwrite(ArenaImpl arenaImpl) {
        List<String> arenasNames = getArenasNames();
        arenasNames.stream().filter(temp -> temp.equalsIgnoreCase(arenaImpl.name)).forEach(temp -> {
            arenaImpls.remove(getArena(arenaImpl.name));
        });
        arenaImpls.add(arenaImpl);
    }

    /**
     * Tries to remove a specific arena from the ArenaLoader by name, can be null.
     * This method does not delete the arena from the config!
     * Have a look at {@link ArenaModifier#deleteArena(ArenaImpl)} method.
     *
     * @param name The name of the arena that will be deleted.
     */
    protected void removeArena(@Nullable String name) {
        if (name == null) {
            return;
        }
        arenaImpls.remove(getArena(name));
    }

    public Set<BaseCommand> getCommands(MinigameFramework plugin) {
        Set<BaseCommand> list = new LinkedHashSet<>();
        list.add(new ATPCommand(plugin));
        list.add(new MGCommand(plugin));
        return list;
    }
}