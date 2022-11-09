package de.legoshi.linkcraft.util.message;

import org.bukkit.ChatColor;

import java.util.Objects;

public enum Message {

    COMMAND_HEADER("&3Command list for &7&l{0}"),
    COMMAND_SYNTAX("/{0} {1} {2} {3} {4} {5} {6} {7} {8}"),

    COMMAND_TAG_DESCRIPTION("Select, create and delete Tags."),
    COMMAND_TAG("See your tags!"),
    COMMAND_TAG_ADD("Add a new tag to the servers tag list."),
    COMMAND_TAG_EDIT("Edit a currently existing tag."),
    COMMAND_TAG_REMOVE("Delete a tag from the servers tag list."),
    COMMAND_TAG_SET("Give a tag to a specified player."),
    COMMAND_TAG_UNSET("Remove a tag from a specified player."),

    PLAYER_JOIN("{0} joined the Server!"),
    PLAYER_LEAVE("{0} left the Server!"),

    CMD_COOLDOWN_GLOBAL("&cPlease wait {0} second(s) before your next command."),
    CMD_COOLDOWN("&cPlease wait {0} second(s) before you can use this command again."),

    NO_PERMISSION("&cYou don't have permission to run that command!");

    public final String m;

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

    public String msgTrimArgs(String... args) {
        String message = msg(args);
        for (int i = args.length-1; i < 15; i++) {
            message = message.replace("{" + i + "} ", "").replace("{" + i + "}", "");
        }
        return message;
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

    public static Message fromString(String string) {
        for(Message m : Message.values()) {
            if(m.name().equalsIgnoreCase(string)) {
                return m;
            }
        }
        return null;
    }

    public String getRawMessage() {
        return this.m;
    }

}
