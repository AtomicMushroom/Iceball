package network.iceball.bukkit;

import network.iceball.bukkit.api.DatabaseProvider;
import network.iceball.bukkit.commands.CommandManager;
import network.iceball.bukkit.events.EventManager;
import network.iceball.bukkit.permissions.Permissions;
import network.iceball.bukkit.util.Config;
import network.iceball.bukkit.util.ConfigFile;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Floris on 09-04-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 */
public final class Iceball extends JavaPlugin {
    /*
 What has to be done?
 - Make an special lobby scoreboard :D
 - Commands:
     - Commmand that need a refresh:

        - IsLobbyCommand doesn't work properly, because item dropping is still enabled and the GUI also.
    */
    private Iceball plugin;

    public static final String pluginPrefix = ChatColor.DARK_GRAY + "[" +ChatColor.AQUA+"Iceball" + ChatColor.DARK_GRAY+ "]" + ChatColor.BLACK+ ": " + ChatColor.DARK_AQUA;
    public static final String pluginSuffixMARK = ChatColor.GOLD + "";
    public static final String pluginSuffixNRML = ChatColor.DARK_AQUA + "";
    public static final String pluginLog = ChatColor.stripColor(pluginPrefix).replace(',',' ');
    public static String channel = "Iceball";
    //public static Permissions permissions;
    public static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        DatabaseProvider.disconnect();
    }

    @Override
    public void onEnable() {
        plugin = this;
        long startTime = System.nanoTime();
        boolean isDBEnabled = new Config(ConfigFile.config_yml, plugin).getConfig().getBoolean("Iceball.mysql.enabled");
        if (!isDBEnabled) {
            this.getServer().getLogger().log(Level.SEVERE, "[Iceball] Database is disabled, enable it in the config.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        DatabaseProvider.connect(plugin);
        new EventManager(this);
        new CommandManager(this);
        Permissions.getInstance().load(plugin);


        long endTime = System.nanoTime();
        long duration = (endTime - startTime)/100000000;  //divide by 1000000 to get seconds.
        plugin.getServer().getLogger().info("[Iceball] Done! (" + duration + "s)!");
    }
    /*
    [16:44:46 INFO]: Mr_RedSheep[/81.206.208.202:48524] logged in with entity id 58427 at ([World]-116.11679022134294, 206.0, -41.76767120726933)
[16:44:56 INFO]: Mr_RedSheep issued server command: /iplookup Thradrys
[16:44:58 INFO]: Thradrys issued server command: /p reload
[16:44:58 ERROR]: null
org.bukkit.command.CommandException: Unhandled exception executing command 'p' in plugin Iceball v0.7
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:46) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.command.SimpleCommandMap.dispatch(SimpleCommandMap.java:141) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.craftbukkit.v1_8_R3.CraftServer.dispatchCommand(CraftServer.java:641) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnection.handleCommand(PlayerConnection.java:1162) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnection.a(PlayerConnection.java:997) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInChat.a(PacketPlayInChat.java:45) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInChat.a(PacketPlayInChat.java:1) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnectionUtils$1.run(SourceFile:13) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [?:1.8.0_66]
        at java.util.concurrent.FutureTask.run(FutureTask.java:266) [?:1.8.0_66]
        at net.minecraft.server.v1_8_R3.SystemUtils.a(SourceFile:44) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.B(MinecraftServer.java:714) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.DedicatedServer.B(DedicatedServer.java:374) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.A(MinecraftServer.java:653) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.run(MinecraftServer.java:556) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_66]
Caused by: java.lang.NullPointerException
        at GroupPerms.update(GroupPerms.java:111) ~[?:?]
        at GroupPerms.addOfflinePermissions(GroupPerms.java:95) ~[?:?]
        at GroupPerms.addOfflinePermissions(GroupPerms.java:88) ~[?:?]
        at GroupManager.setupInheritance(GroupManager.java:99) ~[?:?]
        at GroupManager.reload(GroupManager.java:66) ~[?:?]
        at GroupManager.<init>(GroupManager.java:37) ~[?:?]
        at Permissions.reload(Permissions.java:47) ~[?:?]
        at network.iceball_bk.commands.PCommand.onCommand(PCommand.java:83) ~[?:?]
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:44) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        ... 15 more
[16:45:00 INFO]: Mr_RedSheep issued server command: /iplookup Mr_RedSheep
     */
}
