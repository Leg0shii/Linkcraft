package de.legoshi.linkcraft.command.map;


import de.legoshi.linkcraft.command.tag.*;
import de.legoshi.linkcraft.gui.map.MapMenu;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

// TODO:
/*
- Total completions (color based): x completions
- add completion date from user (newest)
- add completion time (duration, fastest attempt?)

- define values for numbers in /maps help

- only allow predefined key values to be set for /maps edit
- have confirmation for deleting maps (showing results on doing that: deleting 5000 saves)
 */

// /maps
@Command(names = "maps", desc = "%translatable:maps.desc%")
@SubCommandClasses({
        MapsAddCommand.class,
        MapsEditCommand.class,
        MapsHelpCommand.class,
        MapsRemoveCommand.class,
        MapsSetVictorCommand.class,
        MapsUnSetVictorCommand.class,
        MapsSetStartCommand.class,
        MapsSetEndCommand.class
})
@ArgOrSub(value = true)
public class MapsCommand implements CommandClass {

    @Inject private Injector injector;

    @Command(names = "")
    public boolean maps(@Sender CommandSender sender, CommandContext commandContext) {
        if (commandContext.getArguments().size() > 1) {
            // send help
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER.getMessage(), true));
            return false;
        }

        injector.getInstance(MapMenu.class).openGui(player, null);
        return true;
    }

}
