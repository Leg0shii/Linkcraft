package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.BlockEffectManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.util.message.Messages;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private PlayerManager playerManager;
    @Inject private BlockEffectManager blockEffectManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasBlock()) {
            onBlockClick(event);
        }
    }

    private void onBlockClick(PlayerInteractEvent event) {

        // Could optimized by checking if block is a block that effects can be added to, could limit to: player heads, signs, chests, ect for now
        // May require passing the abstract player to execute effects as well
        Player p = event.getPlayer();
        AbstractPlayer player = playerManager.getPlayer(p);
        if(player.canUseEffectBlocks()) {
            Block block = event.getClickedBlock();
            if(blockEffectManager.isEffectBlock(block)) {
                blockEffectManager.executeEffects(event);
            }
        } else {
            // not sure if this is how we send messages anymore
            p.sendMessage(Messages.EFFECT_BLOCK_NO_PERMISSION.getMessage());
        }
    }

}
