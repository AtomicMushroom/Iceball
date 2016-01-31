package net.iceball.mgfw.impl.game.signs;

/**
 * Created by Floris on 03-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
enum Status {

    SEARCHING_GAMES(0),
    SEARCHING_SERVERS(0),
    JOIN(1),
    FULL(2),
    NEW(3);

    final int i;

    Status(int i) {
        this.i = i;
    }
    public int getI() {
        return i;
    }
}
