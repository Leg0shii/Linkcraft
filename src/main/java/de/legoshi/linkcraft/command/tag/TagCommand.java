package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@Command(names = "tags")
@SubCommandClasses({TagAddCommand.class /*, TagsEditCommand.class, TagsRemoveCommand.class, TagsSetCommand.class, TagsUnsetCommand.class */})
public class TagCommand implements CommandClass {

    // @Inject private DBManager dbManager;

    @Command(names = "", desc = "See your tags!")
    public boolean tags(@Sender CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Now your tags will be opened!!!");
        //System.out.println(dbManager.message);
        return true;
    }

}
