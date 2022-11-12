package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "setvictor")
public class MapsSetVictorCommand implements CommandClass {

    @Command(names = "")
    public boolean setVictor(CommandSender sender) {

        return true;
    }

}
