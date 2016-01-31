package network.iceball.bukkit.events.gui;

/**
 * Created by Floris on 19-07-15.
 */
public enum GUIStage{
    CLEAR(0),
    GAMESELECTOR(5),
    PROFILE(25),
    ACHIEVEMENTS(30),
    PARTICLESELECTOR(31),
    SHOP_CHOOSE_SHOP(10),
    SHOP_VANITYSHOP(15),
    SHOP_VANITYSHOP_COLORSHOP(16),
    SHOP_MENUSHOP(20);

    private int Stage = 0;
    GUIStage(int value){
        this.Stage = value;
    }
}
