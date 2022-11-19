package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

// /maps unsetvictor <map-id> <user-name:all>
@Command(names = "unsetvictor")
public class MapsUnSetVictorCommand implements CommandClass {

    @Inject
    private MapManager mapManager;

    @Command(names = "")
    public boolean unsetVictor(CommandSender sender, int id, String userName) {
        try {
            String uuid = Bukkit.getOfflinePlayer(userName).getUniqueId().toString();
            mapManager.removeVictor(id, uuid);
        } catch (Exception e) {
            sender.sendMessage("This player doesnt exist...");
        }
        return true;
    }

}
