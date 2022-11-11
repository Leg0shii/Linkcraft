package de.legoshi.linkcraft.player;

public interface IPlayer {

    void playerCPSignClick(String location);

    void playerEndSignClick();

    void playerTagSignClick();

    void playerTeleportSignClick();

    boolean canUseEffectBlocks();

}
