package network.iceball.bukkit.util;

/**
 * Created by Floris on 06-01-16.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public enum ConfigFile {
    config_yml ("config.yml");

    final String name;

    ConfigFile(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}