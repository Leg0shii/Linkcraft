package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import lombok.RequiredArgsConstructor;
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
    }
}
