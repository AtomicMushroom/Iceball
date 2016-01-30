package net.iceball.mgfw.impl.arena;

import net.iceball.mgfw.impl.MinigameFramework;
import net.iceball.mgfw.impl.util.BaseCommand;
import net.iceball.mgfw.impl.util.ObjectInfo;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 20-09-15.
 */
class ATPCommand implements BaseCommand {

    private MinigameFramework plugin;
    private final String command = "atp";

    ATPCommand(MinigameFramework plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(command)) {
            if (!(args.length == 1 || args.length == 2)){
                return false;
            }
            String name = args[0];
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ArenaImpl arenaImpl = ArenaLoader.getLoader().getArena(name);
                if (arenaImpl.isEmpty()){
                    player.sendMessage(MinigameFramework.prefix + ChatColor.RED + "Arena " + name + " doesn't exist!");
                    return true;
                }
                if (args.length == 2){
                    if (args[1].toLowerCase().equals("lobby")){
                        if (arenaImpl.getLobby() != null){
                            player.sendMessage(MinigameFramework.prefix + "Teleporting you to " + MinigameFramework.mark + ObjectInfo.blockDescription(arenaImpl.getLobby()) + MinigameFramework.suffix + ".");
                            player.teleport(arenaImpl.getLobby());
                            return true;
                        }
                    } else { return false; }
                }

                if (arenaImpl.getSpectator() != null){
                    player.sendMessage(MinigameFramework.prefix + "Teleporting you to " + MinigameFramework.mark + ObjectInfo.blockDescription(arenaImpl.getSpectator()) + MinigameFramework.suffix + ".");
                    Location l = arenaImpl.getSpectator();
                    //l = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                    player.teleport(l);
                    return true;
                } else {
                    player.sendMessage(MinigameFramework.prefix + ChatColor.RED + "Spectator location was not set!");
                    return true;
                }
            }
            sender.sendMessage(MinigameFramework.prefix + ChatColor.RED + "You have to be player for this commmand!");
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return command;
    }
}
