package network.iceball_bc.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Map;

/**
 * Created by Floris on 10-12-15.
 * Email: florisgra@gmail.com
 * <p/>
 * Magic. Do not touch.
 */
public class LeaveCommand extends Command {

    public LeaveCommand() {
        super("leave");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args == null){ args = new String[0];}
        if (args.length != 0){
            sender.sendMessage(new ComponentBuilder("/leave").color(ChatColor.RED).create());
            return;
        }
        if (sender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (player.getServer().getInfo().getName().contains("lobby")){
                player.sendMessage(new ComponentBuilder("You already are on a lobby server!").color(ChatColor.RED).create());
                return;
            }
            ServerInfo server = getLobbyServer();
            if (server != null) {
                player.connect(server);
                return;
            }

            player.sendMessage(new ComponentBuilder("There were no lobby servers found to connect you to!").color(ChatColor.RED).create());
        }
    }

    /**
     * Gets one lobby server!
     * @return online lobby server
     */
    private ServerInfo getLobbyServer(){
        ServerInfo server = null;
        for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()){
            if (entry.getKey().contains("lobby")){
                server = entry.getValue();
                if (entry.getValue().getPlayers().size() < 50){
                    return entry.getValue();
                }
            }
        }
        return server;
    }
}
