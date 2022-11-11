package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StandardPlayer extends AbstractPlayer {

    public StandardPlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

}
