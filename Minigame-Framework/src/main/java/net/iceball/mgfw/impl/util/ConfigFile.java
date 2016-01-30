package net.iceball.mgfw.impl.util;

/**
 * Created by Floris on 06-01-16.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public enum ConfigFile {
    arenas_yml ("arenas.yml"),
    config_yml ("config.yml"),
    signs_yml  ("signs.yml");

    final String name;

    ConfigFile(String s) {
        name = s;
    }

    public String getName() {
        return this.name;
    }
}