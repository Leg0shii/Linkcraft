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
    public void playerCPSignClick() {

    }

    @Override
    public void playerEndSignClick() {

    }

    @Override
    public HashMap<String, Object> getKeyValueList() {
        return null;
    }

}
