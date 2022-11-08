package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@Command(names = "tags")
@SubCommandClasses(TagAddCommand.class)
public class TagCommand implements CommandClass {

    @Inject private DBManager dbManager;

    @Command(names = "")
    public boolean tags(@Sender CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Now your tags will be opened!!!" + " ");
        return true;
    }

}
