package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.LocationManager;
import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

// /maps add <name> <type> <length> <difficulty> <builders>
@Command(names = "add")
public class MapsAddCommand implements CommandClass {

    @Inject private MapManager mapManager;
    @Inject private LocationManager locationManager;

    @Command(names = "")
    public boolean add(CommandSender sender,
                       @Named("name") String name,
                       @Named("type") int type,
                       @Named("length") int length,
                       @Named("difficulty") double difficulty,
                       @Named("builders") String builders
    ) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER.getMessage(), true));
            return false;
        }

        Player player = (Player)sender;

        Location spawn = player.getLocation();
        StandardMap standardMap = new StandardMap(name, type, length, difficulty, builders, spawn);
        int id = mapManager.initObject(standardMap);

        if (id != -1) sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_ADD_MAP, true, name, difficulty));
        else sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_ADD_ERROR, true));
        return true;
    }
}
