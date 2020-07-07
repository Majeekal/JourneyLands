package me.majeek.journeylands.files;

import me.majeek.journeylands.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class CommandBlockerConfig {
    private static File file;
    private static FileConfiguration commandBlockerConfig;

    public static void setup(){
        file = new File(Main.getInstance().getDataFolder(), "commandblocker.yml");

        if(!file.exists()){
            try { file.createNewFile(); }
            catch (IOException e){}
        }

        commandBlockerConfig = YamlConfiguration.loadConfiguration(file);

        commandBlockerConfig.addDefault("is-op-blocked", false);
        commandBlockerConfig.addDefault("blocked-commands", Arrays.asList("pl", "plugins", "?", "bukkit:pl", "bukkit:plugins", "bukkit:?"));
        commandBlockerConfig.addDefault("blocked-command-message", "Unknown command. Type \"/help\" for help.");
    }

    public static FileConfiguration get(){
        return commandBlockerConfig;
    }

    public static void save(){
        try { commandBlockerConfig.save(file); }
        catch (IOException e){}
    }

    public static void reload(){
        commandBlockerConfig = YamlConfiguration.loadConfiguration(file);
    }
}
