package de.legoshi.linkcraft.blockeffect;

import de.legoshi.linkcraft.manager.PlayerManager;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.inject.Inject;

public abstract class BlockEffect {

    @Inject protected PlayerManager playerManager;
    public abstract void executeEffect(PlayerInteractEvent event);
}
