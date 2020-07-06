package me.majeek.journeylands.listeners;

import me.majeek.journeylands.xpbottle.Bottle;
import me.majeek.journeylands.xpbottle.BottleCooldown;
import me.majeek.journeylands.xpbottle.ExpHandle;
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
                    else if(stringIsInt(args[0]) && (player.hasPermission("xpbottle.use") || player.isOp()))
                        xpAmount(player, args[0]);
                    else if (args[0].equalsIgnoreCase("all") && (player.hasPermission("xpbottle.use.all") || player.isOp()))
                        xpAll(player);
                    else if(args[0].equalsIgnoreCase("clear") && (player.hasPermission("xpbottle.clear") || player.isOp())) {
                        if(args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                xpClear(Bukkit.getPlayer(args[1]), player);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Invalid player name.");
                            }
                        }
                        else
                            xpClear(player, player);
                    } else if((args[0].equalsIgnoreCase("give") && args.length >= 3) && (player.hasPermission("xpbottle.give") || player.isOp())){
                        if(Bukkit.getPlayer(args[1]) != null){
                            if(stringIsInt(args[2])){
                                xpGive(Bukkit.getPlayer(args[1]), player, args[2]);
                            } else{
                                xpHelp(player);
                            }
                        } else{
                            xpHelp(player);
                        }
                    }
                    else
                        xpHelp(player);
                }
            } else {
                if ((label.equalsIgnoreCase("jl") || label.equalsIgnoreCase("journeylands")) && (player.hasPermission("jl.help") || player.isOp())){
                    jlHelp(player);
                }
                else if(label.equalsIgnoreCase("xpbottle") && (player.hasPermission("xpbottle.help") || player.isOp())) {
                    xpHelp(player);
                }
                else
                    xpHelp(player);
            }
        }
        return false;
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

    private void xpAmount(Player player, String strExp){
        int xp = Integer.parseInt(strExp);

        if(!BottleCooldown.hasCooldown(player.getUniqueId())){
            if(ExpHandle.getTotalExperience(player) != 0 && xp <= ExpHandle.getTotalExperience(player)) {
                BottleCooldown.addCooldown(player.getUniqueId());

                player.getInventory().addItem(new Bottle(player, xp).getBottle());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l- " + xp + " xp"));

                ExpHandle.setTotalExperience(player, ExpHandle.getTotalExperience(player) - xp);

                player.sendMessage(ChatColor.YELLOW + "You are now afflicated with " + ChatColor.UNDERLINE + "EXP Exhaustion" + ChatColor.RESET + ChatColor.YELLOW + " for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s.");
                player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + ChatColor.YELLOW + " use /xpbottle while EXP Exhausted.");
            } else{
                player.sendMessage(ChatColor.RED + "You do not have enough xp to convert.");
            }
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&cYou cannot create another XP Bottle for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s."));
        }
    }

    private void xpAll(Player player){
        if(!BottleCooldown.hasCooldown(player.getUniqueId())) {
            if(ExpHandle.getTotalExperience(player) != 0) {
                BottleCooldown.addCooldown(player.getUniqueId());

                player.getInventory().addItem(new Bottle(player, ExpHandle.getTotalExperience(player)).getBottle());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l- " + ExpHandle.getTotalExperience(player) + " xp"));

                ExpHandle.setTotalExperience(player, 0);

                player.sendMessage(ChatColor.YELLOW + "You are now afflicated with " + ChatColor.UNDERLINE + "EXP Exhaustion" + ChatColor.RESET + ChatColor.YELLOW + " for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s.");
                player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + ChatColor.YELLOW + " use /xpbottle while EXP Exhausted.");
            } else{
                player.sendMessage(ChatColor.RED + "You do not have enough xp to convert.");
            }
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&cYou cannot create another XP Bottle for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s."));
        }
    }

    private void xpClear(Player player, Player sender){
        BottleCooldown.removeCooldown(player.getUniqueId());

        sender.sendMessage(ChatColor.GREEN + "You have removed " + player.getName() + "'s cooldown.");
    }

    private void xpGive(Player player, Player sender, String strExp){
        int xp = Integer.parseInt(strExp);

        player.getInventory().addItem(new Bottle(sender, xp).getBottle());

        sender.sendMessage(ChatColor.GREEN + "You have gave " + player.getName() + " " + xp + " xp");
    }

    private boolean stringIsInt(String string){
        return string.matches("-?\\d+");
    }
}
