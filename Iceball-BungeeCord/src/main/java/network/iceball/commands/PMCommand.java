package network.iceball.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashSet;

/**
 * Created by Floris on 10-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public class PMCommand extends Command {

    public static HashSet<String> filter;
    public PMCommand() {
        super("pm");
        filter = new HashSet<>();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args == null){ args = new String[0];}
        if (1 > args.length){
            sender.sendMessage(new ComponentBuilder("/pm <player> <message>").color(ChatColor.RED).create());
        } else if (args.length >= 2) {
            String target = args[0];
            ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(target);
            if (receiver != null) {
                if (receiver.getName().equals(sender.getName())) {
                    sender.sendMessage(new ComponentBuilder("Dude, there are much better things you could do " +
                            "than sending messages to yourself. Just sayin'").color(ChatColor.RED).create());
                    return;
                } else
                sender.sendMessage(messageBuilder(sender.getName(), receiver.getName(), args, Target.SENDER));
                filter.add(sender.getName());
                if (!filter.contains(receiver.getName())) {
                    receiver.sendMessage(new ComponentBuilder("You've got a private message. You can reply with /pm <player> <message>")
                            .color(ChatColor.RED).create());
                    filter.add(receiver.getName());
                } else
                    receiver.sendMessage(messageBuilder(sender.getName(), receiver.getName(), args, Target.RECEIVER));
            } else {
                sender.sendMessage(new ComponentBuilder("Player '").color(ChatColor.RED).append(target).append("' is not online!").color(ChatColor.RED).create());
            }
        }
    }

    private BaseComponent[] messageBuilder(String sender, String receiver, String[] args, Target target){
        if (args == null){ args = new String[2]; }
        StringBuilder builder = new StringBuilder();
        for (int i=1; i < args.length; i++) {
            builder.append(" ").append(args[i]);
        }
        String message = builder.toString();
        switch (target){
            case SENDER:
                return new ComponentBuilder(sender).color(ChatColor.LIGHT_PURPLE).append(" -> ").color(ChatColor.WHITE)
                        .append(receiver).color(ChatColor.DARK_PURPLE).append(": ").color(ChatColor.BLACK)
                        .append(message).color(ChatColor.GOLD).create();
            case RECEIVER:
                return new ComponentBuilder(sender).color(ChatColor.DARK_PURPLE).append(": ").color(ChatColor.BLACK)
                        .append(message).color(ChatColor.GOLD).create();
            default:
                return null;
        }
    }
}
enum Target {
    SENDER,
    RECEIVER
}
