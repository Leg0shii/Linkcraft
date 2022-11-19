package de.legoshi.linkcraft.command.map;


import de.legoshi.linkcraft.command.tag.*;
import de.legoshi.linkcraft.gui.map.MapMenu;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

// /maps
@Command(names = "maps", desc = "%translatable:maps.desc%")
@SubCommandClasses({
        MapsAddCommand.class,
        MapsEditCommand.class,
        MapsHelpCommand.class,
        MapsRemoveCommand.class,
        MapsSetVictorCommand.class,
        MapsUnSetVictorCommand.class
})
@ArgOrSub(value = true)
public class MapsCommand implements CommandClass {

    @Inject private MapMenu mapMenu;

    @Command(names = "")
    public boolean maps(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            // send help
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("no player");
            return false;
        }

        mapMenu.openGui(player, null);
        return true;
    }

}
