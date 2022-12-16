package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.entity.Player;

public class RankUpPlayer extends CoursePlayer {

    public RankUpPlayer(Player player, PlayerTag playerTag) {
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
