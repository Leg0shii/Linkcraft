package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.database.Saveable;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public abstract class AbstractPlayer implements IPlayer, Saveable {

    protected Player player;
    protected PlayerTag playerTag;

    @Override
    public void playerCPSignClick(String location) {

    }

    @Override
    public void playerEndSignClick() {

    }

    @Override
    public void playerTagSignClick() {

    }

    @Override
    public void playerTeleportSignClick() {

    }

    // Could make abstract...
    @Override
    public boolean canUseEffectBlocks() {
        return true;
    }


}
