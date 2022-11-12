package de.legoshi.linkcraft.command.map;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

@Command(names = "unsetvictor")
public class MapsUnSetVictorCommand implements CommandClass {

    @Command(names = "")
    public boolean unsetVictor(CommandSender sender) {

        return true;
    }

}
