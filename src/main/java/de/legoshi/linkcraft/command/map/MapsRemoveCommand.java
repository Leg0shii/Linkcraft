package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "remove")
public class MapsRemoveCommand implements CommandClass {

    @Command(names = "")
    public boolean remove(CommandSender sender) {

        return true;
    }

}
