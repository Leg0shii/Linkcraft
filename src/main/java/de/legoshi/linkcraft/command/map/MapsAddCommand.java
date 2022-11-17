package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.LocationManager;
import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.StandardMap;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("You are not a player..");
            return false;
        }
        Location spawn = player.getLocation();
        locationManager.initObject(spawn);
        int mapId = locationManager.requestIdByPos(spawn.getX(), spawn.getY(), spawn.getZ());

        StandardMap standardMap = new StandardMap(name, type, length, difficulty, builders, mapId);
        mapManager.initObject(standardMap);
        sender.sendMessage("Successfully added new map " + name + " with type " + standardMap.getMapType().name()
                + " and length " + MapLength.values()[length].name() + " and difficulty " + difficulty + " and the builders " + builders);
        return true;
    }
}
