package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class MazePlayer extends AbstractPlayer {

    public MazePlayer(Player player, PlayerTag playerTag) {
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
