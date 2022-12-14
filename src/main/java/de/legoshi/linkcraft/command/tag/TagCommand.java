package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.tag.TagMenu;
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

@Command(names = "tags", desc = "%translatable:tags.desc%")
@SubCommandClasses({
        TagAddCommand.class,
        TagEditCommand.class,
        TagRemoveCommand.class,
        TagSetCommand.class,
        TagUnsetCommand.class,
        TagGiveCommand.class,
        TagDeleteCommand.class,
})
@ArgOrSub(value = true)
public class TagCommand implements CommandClass {

    @Inject private Injector injector;
    @Inject private TagHelpCommand tagHelpCommand;

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            tagHelpCommand.tagsHelp(sender);
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER, true));
            return true;
        }

        injector.getInstance(TagMenu.class).openGui(player, null);
        return true;
    }

}
