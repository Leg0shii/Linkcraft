package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private PlayerManager playerManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            onBlockClick(event);
        }
    }

    private void onBlockClick(PlayerInteractEvent event) {

    }

}
