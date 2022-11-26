package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.manager.LocationManager;
import de.legoshi.linkcraft.manager.MapManager;
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

    @Inject private LocationManager locationManager;
    @Inject private MapManager mapManager;

    @Command(names = "")
    public boolean setStart(CommandSender sender, int mapId) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER, true));
            return false;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Linkcraft.getPlugin(), () -> {
            Location location = player.getLocation();
            locationManager.initObject(location);

            // this is awful
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int id = locationManager.requestIdByPos(location.getX(), location.getY(), location.getZ());
            if (id == -1) player.sendMessage(MessageUtils.composeMessage(Messages.ERROR_FATAL, true));
            else {
                mapManager.updateStartLocation(mapId, id);
                player.sendMessage(MessageUtils.composeMessage(Messages.MAPS_SET_START_MAP, true, mapId));
            }
        });
        return true;
    }

}
