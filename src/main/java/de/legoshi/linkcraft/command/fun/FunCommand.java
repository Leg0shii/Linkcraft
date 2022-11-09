package de.legoshi.linkcraft.command.fun;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import de.legoshi.linkcraft.util.message.Prefix;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Required;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunCommand implements CommandClass {

    @Command(names = "gg", desc = "Write gg in chat.")
    public void gg(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_GG));
    }

    @Command(names = "gl", desc = "Write gl in chat.")
    public void gl(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_GL));
    }

    @Command(names = "rip", desc = "Write rip in chat.")
    public void rip(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_RIP));
    }

    @Command(names = "ham", desc = "Write ham in chat.")
    public void ham(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_HAM));
    }

    @Command(names = "bacon", desc = "Write bacon in chat.")
    public void bacon(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_BACON));
    }

    @Command(names = "eggs", desc = "Write eggs in chat.")
    public void eggs(@Sender CommandSender sender) {
        if (!(sender instanceof Player player)) return;
        player.chat(MessageUtils.getMessageTranslated(Messages.COMMAND_EGGS));
    }

}
