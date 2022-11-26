package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "help")
public class TagHelpCommand implements CommandClass {

    @Command(names = "")
    public boolean tagsHelp(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "tags");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "add", "<name>", "<rarity>", "<type>", "[description]");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<tag_id>", "<name:rarity:type:desc>", "<value>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "give", "<user_name>", "<tag_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "remove", "<user_name>", "<tag_id:all>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "delete", "<tag_id>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }

}
