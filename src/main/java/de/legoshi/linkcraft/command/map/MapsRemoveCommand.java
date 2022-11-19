package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
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
        mapManager.deleteObject(String.valueOf(id));
        sender.sendMessage("Successfully deleted map with id " + id);
        return true;
    }

}
