package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.annotated.annotation.Usage;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;


// Note: Another issue that im not sure of a workaround to is that annotations require constants.
// This is a problem because we cannot use something like: Message.NO_PERMISSION.msg(Prefix.INFO) in the annotations
// By the way, you can remove any of these comments after you read them of course :P
@Command(names = "tags", desc = "Opens tag selection menu")
@SubCommandClasses({
        TagAddCommand.class,
        TagsEditCommand.class,
        TagsRemoveCommand.class,
        TagsSetCommand.class,
        TagsUnsetCommand.class
})
public class TagCommand implements CommandClass {

    @Inject private DBManager databaseService;

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Now your tags will be opened!!!" + databaseService.message);
        System.out.println(databaseService.mySQL);
        return true;
    }

}
