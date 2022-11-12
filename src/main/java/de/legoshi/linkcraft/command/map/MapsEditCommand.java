package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "edit")
public class MapsEditCommand implements CommandClass {

    @Command(names = "")
    public boolean edit(CommandSender sender) {

        return true;
    }

}
