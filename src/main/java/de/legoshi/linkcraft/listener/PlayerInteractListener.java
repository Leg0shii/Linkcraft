package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.EffectBlockManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.util.message.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private PlayerManager playerManager;
    @Inject private EffectBlockManager blockEffectManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            onBlockClick(event);
        }
    }

    private void onBlockClick(PlayerInteractEvent event) {
        // Could optimized by checking if block is a block that effects can be added to, could limit to: player heads, signs, chests, ect for now
        // May require passing the abstract player to execute effects as well
        Player p = event.getPlayer();
        AbstractPlayer player = playerManager.getPlayer(p);


        // Don't execute effects if they have an effect to add
        if(blockEffectManager.addEffect(event)) {
            // confirmation message
        } else if(blockEffectManager.removeEffect(event)) {
            // confirmation message
        } else {
            if (!player.canUseEffectBlocks()) {
                p.sendMessage(Messages.EFFECT_BLOCK_NO_PERMISSION.getMessage());
                return;
            }

            blockEffectManager.executeEffects(event);
        }
    }

}
