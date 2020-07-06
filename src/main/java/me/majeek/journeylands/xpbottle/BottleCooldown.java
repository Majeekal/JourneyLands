package me.majeek.journeylands.xpbottle;

import me.majeek.journeylands.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class BottleCooldown {
    private static HashMap<UUID, Integer> cooldown = new HashMap<>();

    private static int COOLDOWN_DURATION = 60; // 1 minute

    public static int getCooldown(UUID uuid){
        return hasCooldown(uuid) ? cooldown.get(uuid) : 0;
    }

    public static void addCooldown(UUID uuid){
        if(!cooldown.containsKey(uuid)) {
            cooldown.put(uuid, COOLDOWN_DURATION);

            new BukkitRunnable() {
                int seconds = COOLDOWN_DURATION;

                @Override
                public void run() {
                    seconds -= 1;

                    cooldown.put(uuid, seconds);

                    if(seconds == 0){
                        cooldown.remove(uuid);
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 20L, 20L);
        }
    }

    public static boolean hasCooldown(UUID uuid){
        return cooldown.containsKey(uuid);
    }
}
