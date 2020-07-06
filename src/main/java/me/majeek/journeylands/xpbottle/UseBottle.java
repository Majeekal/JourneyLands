package me.majeek.journeylands.xpbottle;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class UseBottle implements Listener {
    @EventHandler
    public void onBottleUse(PlayerInteractEvent event){
        if(event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getLore() != null){
            if(event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(new Bottle(event.getPlayer(), 0).getBottle().getItemMeta().getDisplayName())){
                event.setCancelled(true);

                ExpHandle.setTotalExperience(event.getPlayer(), getIntInString(event.getItem().getItemMeta().getLore().get(0)) + ExpHandle.getTotalExperience(event.getPlayer()));

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l+ " + getIntInString(event.getItem().getItemMeta().getLore().get(0)) + " xp"));
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);

                event.getPlayer().getInventory().remove(event.getItem());
            }
        }
    }

    private int getIntInString(String string){
        return Integer.parseInt(string.replaceAll("[\\D]", ""));
    }
}