package me.majeek.journeylands.xpbottle;

import me.majeek.journeylands.files.XPBottleConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Bottle {
    private Player player;
    private int xp;
    private ItemStack item;

    public Bottle(Player player, int xp){
        this.player = player;
        this.xp = xp;
        this.item = createBottle();
    }

    public ItemStack getBottle(){
        return item;
    }

    private ItemStack createBottle(){
        ItemStack item = new ItemStack(Material.valueOf(XPBottleConfig.get().getString("item.material")));
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', XPBottleConfig.get().getString("item.displayname")));
        itemMeta.setLocalizedName(xp + "");

        for(String lore : XPBottleConfig.get().getStringList("item.lore")){
            lore = lore.replaceAll("<xp>", xp + "");
            lore = lore.replaceAll("<enchanter>", player.getName());

            itemLore.add(ChatColor.translateAlternateColorCodes('&', lore));
        }

        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
