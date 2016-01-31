package network.iceball.bukkit.commands;

import network.iceball.bukkit.exceptions.TooBigNumberException;
import network.iceball.bukkit.Iceball;
import network.iceball.bukkit.data.Coins;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Floris on 06-07-15.
 * Email: florisgra@gmail.com
 *
 * Magic. Do not touch.
 *
 */
class CoinsCommand implements CommandExecutor {

    private Iceball plugin;

    public CoinsCommand(Iceball plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arguments) {
        if (cmd.getName().equalsIgnoreCase("coins")) {
            String target;
            int amount;
            String format;

            if ((!(sender instanceof Player)) && arguments.length == 0) {
                sender.sendMessage(Coins.coinsPrefix + "This command can only be run by players!");
                return true;
            } else if (arguments.length == 0) {
                Player p = (Player) sender;
                sender.sendMessage(Coins.coinsPrefix + "Balance: " + ChatColor.YELLOW + Coins.getCoins(p.getName()) + Coins.coinsSuffix + " Coins!");
                return true;

            } else if (arguments.length == 1){

                if (hasPermission(sender, "iceball.coins.seeothers")){
                target = arguments[0];
                amount = Coins.getCoins(target);
                format = formatName(target);
                sender.sendMessage(Coins.coinsPrefix + ChatColor.YELLOW + format + " has " + ChatColor.YELLOW + amount + " Coins!");
                return true;
                }
                return true;
            } else if (arguments.length == 3) {
                try {
                    amount = stringToInteger(arguments[2]);
                } catch (TooBigNumberException e) {
                    sender.sendMessage(Coins.coinsPrefix + ChatColor.RED + "The number you just gave, is way too big!");
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage(Coins.coinsPrefix + ChatColor.RED + "The amount must be in numbers!");
                    return true;
                }
                String arg = arguments[1].toUpperCase();
                target = arguments[0];
                format = formatName(target);

                switch (arg) {
                    case "ADD":
                        if (!hasPermission(sender, "iceball.coins.add")){
                            return true;
                        }
                        Coins.addCoins(target, amount);
                        sender.sendMessage(Coins.coinsPrefix + "Added " + ChatColor.YELLOW + amount + Coins.coinsSuffix +" coins to " + ChatColor.YELLOW + format + " account!");
                        break;
                    case "REMOVE":
                        if (!hasPermission(sender, "iceball.coins.remove")){
                            return true;
                        }
                        Coins.addCoins(target, -amount);
                        sender.sendMessage(Coins.coinsPrefix + "Removed " + ChatColor.YELLOW + amount + Coins.coinsSuffix +" coins of " + ChatColor.YELLOW + format + " account!");
                        break;
                    case "SET":
                        if (!hasPermission(sender, "iceball.coins.set")){
                            return true;
                        }
                        Coins.setCoins(target, amount);
                        sender.sendMessage(Coins.coinsPrefix + ChatColor.YELLOW + amount + Coins.coinsSuffix +" Coins have been set in " + ChatColor.YELLOW + format + " account!");
                        break;
                }
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private int stringToInteger(String s) throws TooBigNumberException, NumberFormatException {
        long i;
        try {
            i = Long.valueOf(s);
            if (i > 21474836){
                throw new TooBigNumberException("Number is too big!");
            }
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Not a number!");
        }
        return (int) i;
    }

    private String formatName(String name){
        if (name.endsWith("s")){
            name = name + "'"  + Coins.coinsSuffix;
            return name;
        } else {
            name = name + "'s" + Coins.coinsSuffix;
            return name;
        }
    }
    private static boolean hasPermission(CommandSender sender, String perms) {
        if (!(sender instanceof Player)) {
            return true; //console always must have access
        }
        Player player = (Player) sender;
        if (player.hasPermission(perms) || player.isOp()) {
            return true;
        } else {
            sender.sendMessage(Coins.coinsPrefix + ChatColor.RED + "You don't have " + perms + "!");
            return false;
        }
    }
}

