package de.legoshi.linkcraft.command.saves;


import de.legoshi.linkcraft.command.map.*;
import de.legoshi.linkcraft.gui.map.MapMenu;
import de.legoshi.linkcraft.gui.save.SavesHolder;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

// /saves - open gui
@Command(names = "saves", desc = "%translatable:saves.desc%")
@SubCommandClasses({
        SavesCheckCommand.class,
        SavesHelpCommand.class
})
@ArgOrSub(value = true)
public class SavesCommand implements CommandClass {

    @Inject private Injector injector;
    @Inject private SavesHelpCommand helpCommand;

    @Command(names = "")
    public boolean saves(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            helpCommand.help(sender);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER.getMessage(), true));
            return true;
        }

        Player player = (Player)sender;

        injector.getInstance(SavesHolder.class).openGui(player, null);
        return true;
    }
}
