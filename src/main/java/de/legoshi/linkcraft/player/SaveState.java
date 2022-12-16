package de.legoshi.linkcraft.player;

import lombok.Data;
import org.bukkit.Location;

import java.sql.Date;

@Data
public class SaveState {

    private PlayThrough playThrough;
    private Location saveLocation;

    private String saveStateName = "Save-State";
    private String blockTypeName = "STONE";

    private Date quitDate;

    public SaveState(PlayThrough playThrough, Location saveLocation, Date quitDate) {
        this.saveLocation = saveLocation;
        this.playThrough = playThrough;
        this.quitDate = quitDate;
    }

}
