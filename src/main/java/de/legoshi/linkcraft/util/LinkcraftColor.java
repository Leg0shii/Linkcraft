package de.legoshi.linkcraft.util;

import net.kyori.text.format.TextColor;
import org.bukkit.ChatColor;

public enum LinkcraftColor {

    BASE(TextColor.GRAY),
    RELEVANT(TextColor.DARK_AQUA),
    IRRELEVANT(TextColor.DARK_GRAY),
    IMPORTANT(TextColor.BLUE),
    WARNING(TextColor.RED);

    private final TextColor textColor;

    LinkcraftColor(TextColor textColor) {
        this.textColor = textColor;
    }

    public TextColor getDefaultTextColor() { return textColor; }

    public ChatColor getChatColor() {
        return ChatColor.valueOf(textColor.toString().toUpperCase());
    }

}
