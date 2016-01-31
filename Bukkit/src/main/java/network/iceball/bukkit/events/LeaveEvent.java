package network.iceball.bukkit.events;

import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.permissions.Permissions;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Floris on 06-07-15.
 */
public class LeaveEvent implements Listener{
    private Iceball plugin;

    public LeaveEvent(Iceball plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

    }
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent event){
        Player player = event.getPlayer();
        player.getInventory().clear();
        Permissions.getInstance().removePlayer(event.getPlayer().getName());
    }
}
/* TODO: remove this
[14:29:30 WARN]:        at me.MrThunder_.iceball.data.Profile.storePlayerLeave(Profile.java:154)
[14:29:30 WARN]:        at me.MrThunder_.iceball.events.LeaveEvent.onPlayerLeave(LeaveEvent.java:28)
[14:29:30 WARN]:        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[14:29:30 WARN]:        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
[14:29:30 WARN]:        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
[14:29:30 WARN]:        at java.lang.reflect.Method.invoke(Method.java:497)
[14:29:30 WARN]:        at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:306)
[14:29:30 WARN]:        at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:62)
[14:29:30 WARN]:        at org.bukkit.plugin.SimplePluginManager.fireEvent(SimplePluginManager.java:502)
[14:29:30 WARN]:        at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:487)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.PlayerList.disconnect(PlayerList.java:348)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.PlayerConnection.a(PlayerConnection.java:871)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.NetworkManager.l(NetworkManager.java:314)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.ServerConnection.c(ServerConnection.java:145)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.MinecraftServer.B(MinecraftServer.java:813)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.DedicatedServer.B(DedicatedServer.java:374)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.MinecraftServer.A(MinecraftServer.java:653)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.MinecraftServer.run(MinecraftServer.java:556)
[14:29:30 WARN]:        at java.lang.Thread.run(Thread.java:745)
plication, increasing the server configured values for client timeouts, or using the Connector/J connection property 'autoReconnect=true' to avoid this problem.
[14:29:30 WARN]:        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
[14:29:30 WARN]:        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62)
[14:29:30 WARN]:        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45)
[14:29:30 WARN]:        at java.lang.reflect.Constructor.newInstance(Constructor.java:422)
[14:29:30 WARN]:        at com.mysql.jdbc.Util.handleNewInstance(Util.java:407)
[14:29:30 WARN]:        at com.mysql.jdbc.SQLError.createCommunicationsException(SQLError.java:1116)
[14:29:30 WARN]:        at com.mysql.jdbc.MysqlIO.send(MysqlIO.java:3348)
[14:29:30 WARN]:        at com.mysql.jdbc.MysqlIO.sendCommand(MysqlIO.java:1967)
[14:29:30 WARN]:        at com.mysql.jdbc.MysqlIO.sqlQueryDirect(MysqlIO.java:2140)
[14:29:30 WARN]:        at com.mysql.jdbc.ConnectionImpl.execSQL(ConnectionImpl.java:2626)
[14:29:30 WARN]:        at com.mysql.jdbc.PreparedStatement.executeInternal(PreparedStatement.java:2111)
[14:29:30 WARN]:        at com.mysql.jdbc.PreparedStatement.executeQuery(PreparedStatement.java:2273)
[14:29:30 WARN]:        at me.MrThunder_.iceball.data.Profile.storePlayer(Profile.java:60)
[14:29:30 WARN]:        at me.MrThunder_.iceball.data.Profile.<init>(Profile.java:33)
[14:29:30 WARN]:        at me.MrThunder_.iceball.events.JoinEvent.PlayerJoin(JoinEvent.java:30)
[14:29:30 WARN]:        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
[14:29:30 WARN]:        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
[14:29:30 WARN]:        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
[14:29:30 WARN]:        at java.lang.reflect.Method.invoke(Method.java:497)
[14:29:30 WARN]:        at org.bukkit.plugin.java.JavaPluginLoader$1.execute(JavaPluginLoader.java:306)
[14:29:30 WARN]:        at org.bukkit.plugin.RegisteredListener.callEvent(RegisteredListener.java:62)
[14:29:30 WARN]:        at org.bukkit.plugin.SimplePluginManager.fireEvent(SimplePluginManager.java:502)
[14:29:30 WARN]:        at org.bukkit.plugin.SimplePluginManager.callEvent(SimplePluginManager.java:487)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.PlayerList.onPlayerJoin(PlayerList.java:298)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.PlayerList.a(PlayerList.java:157)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.LoginListener.b(LoginListener.java:144)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.LoginListener.c(LoginListener.java:54)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.NetworkManager.a(NetworkManager.java:231)
[14:29:30 WARN]:        at net.minecraft.server.v1_8_R3.ServerConnection.c(ServerConnection.java:148)
[14:29:30 WARN]:        ... 5 more
[14:29:30 WARN]: Caused by: java.net.SocketException: Broken pipe
[14:29:30 WARN]:        at java.net.SocketOutputStream.socketWrite0(Native Method)
[14:29:30 WARN]:        at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:109)
[14:29:30 WARN]:        at java.net.SocketOutputStream.write(SocketOutputStream.java:153)
[14:29:30 WARN]:        at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
[14:29:30 WARN]:        at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)
[14:29:30 WARN]:        at com.mysql.jdbc.MysqlIO.send(MysqlIO.java:3329)
[14:29:30 WARN]:        ... 27 more
[14:29:30 INFO]: Prodyrus left the game.
===
===
=== SOLOTION for a bugg: http://stackoverflow.com/questions/2077081/connection-with-mysql-is-being-aborted-automaticly-how-to-configure-connector-j
===
[22:54:10 INFO]: Prodyrus issued server command: /profile Mr_redsheep
[22:54:10 ERROR]: null
org.bukkit.command.CommandException: Unhandled exception executing command 'profile' in plugin Iceball v0.8.0
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:46) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.command.SimpleCommandMap.dispatch(SimpleCommandMap.java:141) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at org.bukkit.craftbukkit.v1_8_R3.CraftServer.dispatchCommand(CraftServer.java:641) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnection.handleCommand(PlayerConnection.java:1162) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnection.a(PlayerConnection.java:997) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInChat.a(PacketPlayInChat.java:45) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PacketPlayInChat.a(PacketPlayInChat.java:1) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.PlayerConnectionUtils$1.run(SourceFile:13) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511) [?:1.8.0_51]
        at java.util.concurrent.FutureTask.run(FutureTask.java:266) [?:1.8.0_51]
        at net.minecraft.server.v1_8_R3.SystemUtils.a(SourceFile:44) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.B(MinecraftServer.java:714) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.DedicatedServer.B(DedicatedServer.java:374) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.A(MinecraftServer.java:653) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.MinecraftServer.run(MinecraftServer.java:556) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_51]
Caused by: libary.JSON.JSONException: JSONObject["country"] not found.
        at libary.JSON.JSONObject.get(JSONObject.java:476) ~[?:?]
        at me.MrThunder_.iceball.data.Profile.getProfile(Profile.java:199) ~[?:?]
        at me.MrThunder_.iceball.commands.ProfileCommand.onCommand(ProfileCommand.java:39) ~[?:?]
        at org.bukkit.command.PluginCommand.execute(PluginCommand.java:44) ~[spigot.jar:git-Spigot-fdc1440-53fac9f]
        ... 15 more
--------------
When a player switches from server to a other server. You get a exception, because the UUID lookup is checked the 2nd time within a minute.
Disable the UUID lookup in Minigame servers. It should online do its work in the lobby.
--- Exception coming :D
19:13:46 INFO]: Adding group permissions: |iceball.lobby|true|
[19:13:46 INFO]: Thradrys[/77.165.12.177:41740] logged in with entity id 0 at ([World]-139.58780670946433, 51.0, -622.4325373265257)
[19:13:46 INFO]: [Iceball] Found ipaddress [77.165.12.177] of player 'Thradrys'!
ies={},legacy=false]
com.mojang.authlib.exceptions.AuthenticationException: The client has sent too many requests within a certain amount of time
ac9f]
1440-53fac9f]
ot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$1.load(TileEntitySkull.java:70) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$1.load(TileEntitySkull.java:1) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LoadingValueReference.loadFuture(LocalCache.java:3524) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.loadSync(LocalCache.java:2317) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.lockedGetOrLoad(LocalCache.java:2280) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$Segment.get(LocalCache.java:2195) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache.get(LocalCache.java:3934) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache.getOrLoad(LocalCache.java:3938) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LocalLoadingCache.get(LocalCache.java:4821) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at com.google.common.cache.LocalCache$LocalLoadingCache.getUnchecked(LocalCache.java:4827) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at net.minecraft.server.v1_8_R3.TileEntitySkull$3.run(TileEntitySkull.java:172) [spigot.jar:git-Spigot-fdc1440-53fac9f]
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142) [?:1.8.0_60]
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617) [?:1.8.0_60]
        at java.lang.Thread.run(Thread.java:745) [?:1.8.0_60]





 */
