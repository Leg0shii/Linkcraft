package de.legoshi.linkcraft.util.message;

import java.util.EnumSet;
import java.util.Set;

public enum Messages {

    INFO("§6§l[Linkcraft]§7 ", "prefix"),
    ERROR("§c[Error]§7 ", "prefix"),
    SUCCESS("§a[Success]§7 ", "prefix"),

    COMMAND_HEADER("§3Command list for §7§l{0}", "help"),
    COMMAND_SYNTAX("/{0} {1} {2} {3} {4} {5} {6} {7} {8}", "help"),
    UNKNOWN_DESC("{0} command.", "description"),
    USAGE_ENTRY("/{0}", "help"),
    INVALID_COMMAND("command invalid", "error"),

    COMMAND_GG("§6§lG§e§lG", "fun-command"),
    COMMAND_GL("§2§lG§a§lL ☺", "fun-command"),
    COMMAND_RIP("§0§lR§8§lI§f§lP ☹", "fun-command"),
    COMMAND_HAM("§d§lha §cm", "fun-command"),
    COMMAND_BACON("BACON", "fun-command"),
    COMMAND_EGGS("§6§lE§e§lG§6§lg§e§lS", "fun-command"),

    COMMAND_TAG_DESCRIPTION("Select, create and delete Tags.", "tag-command"),
    COMMAND_TAG("See your tags!", "tag-command"),
    COMMAND_TAG_ADD("Add a new tag to the servers tag list.", "tag-command"),
    COMMAND_TAG_EDIT("Edit a currently existing tag.", "tag-command"),
    COMMAND_TAG_REMOVE("Delete a tag from the servers tag list.", "tag-command"),
    COMMAND_TAG_SET("Give a tag to a specified player.", "tag-command"),
    COMMAND_TAG_UNSET("Remove a tag from a specified player.", "tag-command"),

    PLAYER_JOIN("{0} joined the Server!", "info"),
    PLAYER_LEAVE("{0} left the Server!", "info"),

    CMD_COOLDOWN_GLOBAL("§cPlease wait {0} second(s) before your next command.", "error"),
    CMD_COOLDOWN("§cPlease wait {0} second(s) before you can use this command again.", "error"),

    ERROR_FATAL("FATAL ERROR!!!!!!", "error"),

    COMMAND_LIST_PAGE_HEADER("§0-------§2[§3Category {0}§2]§0-------", "help"),
    COMMAND_LIST_PAGE_FOOTER("§0--------------------------------", "help"),

    PLAYER_NO_PERMISSION("§cYou don't have permission to run that command!", "error"),
    INVALID_ARG_LENGTH("INVALID_ARG_LENGTH", "error");

    private final String message;
    private final String category;

    private static final Set<Messages> messages =
            EnumSet.allOf(Messages.class);

    Messages(String message, String category) {
        this.message = message;
        this.category = category;
    }

    public String getNode() {
        return (category + "." + name()).toLowerCase();
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public static Set<Messages> defaultMessagesSet() {
        return messages;
    }

}
