package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DatabaseService;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "add", permission = "lc.tags.add", permissionMessage = "You cant do that!!!!")
public class TagAddCommand implements CommandClass {

    @Inject private DatabaseService databaseService;

    @Command(names = "")
    public boolean addTags(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Add a playertag" + " " + databaseService.message);
        System.out.println(databaseService.mySQL);
        return true;
    }

}
