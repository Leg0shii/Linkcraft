package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.player.playertype.RankUpPlayer;

public class PlayerFactory {

    public static AbstractPlayer getPlayerByType(AbstractPlayer abstractPlayer) {
        RankUpPlayer rankUpPlayer = new RankUpPlayer(abstractPlayer.getPlayer(), abstractPlayer.getPlayerTag());
        rankUpPlayer.setPlayThrough(abstractPlayer.getPlayThrough());
        rankUpPlayer.setPlayThroughManager(abstractPlayer.getPlayThroughManager());
        rankUpPlayer.setSaveStateManager(abstractPlayer.getSaveStateManager());
        return rankUpPlayer;
    }

}
