package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.tag.TagMenu;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(names = "tags", desc = "%translatable:tags.desc%")
@SubCommandClasses({
        TagAddCommand.class,
        TagsEditCommand.class,
        TagsRemoveCommand.class,
        TagsSetCommand.class,
        TagsUnsetCommand.class
})
@ArgOrSub(value = true)
public class TagCommand implements CommandClass {

    @Inject private DBManager databaseService;

    @Command(names = "help")
    public boolean tagsHelp(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "tags");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "help");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "add", "<name>", "<rarity>", "[description]");
        message =  message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<name:rarity:desc>", "<tag_id>", "<value>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "remove", "<tag_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "set", "<user_name>", "<tag_id>");
        message = message + "\n §3>§7 " + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<user_name>", "<tag_id:all>");
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

        new TagMenu(player, null).openGui();
        return true;
    }

}
