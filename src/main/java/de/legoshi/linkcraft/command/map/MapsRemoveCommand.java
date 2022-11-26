package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

// /maps remove <map-id>
@Command(names = "remove")
public class MapsRemoveCommand implements CommandClass {

    @Inject private MapManager mapManager;

    @Command(names = "")
    public boolean remove(CommandSender sender, int id) {
        boolean success = mapManager.deleteObject(String.valueOf(id));
        if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_REMOVE_MAP, true, id));
        else sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_REMOVE_ERROR, true));
        return true;
    }

}
