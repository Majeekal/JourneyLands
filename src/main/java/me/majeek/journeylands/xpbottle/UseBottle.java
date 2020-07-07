package me.majeek.journeylands.xpbottle;

import me.majeek.journeylands.files.XPBottleConfig;
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

                int xp = 0;

                for(int i = 0; i < event.getItem().getAmount(); i++)
                    xp += Integer.parseInt(event.getItem().getItemMeta().getLocalizedName());

                ExpHandle.setTotalExperience(event.getPlayer(), xp + ExpHandle.getTotalExperience(event.getPlayer()));

                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&l+ " + xp + " xp"));

                if(XPBottleConfig.get().getBoolean("redeem-sound.enabled"))
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.valueOf(XPBottleConfig.get().getString("redeem-sound.sound")), 3, 1);

                event.getPlayer().getInventory().remove(event.getItem());
            }
        }
    }

    private int getIntInString(String string){
        return Integer.parseInt(string.replaceAll("[\\D]", ""));
    }
}
