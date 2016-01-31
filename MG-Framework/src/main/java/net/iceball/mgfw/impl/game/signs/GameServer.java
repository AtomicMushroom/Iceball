package net.iceball.mgfw.impl.game.signs;

/**
 * Created by Floris on 03-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
class GameServer {

    boolean multimode;
    String server;
    String game;

    public boolean isMultimode() {
        return multimode;
    }

    public String getServer() {
        return server;
    }

    public String getGame() {
        return game;
    }

    public GameServer(boolean multimode, String server, String game) {
        this.multimode = multimode;
        this.server = server;
        this.game = game;
    }
}
