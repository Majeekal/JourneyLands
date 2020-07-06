package me.majeek.journeylands.xpbottle;

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
        ItemStack item = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> itemLore = new ArrayList<>();

        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lExperience Bottle"));

        itemLore.add(ChatColor.LIGHT_PURPLE + "Value " + ChatColor.RESET + xp + " XP");
        itemLore.add(ChatColor.LIGHT_PURPLE + "Enchanter " + ChatColor.RESET + player.getName());

        itemMeta.setLore(itemLore);
        item.setItemMeta(itemMeta);

        return item;
    }
}
