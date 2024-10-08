package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import org.bukkit.Location;

public interface IPlayer {

    void playerCPSignClick(String location);

    void playerEndSignClick();

    void playerTagSignClick();

    void playerTeleportSignClick();

    boolean isPlayingCourse();
    boolean canPractice();
    boolean canUsePractice();

    boolean canRemoveTags();

    boolean canUnpractice();

}
