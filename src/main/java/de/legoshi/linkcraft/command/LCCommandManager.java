package de.legoshi.linkcraft.command;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.command.tags.*;
import de.legoshi.linkcraft.util.message.Message;
import de.legoshi.linkcraft.util.message.Prefix;
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.CommandManager;

public class LCCommandManager {

    public LCCommandManager() {
        registerCommands();
    }

    private void registerCommands() {
        registerTagCommands();
    }

    private void registerTagCommands() {
        try {
            CommandManager.createCoreCommand(
                    Linkcraft.getInstance(),
                    "tags",
                    Message.COMMAND_TAG_DESCRIPTION.msg(),
                    "/tags",
                    (player, subCommandList) -> {
                        player.sendMessage(ColorTranslator.translateColorCodes(Message.COMMAND_HEADER.msg(Prefix.INFO, "Tags")));
                        subCommandList.forEach(subCommand -> {
                            if (player.hasPermission("lc.tags." + subCommand.getName())) {
                                player.sendMessage(ColorTranslator.translateColorCodes("&3> &a" + subCommand.getSyntax()));
                            }
                        });
                    },
                    TagsAddCommand.class, TagsEditCommand.class, TagsRemoveCommand.class, TagsSetCommand.class, TagsUnsetCommand.class
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
