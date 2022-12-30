package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.PracticeManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private PlayerManager playerManager;
    @Inject private PracticeManager practiceManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            onRightClick(event);
        }
    }

    private void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack held = player.getInventory().getItemInHand();

        if(held != null && !held.getType().equals(Material.AIR)) {
            onItemUsage(event, held);
        }
    }

    private void onItemUsage(PlayerInteractEvent event, ItemStack held) {
        if(held.getType().equals(Material.SLIME_BALL) && ItemUtils.hasNbtId(held, "prac")) {
            practiceAction(event);
        }
    }

    private void practiceAction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        AbstractPlayer aPlayer = playerManager.getPlayer(player);

        if(aPlayer.canUsePractice()) {
            practiceManager.back(player);
            event.setCancelled(true);
        }
    }

}
