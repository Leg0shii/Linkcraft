package de.legoshi.linkcraft.player.playertype;

import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.PlayThrough;
import de.legoshi.linkcraft.tag.PlayerTag;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class CoursePlayer extends AbstractPlayer {

    public CoursePlayer(Player player, PlayerTag playerTag) {
        super(player, playerTag);
    }

    @Override
    public void playerJoinMap(StandardMap map) {
        playerLeaveMap();
        super.playerJoinMap(map);
    }

    @Override
    public void playerLeaveMap() {
        saveStateManager.createSaveState(this);
        // write all collected stats to the database (play_through)
        // create a new save linked to the play_through
    }

}
