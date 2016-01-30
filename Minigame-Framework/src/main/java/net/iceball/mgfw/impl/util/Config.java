package net.iceball.mgfw.impl.util;

import net.iceball.mgfw.impl.MinigameFramework;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * A class that handles the configuration file.
 *
 * @author iffa
 * @author Pandarr
 * @author Sammy
 * @author kitskub
 * @author Thradrys
 */
public class Config {

    private boolean loaded = false;

    private MinigameFramework plugin;

    private File file;
    private YamlConfiguration yamlConfiguration;
    private final String name;

    /**
     * Constructor of SpaceConfig.
     */
    public Config(ConfigFile file, MinigameFramework plugin) {
        this.name = file.getName();
        this.plugin = plugin;
        loadConfig();
    }

    /**
     * Gets the configuration file.
     *
     * @return the loaded config
     */
    public YamlConfiguration getConfig() {
        if (!loaded) {
            //loadConfig(plugin); //throws NPE when plugin is null
            loadConfig();
        }
        return yamlConfiguration;
    }

    /**
     * Loads the configuration file from the .jar.
     */
    private void loadConfig() {
        file = new File(plugin.getDataFolder(), name);
        if (file.exists()) {
            yamlConfiguration = new YamlConfiguration();
            try {
                yamlConfiguration.load(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
            }
            loaded = true;
        } else {
            try {
                plugin.getDataFolder().mkdir();
                InputStream jarURL = Config.class.getResourceAsStream("/" + name);
                copyFile(jarURL, file);
                yamlConfiguration = (new YamlConfiguration());
                yamlConfiguration.load(file);
                //loaded = true;
            } catch (Exception e) {
                // This is not good, I know. At least I didn't wrote this.
                //copyFile method does this, I cannot help it sorry.
            }
        }
    }

    public void saveConfig() {
        if (yamlConfiguration == null || file == null) {
            return;
        }
        try {
            yamlConfiguration.save(file);
        } catch (IOException ex) {
            MinigameFramework.logger.log(Level.SEVERE, "Could not serialize Yaml-Configuration to " + file, ex);
        }
    }

    /**
     * Copies a file to a new location.
     *
     * @param in  InputStream
     * @param out File
     * @throws Exception
     */
    private void copyFile(InputStream in, File out) throws Exception {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * Get a list of arena names.
     *
     * @return List names of arena.
     */
    public Set<String> arenasList() {
        Set<String> arenas = new HashSet<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection("arenas")) {
            return arenas;
        }
        for (String key : config.getConfigurationSection("arenas").getKeys(false)) {
            arenas.add(key);
        }
        return arenas;
    }

    /**
     * @param name Name of arena
     * @return 0, or the highest number block of the arena.
     */
    public int getHighestBlock(String name) {
        Set<String> block = new HashSet<>();
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.getString("arenas." + name + "blocks.B1") == null) {
            return 0;
        }
        for (String key : config.getConfigurationSection("arenas." + name + ".blocks").getKeys(false)) {
            block.add(key);
        }
        return block.size();
    }

    /**
     * @param name Name of arena
     * @return Set of Teamnames or a empty set, when the path is null.
     */
    public Set<String> getTeamNames(String name) {
        Set<String> teamnames = new HashSet<>();
        String path = "arenas." + name + ".teams"; //arenas.castle.teams
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.isConfigurationSection(path)) {
            return teamnames;
        }
        for (String key : config.getConfigurationSection(path).getKeys(false)) {
            teamnames.add(key);
        }
        return teamnames;
    }

    /**
     * Gets Map Information of Team Instanec from yamlConfiguration
     *
     * @param arena     Name of Arena
     * @param team_name Name of Team
     * @return Map<String,Object> map info about team
     */
    public Map<String, Object> getTeam(String arena, String team_name) {
        String path = "arenas." + arena + ".teams." + team_name;
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        return config.getConfigurationSection(path).getValues(true);
    }

    public void deleteArena(String name) {
        if (yamlConfiguration.isSet("arenas." + name)) {
            yamlConfiguration.set("arenas." + name, null);
            saveConfig();
        }
    }
    public File getFile() {
        return file;
    }
}

