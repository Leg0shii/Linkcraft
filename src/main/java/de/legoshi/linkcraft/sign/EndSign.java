package de.legoshi.linkcraft.sign;

import de.legoshi.linkcraft.player.AbstractPlayer;

public class EndSign implements ISign {

    @Override
    public void clickSign(AbstractPlayer player) {
        player.playerEndSignClick();
    }

}
