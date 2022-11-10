package de.legoshi.linkcraft.command.sign;

import de.legoshi.linkcraft.util.message.MessageUtils;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;

@Command(names = "sign", desc = "%translatable:tags.desc%")
@SubCommandClasses(SignCreateCommand.class)
public class SignCommand implements CommandClass {

    @Command(names = "help")
    public boolean signHelp(CommandSender sender) {
        String message = "";
        MessageUtils.sendMessage(sender, message);
        return true;
    }

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender, CommandContext commandContext) {
        signHelp(sender);
        return true;
    }

}
