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
    public boolean isPlayingCourse() {
        return true;
    }

}
