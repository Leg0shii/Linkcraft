package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        String playerMessage = event.getMessage();

        String updatedMessage = ChatColor.translateAlternateColorCodes('&', aPlayer.getPlayerTag().getDisplayName()) + " §r"
                + ChatColor.translateAlternateColorCodes('&', player.getDisplayName()) + "§7» "
                + playerMessage;

        //updatedMessage = "§4§l#12 §b«§3«§2«§a§lXVII§2»§3»§b» §b§0" + player.getDisplayName() + " §7» " + playerMessage;
        for (Player all : Bukkit.getOnlinePlayers()) all.sendMessage(updatedMessage);
    }

}
