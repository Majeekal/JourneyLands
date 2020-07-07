package me.majeek.journeylands.xpbottle;

import me.majeek.journeylands.Main;
import me.majeek.journeylands.files.XPBottleConfig;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class BottleCooldown {
    private static HashMap<UUID, Integer> cooldown = new HashMap<>();

    public static int getCooldown(UUID uuid){
        return hasCooldown(uuid) ? cooldown.get(uuid) : 0;
    }

    public static void addCooldown(UUID uuid){
        if(!cooldown.containsKey(uuid)) {
            cooldown.put(uuid, XPBottleConfig.get().getInt("exp-exhaustion-cooldown"));

            new BukkitRunnable() {
                int seconds = XPBottleConfig.get().getInt("exp-exhaustion-cooldown");

                @Override
                public void run() {
                    seconds -= 1;

                    if(seconds == 0){
                        cooldown.remove(uuid);
                        cancel();
                    } else if(!cooldown.containsKey(uuid)){
                        cancel();
                    } else{
                        cooldown.put(uuid, seconds);
                    }
                }
            }.runTaskTimer(Main.getInstance(), 20L, 20L);
        }
    }

    public static boolean hasCooldown(UUID uuid){
        return cooldown.containsKey(uuid);
    }

    public static void removeCooldown(UUID uuid){
        if(cooldown.containsKey(uuid))
            cooldown.remove(uuid);
    }
}
