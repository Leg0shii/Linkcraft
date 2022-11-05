package de.legoshi.linkcraft.util.message;

import org.bukkit.ChatColor;

import java.util.Objects;

public enum Message {

    PLAYER_JOIN("{0} joined the Server!"),
    PLAYER_LEAVE("{0} left the Server!");

    String m;

    Message(String message) {
        this.m = message;
    }


    public String msg() {
        this.m = ChatColor.translateAlternateColorCodes('&', m);
        return this.m;
    }

    public String msg(String... args) {

        for (int i = 0; i < args.length; i++) {
            String replacement = args[i];
            this.m = this.m.replace("{" + i + "}", Objects.nonNull(replacement) ? replacement : "<?>");
        }

        return this.m;
    }

    public String msg(Prefix prefix) {
        this.m = prefix.text() + this.m;
        return msg();
    }

    public String msg(Prefix prefix, String... args) {
        this.m = msg(args);
        this.m = msg(prefix);
        return this.m;
    }

    public String getRawMessage() {
        return this.m;
    }

}
