package me.majeek.journeylands.listeners;

import me.majeek.journeylands.files.XPBottleConfig;
import me.majeek.journeylands.xpbottle.Bottle;
import me.majeek.journeylands.xpbottle.BottleCooldown;
import me.majeek.journeylands.xpbottle.ExpHandle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
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
                    else if (args[0].equalsIgnoreCase("all") && (player.hasPermission("xpbottle.all") || player.isOp()))
                        xpAll(player);
                    else if(args[0].equalsIgnoreCase("clear") && (player.hasPermission("xpbottle.clear") || player.isOp())) {
                        if(args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                xpClear(Bukkit.getPlayer(args[1]), player);
                            } else {
                                sender.sendMessage(ChatColor.RED + "Invalid player name.");
                            }
                        } else {
                            xpClear(player, player);
                        }
                    } else if((args[0].equalsIgnoreCase("give") && args.length >= 3) && (player.hasPermission("xpbottle.give") || player.isOp())){
                        if(Bukkit.getPlayer(args[1]) != null && stringIsInt(args[2]))
                            xpGive(Bukkit.getPlayer(args[1]), player, args[2]);
                        else
                            xpHelp(player);
                    } else if((args[0].equalsIgnoreCase("giveall") && args.length >= 2) && (player.hasPermission("xpbottle.giveall") || player.isOp())){
                        if(stringIsInt(args[1])){
                            xpGiveAll(player, args[1]);
                        } else{
                            xpHelp(player);
                        }
                    } else if((args[0].equalsIgnoreCase("giverd") && args.length >= 4) && (player.hasPermission("xpbottle.giverd") || player.isOp())) {
                        if (Bukkit.getPlayer(args[1]) != null) {
                            if(stringIsInt(args[2]) && stringIsInt(args[3])){
                                xpGiveRandom(Bukkit.getPlayer(args[1]), player, args[2], args[3]);
                            } else{
                                sender.sendMessage(ChatColor.RED + "Invalid max and min.");
                            }
                        } else{
                            sender.sendMessage(ChatColor.RED + "Invalid player name.");
                        }
                    } else if((args[0].equalsIgnoreCase("giveallrd") && args.length >= 3) && (player.hasPermission("xpbottle.giveallrd") || player.isOp())){
                        if(stringIsInt(args[1]) && stringIsInt(args[2])){
                            xpGiveAllRandom(player, args[1], args[2]);
                        } else{
                            sender.sendMessage(ChatColor.RED + "Invalid max and min.");
                        }
                    } else if(args[0].equalsIgnoreCase("reload") && (player.hasPermission("xpbottle.reload") || player.isOp())){
                        xpReload(player);
                    } else {
                        xpHelp(player);
                    }
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
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle <amount>" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Withdraws <amount> into an exp bottle.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle give <player> <amount>" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Gives a user an exp bottle with the <amount> amount.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle giveall <amount>" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Sends the whole server an exp bottle with the <amount> amount.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle giverd <player> <min> <max>" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Gives a user an exp bottle with a random exp bottle amount.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle giveallrd <min> <max>" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Removes the users EXP exhaustion.");
        player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "/xpbottle reload" + ChatColor.WHITE + " - " + ChatColor.GRAY + "Reloads the config file.");
    }

    private void xpAmount(Player player, String strExp){
        int xp = Integer.parseInt(strExp);

        if(!BottleCooldown.hasCooldown(player.getUniqueId())){
            if(xp >= XPBottleConfig.get().getInt("min-withdrawal-amount") && xp <= ExpHandle.getTotalExperience(player) && xp <= XPBottleConfig.get().getInt("max-withdrawal-amount")) {
                BottleCooldown.addCooldown(player.getUniqueId());

                player.getInventory().addItem(new Bottle(player, xp).getBottle());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l- " + xp + " xp"));

                if(XPBottleConfig.get().getBoolean("withdraw-sound.enabled"))
                    player.playSound(player.getLocation(), Sound.valueOf(XPBottleConfig.get().getString("withdraw-sound.sound")), 3, 1);

                ExpHandle.setTotalExperience(player, ExpHandle.getTotalExperience(player) - xp);

                player.sendMessage(ChatColor.YELLOW + "You are now afflicated with " + ChatColor.UNDERLINE + "EXP Exhaustion" + ChatColor.RESET + ChatColor.YELLOW + " for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s.");
                player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + ChatColor.YELLOW + " withdraw XP while EXP Exhausted.");
            } else{
                player.sendMessage(ChatColor.RED + "Invalid amount of xp.");
            }
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l(!) &r&cYou cannot create another XP Bottle for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s."));
        }
    }

    private void xpAll(Player player){
        int xp = ExpHandle.getTotalExperience(player);

        if(!BottleCooldown.hasCooldown(player.getUniqueId())) {
            if(xp >= XPBottleConfig.get().getInt("min-withdrawal-amount") && xp <= XPBottleConfig.get().getInt("max-withdrawal-amount")) {
                BottleCooldown.addCooldown(player.getUniqueId());

                player.getInventory().addItem(new Bottle(player, ExpHandle.getTotalExperience(player)).getBottle());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l- " + ExpHandle.getTotalExperience(player) + " xp"));

                ExpHandle.setTotalExperience(player, 0);

                player.sendMessage(ChatColor.YELLOW + "You are now afflicated with " + ChatColor.UNDERLINE + "EXP Exhaustion" + ChatColor.RESET + ChatColor.YELLOW + " for " + BottleCooldown.getCooldown(player.getUniqueId()) / 60 + "m " + BottleCooldown.getCooldown(player.getUniqueId()) % 60 + "s.");
                player.sendMessage(ChatColor.YELLOW + "You " + ChatColor.UNDERLINE + "cannot" + ChatColor.RESET + ChatColor.YELLOW + " withdraw XP while EXP Exhausted.");
            } else{
                player.sendMessage(ChatColor.RED + "Invalid amount of xp.");
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

        sender.sendMessage(ChatColor.GREEN + "You have gave " + player.getName() + " " + xp + " xp.");
    }

    private void xpGiveAll(Player sender, String strExp){
        int xp = Integer.parseInt(strExp);

        for(Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().addItem(new Bottle(sender, xp).getBottle());
        }

        sender.sendMessage(ChatColor.GREEN + "You have gave everyone " + xp + " xp.");
    }

    private void xpGiveRandom(Player player, Player sender, String minExp, String maxExp){
        int min = Integer.parseInt(minExp);
        int max = Integer.parseInt(maxExp);

        if(min <= max) {

            int xp = (int) (Math.random() * max + min);

            player.getInventory().addItem(new Bottle(sender, xp).getBottle());

            sender.sendMessage(ChatColor.GREEN + "You have gave " + player.getName() + " " + xp + " xp.");
        } else{
            sender.sendMessage(ChatColor.RED + "Invalid max and min.");
        }
    }

    private void xpGiveAllRandom(Player sender, String minExp, String maxExp){
        int min = Integer.parseInt(minExp);
        int max = Integer.parseInt(maxExp);

        if(min <= max){
            int xp = (int) (Math.random() * max + min);

            for(Player player : Bukkit.getOnlinePlayers()){
                player.getInventory().addItem(new Bottle(sender, xp).getBottle());
            }

            sender.sendMessage(ChatColor.GREEN + "You have gave everyone " + xp + " xp.");
        } else{
            sender.sendMessage(ChatColor.RED + "Invalid max and min.");
        }
    }

    private void xpReload(Player sender){
        XPBottleConfig.reload();
        sender.sendMessage(ChatColor.GREEN + "Reload complete.");
    }

    private boolean stringIsInt(String string){
        return string.matches("-?\\d+");
    }
}
