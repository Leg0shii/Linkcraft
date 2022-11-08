package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@Command(names = "add", permission = "lc.tags.add", permissionMessage = "You cant do that!!!!")
public class TagAddCommand implements CommandClass {

    @Inject private DBManager dbManager;

    @Command(names = "")
    public boolean addTags(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Add a playertag" + " " + dbManager.message);
        System.out.println(dbManager.mySQL);
        return true;
    }

}
