package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class StaffPlayer extends AbstractPlayer {

    public StaffPlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

}
