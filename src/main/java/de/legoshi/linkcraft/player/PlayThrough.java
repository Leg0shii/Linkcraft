package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.map.StandardMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.sql.Date;

@Data
@AllArgsConstructor
public class PlayThrough {

    private Player player;

    private int ptID;
    private StandardMap map;
    private String userID;

    private boolean completion = false;
    private Date completionDate = null;

    private Date joinDate = new Date(System.currentTimeMillis());
    private int pracUsages = 0;
    private long timePlayedNormal = 0;
    private long timePlayedPrac = 0;
    private int currentJumps;

    private long sessionNormalTime;
    private long sessionPracTime;
    private int sessionJumps;

    public PlayThrough() {

    }

    public PlayThrough(Player player, StandardMap map) {
        this.player = player;
        this.map = map;
        this.userID = player.getUniqueId().toString();
        startSession();
    }

    public void startSession() {
        this.sessionJumps = player.getStatistic(Statistic.JUMP);
        this.sessionNormalTime = System.currentTimeMillis();
    }

    public void updatePT() {
        this.currentJumps = currentJumps + (player.getStatistic(Statistic.JUMP) - sessionJumps);
        this.timePlayedNormal = timePlayedNormal + (System.currentTimeMillis() - sessionNormalTime);
        this.timePlayedPrac = timePlayedPrac + sessionPracTime;
    }

    public void startPracTime() {
        this.sessionPracTime = System.currentTimeMillis();
    }

    public void updatePracTime() {
        this.timePlayedPrac = timePlayedPrac + (System.currentTimeMillis() - sessionPracTime);
    }

}
