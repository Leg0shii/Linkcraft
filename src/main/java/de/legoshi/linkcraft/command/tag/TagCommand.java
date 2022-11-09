package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.annotated.annotation.Usage;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.util.Arrays;

@Command(names = "tags", desc = "%translatable:command.tag-description%")
@SubCommandClasses({
        TagAddCommand.class,
        TagsEditCommand.class,
        TagsRemoveCommand.class,
        TagsSetCommand.class,
        TagsUnsetCommand.class
})
public class TagCommand implements CommandClass {

    @Inject private DBManager databaseService;

    @Command(names = "help")
    public boolean tagsHelp(CommandSender sender) {
        String message = MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_HEADER, false, "tags");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "help");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "add", "<name>", "<rarity>", "[description]");
        message =  message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<name:rarity:desc>", "<tag_id>", "<value>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "remove", "<tag_id>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "set", "<user_name>", "<tag_id>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_SYNTAX, false, "tags", "edit", "<user_name>", "<tag_id:all>");
        message = message + "\n" + MessageUtils.composeMessage(Messages.COMMAND_LIST_PAGE_FOOTER, false);
        MessageUtils.sendMessage(sender, message);
        return true;
    }

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Now your tags will be opened!!!" + databaseService.message);
        System.out.println(databaseService.mySQL);
        return true;
    }

}
