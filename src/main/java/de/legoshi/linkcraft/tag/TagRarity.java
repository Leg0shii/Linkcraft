package de.legoshi.linkcraft.tag;

import org.bukkit.ChatColor;

public enum TagRarity {

    NONE,
    COMMON,
    RARE,
    EPIC,
    LEGENDARY;

    public static ChatColor toColor(TagRarity tagRarity) {
        ChatColor result;

        switch(tagRarity) {
            case COMMON:
                result = ChatColor.GRAY;
                break;
            case RARE:
                result = ChatColor.DARK_AQUA;
                break;
            case EPIC:
                result = ChatColor.DARK_PURPLE;
                break;
            case LEGENDARY:
                result = ChatColor.GOLD;
                break;
            default:
                result = ChatColor.WHITE;
        }

        return result;
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
