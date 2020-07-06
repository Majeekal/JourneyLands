package me.majeek.journeylands.listeners;

import me.majeek.journeylands.xpbottle.Bottle;
import me.majeek.journeylands.xpbottle.BottleCooldown;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandListener implements CommandExecutor {
    private HashMap<String, Runnable> commands = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length > 0) {
                if(label.equalsIgnoreCase("jl") || label.equalsIgnoreCase("journeylands")){
                    if(args[0].equalsIgnoreCase("help") && (player.hasPermission("jl.help") || player.isOp()))
                        jlHelp(player);

                } else if (label.equalsIgnoreCase("xpbottle")) {
                    if (args[0].equalsIgnoreCase("help") && (player.hasPermission("xpbottle.help") || player.isOp()))
                        xpHelp(player);
                    else if (args[0].equalsIgnoreCase("all") && (player.hasPermission("xpbottle.all") || player.isOp()))
                        xpAll(player);
                    else
                        noPermission(player);
                }
            } else {
                if ((label.equalsIgnoreCase("jl") || label.equalsIgnoreCase("journeylands")) && (player.hasPermission("jl.help") || player.isOp())){
                    jlHelp(player);
                }
                else if(label.equalsIgnoreCase("xpbottle") && (player.hasPermission("xpbottle.help") || player.isOp())) {
                    xpHelp(player);
                }
                else
                    noPermission(player);
            }
        }
        return false;
    }

    private void noPermission(Player player){
        player.sendMessage(ChatColor.RED + "You don't have permission for that command!");
    }

    private void jlHelp(Player player){
        player.sendMessage(ChatColor.GRAY + "--------------" + " [ " + ChatColor.RESET + ChatColor.GREEN + "Journey Lands" + ChatColor.GRAY + " ] " + "-------------------");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/journeylands" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Displays this.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Experience Bottle commands.");

    }

    private void xpHelp(Player player){
        player.sendMessage(ChatColor.GRAY + "--------------" + " [ " + ChatColor.RESET + ChatColor.GREEN + "Experience Bottle" + ChatColor.GRAY + " ] " + "-------------------");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle help" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Displays this.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle all" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Withdraws all of your exp points into an exp bottle.");
    }

    private void xpAll(Player player){
        if(!BottleCooldown.hasCooldown(player.getUniqueId())) {
            BottleCooldown.addCooldown(player.getUniqueId());

            player.getInventory().addItem(new Bottle(player, player.getExp()).getBottle());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l- " + Math.round(player.getExp()) + " xp"));
            player.setExp(0);

            player.sendMessage(ChatColor.YELLOW + "You are now afflicated with " + ChatColor.UNDERLINE + "EXP Exhaustion" + ChatColor.RESET + ChatColor.YELLOW + " for " + BottleCooldown.getCooldown(player.getUniqueId()) + " seconds.");
            player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.UNDERLINE + "cannot " + ChatColor.RESET + ChatColor.YELLOW + "use /xpbottle or teleport with EXP Exhausted.");
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&cYou cannot create another XP Bottle for " + BottleCooldown.getCooldown(player.getUniqueId()) + " seconds."));
        }
    }
}
