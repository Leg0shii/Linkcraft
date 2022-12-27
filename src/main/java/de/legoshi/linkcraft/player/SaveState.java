package de.legoshi.linkcraft.player;

import lombok.Data;
import org.bukkit.Location;

import java.sql.Date;

@Data
public class SaveState {

    private int saveID;
    private PlayThrough playThrough;
    private Location saveLocation;
    private int locationID; // update location

    private String saveStateName = "Save-State";
    private String blockTypeName = "STONE";

    private Date quitDate;
    private boolean loaded;

    public SaveState(PlayThrough playThrough, Location saveLocation, Date quitDate) {
        this.saveLocation = saveLocation;
        this.playThrough = playThrough;
        this.quitDate = quitDate;
    }

}
