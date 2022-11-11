package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public abstract class AbstractPlayer implements IPlayer {

    protected Player player;
    @Setter protected PlayerTag playerTag;

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
