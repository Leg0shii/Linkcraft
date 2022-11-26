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
    USAGE_ENTRY("§f/{0}", "help"),
    INVALID_COMMAND("command invalid", "error"),

    MESSAGE_GG("§6§lG§e§lG", "other"),
    MESSAGE_GL("§2§lG§a§lL ☺", "other"),
    MESSAGE_RIP("§0§lR§8§lI§f§lP ☹", "other"),
    MESSAGE_HAM("§d§lha §cm", "other"),
    MESSAGE_EGGS("§6§lE§e§lG§6§lg§e§lS", "other"),

    TAGS_DESC("§fSee your tags! For help type /tags help", "description"),
    TAGS_ADD_DESC("Add a new tag to the servers tag list.", "description"),
    TAGS_GIVE_DESC("Gives a specified tag to a player.", "description"),
    TAGS_EDIT_DESC("Edit a currently existing tag.", "description"),
    TAGS_REMOVE_DESC("Delete a tag from the servers tag list.", "description"),
    TAGS_SET_DESC("Give a tag to a specified player.", "description"),
    TAGS_UNSET_DESC("Remove a tag from a specified player.", "description"),
    TAGS_RARITY_ERROR("§cRarity must be between 0 and {0}.", "error"),
    TAGS_TYPE_ERROR("§cType must be between 0 and {0}.", "error"),
    TAGS_ADD_SUCCESS("§aSuccessfully added tag §r{0}", "info"),
    TAGS_UPDATE_SUCCESS("§aSuccessfully updated tag with id {0}", "info"),
    TAGS_NO_TAG("§cNo tag exists with the id {0}.", "error"),
    TAGS_HASNT_UNLOCKED("§c{0} does not have a tag with the id {1}", "error"),
    TAGS_HAS_TAG("§c{0} already has the tag with id {1}.", "error"),
    TAGS_NOT_UNLOCKED("§cYou have not unlocked §r{0}", "error"),
    TAGS_GAVE_TAG("§aSuccessfully gave {0} tag: §r{1}", "info"),
    TAGS_REMOVE_TAG("§aSuccessfully removed {0}'s tag: §r{1}", "info"),
    TAGS_REMOVE_TAGS("§aSuccessfully removed {0} tag(s) from {1}", "info"),
    TAGS_DELETE_TAG("§aSuccessfully deleted tag with id {0}.", "info"),
    TAGS_SELECT("§aYour tag has been set to: §r{0}", "info"),
    TAGS_SELECT_OTHER("§a{0}'s tag has been set to: §r{1}", "info"),
    TAGS_UNSET_TAG("§aYour tag has been unset", "info"),
    TAGS_UNSET_TAG_OTHER("§a{0}'s tag has been unset", "info"),
    TAGS_UNSET_NONE("§cYou have no tag equipped!", "error"),

    MAPS_ADD_MAP("§aSuccessfully added new map §r{0} with difficulty §r{1}.", "info"),
    MAPS_EDIT_MAP("§aSuccessfully edited map §r{0}§'s value §r{1} §ato §r{2}§a.", "info"),
    MAPS_SET_START_MAP("§aSuccessfully set a new start for map §r{0}§a.", "info"),
    MAPS_REMOVE_MAP("§aSuccessfully removed map §r{0}§a.", "info"),
    MAPS_SET_VICTOR_MAP("§aSuccessfully added §r{0}§a as a victor to map §r{1}§a.", "info"),

    MAPS_ID_NOT_EXIST("§cId §r{0}§c doesn't exist.", "error"),

    GG_DESC("§Sends §6§lG§e§lG§f in chat.", "description"),
    GL_DESC("§Sends §2§lG§a§lL ☺§f in chat.", "description"),
    RIP_DESC("§Sends §0§lR§8§lI§f§lP ☹§f in chat.", "description"),
    HAM_DESC("§Sends §d§lha §c§lm§f in chat.", "description"),
    EGGS_DESC("§fWrite §6§lE§e§lG§6§lg§e§lS§f in chat.", "description"),

    PLAYER_JOIN("{0} joined the Server!", "info"),
    PLAYER_LEAVE("{0} left the Server!", "info"),


    CMD_COOLDOWN_GLOBAL("§cPlease wait {0} second(s) before your next command.", "error"),
    CMD_COOLDOWN("§cPlease wait {0} second(s) before you can use this command again.", "error"),

    ERROR_FATAL("FATAL ERROR!!!!!!", "error"),

    COMMAND_LIST_PAGE_HEADER("§0§m---------§r§2[§3Category {0}§2]§0§m---------", "help"),
    COMMAND_LIST_PAGE_FOOTER("§0§m--------------------------------", "help"),

    PLAYER_NOT_EXIST("§cPlayer does not exist.", "error"),
    NOT_A_PLAYER("§cYou have to be a player to execute this command", "error"),
    PLAYER_NO_PERMISSION("§cYou don't have permission to run that command!", "error"),
    EFFECT_BLOCK_NO_PERMISSION("§cYou do not have permissions to click this!", "error"),
    INVALID_ARG_LENGTH("INVALID_ARG_LENGTH", "error"),

    NEVER_JOINED("§c{0} has never joined the server.", "error"),
    REQUIRE_INTEGER("§c{0} must be an integer!", "error"),
    INVALID_ARG("§cArgument {0} is invalid", "error");

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
