package me.majeek.journeylands.files;

import me.majeek.journeylands.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class XPBottleConfig {
    private static File file;
    private static FileConfiguration xpBottleConfig;

    public static void setup(){
        file = new File(Main.getInstance().getDataFolder(), "xpbottle.yml");

        if(!file.exists()){
            try { file.createNewFile(); }
            catch (IOException e){}
        }

        xpBottleConfig = YamlConfiguration.loadConfiguration(file);

        xpBottleConfig.options().header("All values are in seconds");
        xpBottleConfig.options().copyHeader(true);

        xpBottleConfig.addDefault("exp-exhaustion-cooldown", 1200);
        xpBottleConfig.addDefault("min-withdrawal-amount", 1);
        xpBottleConfig.addDefault("max-withdrawal-amount", 10000000);
        xpBottleConfig.addDefault("withdraw-sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        xpBottleConfig.addDefault("withdraw-sound.enabled", true);
        xpBottleConfig.addDefault("redeem-sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
        xpBottleConfig.addDefault("redeem-sound.enabled", true);
        xpBottleConfig.addDefault("item.displayname", "&a&lExperience Bottle");
        xpBottleConfig.addDefault("item.material", "EXPERIENCE_BOTTLE");
        xpBottleConfig.addDefault("item.lore", Arrays.asList("&dValue&r <xp> XP", "&dEnchanter&r <enchanter>"));
    }

    public static FileConfiguration get(){
        return xpBottleConfig;
    }

    public static void save(){
        try { xpBottleConfig.save(file); }
        catch (IOException e){}
    }

    public static void reload(){
        xpBottleConfig = YamlConfiguration.loadConfiguration(file);
    }
}
