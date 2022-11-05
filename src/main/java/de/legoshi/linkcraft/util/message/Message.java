package de.legoshi.linkcraft.util.message;

import org.bukkit.ChatColor;

import java.util.Objects;

public enum Message {



    PLAYER_JOIN("{0} joined the Server!"),
    PLAYER_LEAVE("{0} left the Server!"),
    CMD_COOLDOWN("Please wait {0} second(s) before your next command.");

    final String m;

    Message(String message) {
        this.m = message;
    }


    public String msg() {
        return colourize(this.m);
    }

    public String msg(String... args) {
        return template(this.m, args);
    }

    public String msg(Prefix prefix) {
        return colourize(prefix.text() + this.m);
    }

    public String msg(Prefix prefix, String... args) {
        return colourize(template(prefix.text() + this.m, args));
    }


    private String colourize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    private String template(String message, String... args) {
        String result = message;

        for (int i = 0; i < args.length; i++) {
            String replacement = args[i];
            result = message.replace("{" + i + "}", Objects.nonNull(replacement) ? replacement : "<?>");
        }
        return result;
    }

    public String getRawMessage() {
        return this.m;
    }

}
