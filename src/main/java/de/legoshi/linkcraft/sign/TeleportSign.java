package de.legoshi.linkcraft.sign;

import de.legoshi.linkcraft.player.AbstractPlayer;

public class TeleportSign implements ISign {

    @Override
    public void clickSign(AbstractPlayer player) {
        player.playerTeleportSignClick();
    }

}
