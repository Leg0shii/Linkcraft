package de.legoshi.linkcraft.command.saves;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

// /saves help
@Command(names = "help")
public class SavesHelpCommand implements CommandClass {

    @Command(names = "")
    public boolean help(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "saves");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "saves");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "saves", "check", "<player_name>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }

}
