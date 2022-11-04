package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.util.message.Message;
import de.legoshi.linkcraft.util.message.Prefix;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class JoinListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        playerManager.playerJoin(player);

        for (Player all : Bukkit.getOnlinePlayers()) {
            all.sendMessage(Message.PLAYER_JOIN.msg(Prefix.INFO, player.getName()));
        }
    }
}
