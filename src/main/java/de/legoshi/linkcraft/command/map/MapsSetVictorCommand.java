package de.legoshi.linkcraft.command.map;

import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import java.sql.SQLIntegrityConstraintViolationException;

// /maps setvictor <map-id> <user-name>
@Command(names = "setvictor")
public class MapsSetVictorCommand implements CommandClass {

    @Inject private MapManager mapManager;

    @Command(names = "")
    public boolean setVictor(CommandSender sender, String userName, int id) {
        try {
            String uuid = Bukkit.getOfflinePlayer(userName).getUniqueId().toString();
            boolean success = mapManager.addVictor(id, uuid);
            if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_SET_VICTOR_MAP, true, userName, id));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.MAPS_VICTOR_ERROR, true, id));
        } catch (Exception e) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.PLAYER_NOT_EXIST, true));
        }
        return true;
    }

}
