package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.player.IPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerManager {

    private HashMap<Player, IPlayer> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public void playerJoin(Player player) {
        IPlayer iPlayer = null; // load from database
        hashMap.put(player, iPlayer);
    }

    public void playerQuit(Player player) {
        IPlayer iPlayer = hashMap.get(player); // save to database
        hashMap.remove(player);
    }

}
