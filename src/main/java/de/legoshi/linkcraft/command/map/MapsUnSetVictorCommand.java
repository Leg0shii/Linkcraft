package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
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
            boolean success = mapManager.removeVictor(id, uuid);
            if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_UNSET_VICTOR_MAP, true));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_UNSET_VICTOR_ERROR, true));
        } catch (Exception e) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.PLAYER_NOT_EXIST, true));
        }
        return true;
    }

}
