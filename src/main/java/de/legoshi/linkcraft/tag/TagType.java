package de.legoshi.linkcraft.tag;

import org.bukkit.ChatColor;

public enum TagType {
    NONE,
    HIDDEN,
    VICTOR,
    EVENT,
    CUSTOM;

    public static ChatColor toColor(TagType tagType) {
        ChatColor result;
        switch (tagType) {
            case HIDDEN:
                result = ChatColor.LIGHT_PURPLE;
                break;
            case VICTOR:
                result = ChatColor.GOLD;
                break;
            case EVENT:
                result = ChatColor.RED;
                break;
            case CUSTOM:
                result = ChatColor.DARK_AQUA;
                break;
            default:
                result =ChatColor.WHITE;
        }
        return result;
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
