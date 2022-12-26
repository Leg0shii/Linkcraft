package de.legoshi.linkcraft.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public interface ItemUtils {
    static void addGlow(ItemStack item) {
        ItemMeta im = item.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(im);
    }

    static ItemStack playerHead(String username) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta sm = (SkullMeta)head.getItemMeta();
        sm.setOwner(username);
        head.setItemMeta(sm);
        return head;
    }
}
