package de.legoshi.linkcraft.command.tag;

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

    @Inject private DBManager databaseService;
    @Inject private TagMenu tagMenu;

    @Command(names = "help")
    public boolean tagsHelp(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "tags");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "add", "<name>", "<rarity>", "<type>", "[description]");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<tag_id>", "<name:rarity:type:desc>", "<value>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "remove", "<user_name>", "<tag_id:all>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "set", "<user_name>", "<tag_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "unset", "<user_name>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "give", "<user_name>", "<tag_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "delete", "<tag_id>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            tagsHelp(sender);
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("no player");
            return false;
        }

        tagMenu.openGui(player, null);
        return true;
    }

}
