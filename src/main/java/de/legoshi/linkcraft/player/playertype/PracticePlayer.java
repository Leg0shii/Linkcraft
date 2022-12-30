package de.legoshi.linkcraft.player.playertype;


import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class PracticePlayer extends AbstractPlayer {

    private final Class<?> previous;

    public PracticePlayer(Player player, PlayerTag playerTag, Class<?> previous) {
        super(player, playerTag);
        this.previous = previous;
    }

    @Override
    public boolean canPractice() { return false; }

    @Override
    public boolean canUsePractice() { return true; }

    @Override
    public boolean canUnpractice() { return true; }
}
