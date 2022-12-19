package de.legoshi.linkcraft.command;

import de.legoshi.linkcraft.manager.PlayerManager;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(names = "spawn")
public class SpawnCommand implements CommandClass {

    @Inject private PlayerManager playerManager;

    @Command(names = "")
    public boolean maps(@Sender CommandSender sender) {
        Player player = (Player) sender;
        playerManager.playerLeaveMap(player);
        player.performCommand("warp spawn");
        return true;
    }

}
