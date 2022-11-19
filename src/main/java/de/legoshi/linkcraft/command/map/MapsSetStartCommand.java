package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

// /maps setstart
@Command(names = "setstart")
public class MapsSetStartCommand implements CommandClass {

    @Command(names = "")
    public boolean remove(CommandSender sender) {
        sender.sendMessage("To be implemented");
        return true;
    }

}
