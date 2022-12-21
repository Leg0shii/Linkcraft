package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.manager.LocationManager;
import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

// /maps setstart
@Command(names = "setstart")
public class MapsSetStartCommand implements CommandClass {

    @Inject private MapManager mapManager;
    @Inject private LocationManager locationManager;

    @Command(names = "")
    public boolean setStart(CommandSender sender, int mapId) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER, true));
            return true;
        }

        Player player = (Player)sender;

        Location currentLocation = player.getLocation();
        StandardMap standardMap = mapManager.requestObjectById(mapId);

        locationManager.updateLocation(standardMap.getLocationID(), currentLocation);
        standardMap.setMapSpawn(currentLocation);
        return true;
    }

}
