package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.PracticeManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

public class QuitListener implements Listener {

    @Inject private PlayerManager playerManager;
    @Inject private PracticeManager practiceManager;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        AbstractPlayer abstractPlayer = playerManager.getPlayer(player);
        if (abstractPlayer.isPlayingCourse()) playerManager.saveSaveState(player);
        playerManager.playerQuit(player);
        practiceManager.removeFromCache(player);
        MessageUtils.broadcast(Messages.PLAYER_LEAVE, true, player.getName());
    }
}
