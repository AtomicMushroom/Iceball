package network.iceball.bukkit.events.gui;

import network.iceball.bukkit.events.gui.events.ClickEvent;
import network.iceball.bukkit.events.gui.items.PlayerGUI;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.events.gui.events.PlayerDrop;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Floris on 19-07-15.
 */
public class GUI {

    private Iceball plugin;
    public static HashMap<String, GUIStage> guiStageHashMap = new HashMap<String, GUIStage>();

    public GUI(Iceball plugin, final Player player) {
        this.plugin = plugin;
        guiStageHashMap.put(player.getName(), GUIStage.CLEAR);
        new PlayerGUI(plugin, player);
    }
    public GUI(Iceball plugin){
        this.plugin = plugin;
        new ClickEvent(plugin);
        new PlayerDrop(plugin);
    }

}
