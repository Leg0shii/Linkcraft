package de.legoshi.linkcraft.util.message;

import org.bukkit.ChatColor;

import java.util.Objects;

public enum Message {

    INFO_PREFIX("&6&l[Linkcraft]&7 "),
    ERR_PREFIX("&c[Error]&7 "),
    SUC_PREFIX("&a[Success]&7 "),
    NO_PREFIX("&7"),

    PLAYER_JOIN("{0} joined the Server!"),
    PLAYER_LEAVE("{0} left the Server!");

    final String m;

    Message(String message) {
        this.m = message;
    }

    public String msg(Prefix prefix) {
        String manip = m;

        if (prefix == Prefix.INFO) manip = INFO_PREFIX.m + manip;
        else if (prefix == Prefix.ERROR)  manip = ERR_PREFIX.m + manip;
        else if (prefix == Prefix.SUCCESS)  manip = SUC_PREFIX.m + manip;
        else manip = NO_PREFIX.m + manip;

        manip = ChatColor.translateAlternateColorCodes('&', manip);
        return manip;
    }

    public String msg(Prefix prefix, String... args) {
        String manip = m;

        for (int i = 0; i < args.length; i++) {
            String replacement = args[i];
            manip = manip.replace("{" + i + "}", Objects.nonNull(replacement) ? replacement : "<?>");
        }

        manip = msg(prefix);
        return manip;
    }

    public String getRawMessage() {
        return this.m;
    }

}
