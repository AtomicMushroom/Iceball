// Package Declaration
package network.iceball.bukkit.util;

// Java Imports
import network.iceball.bukkit.Iceball;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
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

    private Iceball plugin;

    private File file;
    private YamlConfiguration yamlConfiguration;
    private final String name;

    /**
     * Constructor of SpaceConfig.
     */
    public Config(ConfigFile file, Iceball plugin) {
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
            plugin.getLogger().log(Level.SEVERE, "Could not serialize Yaml-Configuration to " + file, ex);
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

    public File getFile() {
        return file;
    }
}