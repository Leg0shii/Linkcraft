package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

// /maps edit <name:type:difficulty:builders:release:length> <map_id> <value>
@Command(names = "edit")
public class MapsEditCommand implements CommandClass {

    @Inject
    private MapManager mapManager;

    @Command(names = "")
    public boolean edit(CommandSender sender,
                        @Named(value = "map_id") int map_id,
                        @Named(value = "type") String type,
                        @Named(value = "value") String value) {
        StandardMap standardMap = mapManager.requestObjectById(map_id);
        switch (type) {
            case "name" -> standardMap.setMapName(value);
            case "type" -> standardMap.setMapType(value);
            case "difficulty" -> standardMap.setDifficulty(value);
            case "builders" -> standardMap.setBuilderNames(value);
            case "release" -> standardMap.setReleaseDate(value);
            case "length" -> standardMap.setMapLength(value);
        }

        boolean success = mapManager.updateObject(standardMap);
        if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_EDIT_MAP, true, map_id, type, value));
        else sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_EDIT_ERROR, true));
        return true;
    }

}
