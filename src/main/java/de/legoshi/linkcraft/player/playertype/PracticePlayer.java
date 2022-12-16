package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class PracticePlayer extends AbstractPlayer {

    public PracticePlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

    // what happens if practice player leaves
    // we gotta store last position from where player went into practice, and store that as save
    @Override
    public void playerJoinMap(StandardMap map) {
        this.player.sendMessage("You can't join a new map while being in practice");
    }

}
