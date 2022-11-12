package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "add")
public class MapsAddCommand implements CommandClass {

    @Command(names = "")
    public boolean add(CommandSender sender) {

        return true;
    }
}
