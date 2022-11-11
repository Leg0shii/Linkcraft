package de.legoshi.linkcraft.tag;

import org.bukkit.ChatColor;

public enum TagRarity {

    NONE,
    COMMON,
    RARE,
    EPIC,
    LEGENDARY;

    public static ChatColor toColor(TagRarity tagRarity) {
        return switch (tagRarity) {
            case COMMON -> ChatColor.GRAY;
            case RARE -> ChatColor.DARK_AQUA;
            case EPIC -> ChatColor.DARK_PURPLE;
            case LEGENDARY -> ChatColor.GOLD;
            default -> ChatColor.WHITE;
        };
    }

    public static int getTagPosition(TagRarity rarity) {
        int rarityCount = 0;
        for (TagRarity tagRar : TagRarity.values()) {
            if (tagRar.equals(rarity)) break;
            rarityCount++;
        }
        return rarityCount;
    }
}
