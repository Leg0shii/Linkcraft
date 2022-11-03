package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.IPlayer;
import de.legoshi.linkcraft.player.RankupPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager {

    @Getter
    private final HashMap<Player, AbstractPlayer> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public void playerJoin(Player player) {
        // issue: player might have joined before
        AbstractPlayer iPlayer;
        if (!player.hasPlayedBefore()) {
            iPlayer = new RankupPlayer(player, new PlayerTag());
        } else {
            // load tag from database
            // determine state of player
            iPlayer = new RankupPlayer(player, new PlayerTag());
        }

        hashMap.put(player, iPlayer);
    }

    public void playerQuit(Player player) {
        IPlayer iPlayer = hashMap.get(player);
        hashMap.remove(player);
    }

}
