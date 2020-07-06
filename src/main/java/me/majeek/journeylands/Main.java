package me.majeek.journeylands;

import me.majeek.journeylands.listeners.CommandListener;
import me.majeek.journeylands.listeners.EventListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        // Listeners
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getCommand("journeylands").setExecutor(new CommandListener());

        // Managers
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance(){
        return instance;
    }
}
