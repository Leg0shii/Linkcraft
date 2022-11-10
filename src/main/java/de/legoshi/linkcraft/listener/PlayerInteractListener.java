package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SignManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.sign.ISign;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import javax.inject.Inject;

public class PlayerInteractListener implements Listener {

    @Inject private PlayerManager playerManager;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        onSignClick(event);
    }

    private void onSignClick(PlayerInteractEvent event) {
        // to be removed/replaced?
        event.getPlayer().sendMessage("Interact! " + event.getClickedBlock().getType().toString());
        if (!event.getClickedBlock().getType().toString().contains("SIGN")) return;
        Block block = event.getClickedBlock();

        for (MetadataValue metadataValue : block.getMetadata("args"))
            event.getPlayer().sendMessage((String) metadataValue.value());

        String[] args = new String[]{event.getMaterial().toString()};
        ISign sign = SignManager.loadSign(args);
        AbstractPlayer player = playerManager.getHashMap().get(event.getPlayer());
        sign.clickSign(player);
    }

}
