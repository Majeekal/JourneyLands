package me.majeek.journeylands.listeners;

import me.majeek.journeylands.Main;
import me.majeek.journeylands.commandblocker.RedirectCommands;
import me.majeek.journeylands.xpbottle.UseBottle;
import org.bukkit.event.Listener;

public class EventListener implements Listener {
    public EventListener(){
        Main.getInstance().getServer().getPluginManager().registerEvents(new UseBottle(), Main.getInstance());
        Main.getInstance().getServer().getPluginManager().registerEvents(new RedirectCommands(), Main.getInstance());
    }
}
