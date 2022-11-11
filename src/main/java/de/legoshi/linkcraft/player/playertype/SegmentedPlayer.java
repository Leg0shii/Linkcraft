package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class SegmentedPlayer extends AbstractPlayer {

    public SegmentedPlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

    @Override
    public void playerCPSignClick(String location) {
        // Update current map CP with new location string..
    }

    @Override
    public void playerEndSignClick() {

    }

}
