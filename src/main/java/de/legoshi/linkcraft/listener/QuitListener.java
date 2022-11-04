package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.util.message.Message;
import de.legoshi.linkcraft.util.message.Prefix;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class QuitListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        playerManager.playerQuit(player);

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(Message.PLAYER_LEAVE.msg(Prefix.INFO, player.getName()));
        }
    }
}
