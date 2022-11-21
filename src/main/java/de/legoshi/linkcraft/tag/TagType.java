package de.legoshi.linkcraft.tag;

import org.bukkit.ChatColor;

public enum TagType {
    NONE,
    HIDDEN,
    VICTOR,
    EVENT,
    CUSTOM;

    public static ChatColor toColor(TagType tagType) {
        return switch (tagType) {
            case HIDDEN -> ChatColor.LIGHT_PURPLE;
            case VICTOR -> ChatColor.GOLD;
            case EVENT -> ChatColor.RED;
            case CUSTOM -> ChatColor.DARK_AQUA;
            default -> ChatColor.WHITE;
        };
    }

    public static int getTagPosition(TagType type) {
        int typeCount = 0;
        for (TagType tagType : TagType.values()) {
            if (tagType.equals(type)) break;
            typeCount++;
        }
        return typeCount;
    }
}
