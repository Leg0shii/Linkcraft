package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

// /maps setend <sign:plate>
@Command(names = "setend")
public class MapsSetEndCommand implements CommandClass {

    // bind to sign/plate which still has to be implemented
    @Command(names = "")
    public boolean remove(CommandSender sender) {
        sender.sendMessage("To be implemented");
        return true;
    }

}
