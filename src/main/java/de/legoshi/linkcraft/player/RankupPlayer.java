package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.entity.Player;

public class RankupPlayer extends AbstractPlayer {

    public RankupPlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

    @Override
    public void playerEndSignClick() {
        // teleport player to spawn
        playerRankUp();
        // send message in chat and play sound
    }

    private void playerRankUp() {
        // update player permission to next rank
    }

}
