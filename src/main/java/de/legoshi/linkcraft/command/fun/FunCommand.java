package de.legoshi.linkcraft.command.fun;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FunCommand implements CommandClass {

    @Command(names = "gg", desc = "%translatable:gg.desc%")
    public void gg(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_GG));
    }

    @Command(names = "gl", desc = "%translatable:gl.desc%")
    public void gl(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_GL));
    }

    @Command(names = "rip", desc = "%translatable:rip.desc%")
    public void rip(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_RIP));
    }

    @Command(names = "ham", desc = "%translatable:ham.desc%")
    public void ham(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_HAM));
    }

    @Command(names = "eggs", desc = "%translatable:eggs.desc%")
    public void eggs(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_EGGS));
    }

    @Command(names = "fail", desc = "%translatable:fail.desc%")
    public void fail(@Sender CommandSender sender) {
        if (!(sender instanceof Player)) return;
        Player player = (Player)sender;
        player.chat(MessageUtils.getMessageTranslated(Messages.MESSAGE_FAIL));
    }

}
