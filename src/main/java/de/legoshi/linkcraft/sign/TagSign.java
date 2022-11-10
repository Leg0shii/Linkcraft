package de.legoshi.linkcraft.sign;

import de.legoshi.linkcraft.player.AbstractPlayer;

public class TagSign implements ISign {

    @Override
    public void clickSign(AbstractPlayer player) {
        player.playerTagSignClick();
    }

}
