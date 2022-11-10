package de.legoshi.linkcraft.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class SignHelper {

    public static Sign createSign(Player player) {
        Block block = player.getWorld().getBlockAt(player.getLocation());
        block.setType(Material.SIGN_POST);
        return (Sign) block.getState();
    }


}
