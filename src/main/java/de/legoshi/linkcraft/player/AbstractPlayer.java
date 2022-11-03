package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public abstract class AbstractPlayer implements IPlayer {

    private Player player;
    private PlayerTag playerTag;

}
