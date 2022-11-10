package de.legoshi.linkcraft.sign;

import de.legoshi.linkcraft.player.AbstractPlayer;
import org.bukkit.entity.Player;

public class CheckPointSign implements ISign {

    @Override
    public void clickSign(AbstractPlayer player) {
        player.playerCPSignClick();
    }

}
