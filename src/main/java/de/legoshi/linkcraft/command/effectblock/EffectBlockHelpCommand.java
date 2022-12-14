package de.legoshi.linkcraft.command.effectblock;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

public class EffectBlockHelpCommand implements CommandClass {
    @Command(names = "help")
    public boolean effectBlockHelp(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "effectblock");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "effectblock", "add", "[command]");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "effectblock", "remove");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }
}
