package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class JoinListener implements Listener {

    @Inject private PlayerManager playerManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        // we can just use this method instead of broadcasting (unless there is a reason to)
        event.setJoinMessage("");
        Player player = event.getPlayer();
        playerManager.playerJoin(player);
        MessageUtils.broadcast(Messages.PLAYER_JOIN, true, player.getName());
    }
}
