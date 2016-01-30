package net.iceball.mgfw.impl.game;

import net.iceball.mgfw.api.game.config.MGConfiguration;

/**
 * Created by Floris on 25-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public abstract class ConfigBuilder {

    public abstract MGConfiguration getConfiguration();
    public GameSettings build(){
        return new GameSettings(getConfiguration());
    }


}
