package me.majeek.journeylands.commandblocker;

import me.majeek.journeylands.files.CommandBlockerConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RedirectCommands implements Listener {
    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        if((event.getPlayer().isOp() && CommandBlockerConfig.get().getBoolean("is-op-blocked")) || !event.getPlayer().isOp()){
            for(String command : CommandBlockerConfig.get().getStringList("blocked-commands")){
                if(event.getMessage().equalsIgnoreCase("/" + command)){
                    if(CommandBlockerConfig.get().getString("blocked-command-message") != null)
                        event.getPlayer().sendMessage(CommandBlockerConfig.get().getString("blocked-command-message"));

                    event.setCancelled(true);
                }
            }
        }
    }
}
