package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;

public class ChatListener implements Listener {

    @Inject private PlayerManager playerManager;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        AbstractPlayer aPlayer = playerManager.getHashMap().get(player);
        String message = aPlayer.chat(event.getMessage());
        for (Player all : Bukkit.getOnlinePlayers()) all.sendMessage(message);
    }

}
