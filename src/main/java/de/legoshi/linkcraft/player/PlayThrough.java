package de.legoshi.linkcraft.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class PlayThrough {

    private int ptID;
    private int mapID;
    private String userID;

    private boolean completion = false;
    private long completionDate = 0;

    private long joinDate = -1;
    private int pracUsages = 0;
    private long timePlayed = 0;
    private int currentJumps;

    public PlayThrough(Player player, int mapID) {
        this.mapID = mapID;
        this.userID = player.getUniqueId().toString();
        this.currentJumps = player.getStatistic(Statistic.JUMP);
    }

}
