package de.legoshi.linkcraft.blockeffect;

import de.legoshi.linkcraft.player.AbstractPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

@RequiredArgsConstructor
public class BlockCheckpointEffect extends BlockEffect {

    private final String location;

    @Override
    public void executeEffect(PlayerInteractEvent event) {
        AbstractPlayer player = playerManager.getPlayer(event.getPlayer());
        player.playerCPSignClick(location);
    }

}
