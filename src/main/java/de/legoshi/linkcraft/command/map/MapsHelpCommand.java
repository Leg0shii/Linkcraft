package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

// /maps help
@Command(names = "help")
public class MapsHelpCommand implements CommandClass {

    @Command(names = "")
    public boolean help(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "maps");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "add", "<name>", "<type>", "<length>", "<difficulty>", "<builders>");
        message =  message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "setstart <map_id>");
        message =  message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "setend <sign:plate>");
        message =  message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "edit", "<map_id>", "<name:type:difficulty:builders:release:length>", "<value>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "remove", "<map_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "setvictor", "<user_name>", "<map_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "maps", "unsetvictor", "<user_name>", "<map_id:all>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "signs help to see how to apply commands to signs (tags...)");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }

}
