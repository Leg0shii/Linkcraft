package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@RequiredArgsConstructor
@Command(names = "add", permission = "lc.tags.add")
public class TagAddCommand implements CommandClass {

    // @Inject private final DBManager dbManager;

    @Command(names = "")
    public boolean addTags(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Add a playertag");
        //System.out.println(dbManager.message);
        return true;
    }

}
