package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@RequiredArgsConstructor
public class ChatListener implements Listener {

    private final PlayerManager playerManager;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        AbstractPlayer aPlayer = playerManager.getHashMap().get(player);
        String playerMessage = event.getMessage();

        String updatedMessage = aPlayer.getPlayerTag().getDisplayName() + " "
                + player.getDisplayName() + "> "
                + playerMessage;
        for (Player all : Bukkit.getOnlinePlayers()) all.sendMessage(updatedMessage);
    }

}
