package network.iceball.commands;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import network.iceball.Iceball;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Floris on 20-10-15.
 */
public class PluginCommandListener implements Listener {

    private Iceball plugin;
    private final String channel = "Iceball";
    private ChatColor Z = ChatColor.GREEN;
    private ChatColor S = ChatColor.GRAY;


    public PluginCommandListener(Iceball plugin){
        this.plugin = plugin;
        this.plugin.getProxy().registerChannel(channel);
        this.plugin.getProxy().getPluginManager().registerListener(this.plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals(channel)) {
            return;
        }
        if (!(event.getSender() instanceof Server)) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String command = in.readUTF();
        String value;
        String sender;
        ProxiedPlayer pTarget;
        ProxiedPlayer pSender;
        switch (command) {
            case "profile":
                value = in.readUTF();
                sender = in.readUTF();
                pTarget = plugin.getProxy().getPlayer(value);
                pSender = plugin.getProxy().getPlayer(sender);
                if (pSender == null){return;}
                if (pTarget != null) { //value is online
                    String server = pTarget.getServer().getInfo().getName();
                    TextComponent message = new TextComponent("Player ");
                    message.setColor(S);

                    TextComponent msg1 = new TextComponent(value);
                    msg1.setColor(Z);
                    message.addExtra(msg1);

                    TextComponent msg2 = new TextComponent(" is currently online at server ");
                    msg2.setColor(S);
                    message.addExtra(msg2);


                    TextComponent msg3 = new TextComponent(server);
                    msg3.setColor(Z);
                    msg3.setUnderlined(true);
                    msg3.setHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Teleport to ").color(S).append(value).color(Z).create()));
                    msg3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/stp " + value));
                    message.addExtra(msg3);
                    message.addExtra(new ComponentBuilder(".").color(S).create()[0]);
                    pSender.sendMessage(message);
                    break;
                } else {
                    pSender.sendMessage(new ComponentBuilder(value).color(Z).append(" is not connected to the proxyserver.").color(S).create());
                    break;
                }
            case "stp":
                value = in.readUTF();
                sender = in.readUTF();
                pTarget = plugin.getProxy().getPlayer(value);
                pSender = plugin.getProxy().getPlayer(sender);
                if (pTarget != null){
                    if(pSender != null){
                        ServerInfo server = pTarget.getServer().getInfo();
                        pSender.sendMessage(new ComponentBuilder("Teleporting to ").color(S).append(server.getName()).color(Z)
                                .append("..").color(S).create());
                        pSender.connect(server);
                        break;
                    }
                } else {
                    if (pSender != null)
                    pSender.sendMessage(new ComponentBuilder(value).color(S).append(" is not connected to the proxyserver.").color(Z).create());
                    break;
                    // sender is not online
                }
            case "lobby":
                plugin.getLogger().info("Lobby teleported test");
                sender = in.readUTF();
                pSender = plugin.getProxy().getPlayer(sender);
                if (pSender != null) {
                    new LeaveCommand().execute(pSender, null);
                    break;
                }
            default:
                break;
        }
    }

    public void sendMessage(String[] message, ServerInfo target) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        for (String s : message) {
            try {
                out.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        target.sendData(channel, stream.toByteArray());
    }


}
