package de.legoshi.linkcraft.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StandardMap {

    private int id = -1;
    private String mapName = "null";
    private MapType mapType = MapType.STANDARD;
    private MapLength mapLength = MapLength.STANDARD;
    private double difficulty = -1;
    private String builderNames = "null";
    private String releaseDate = "null";
    private int locationId = -1;

    public StandardMap(String mapName, int mapType, int mapLength, double difficulty, String builderNames, int locId) {
        this.locationId = locId;
        this.mapName = mapName;
        this.mapType = MapType.values()[mapType];
        this.mapLength = MapLength.values()[mapLength];
        this.difficulty = difficulty;
        this.builderNames = builderNames;
    }

    public StandardMap(String mapName, int mapType, int mapLength, double difficulty, String releaseDate, String builderNames) {
        this.mapName = mapName;
        this.mapType = MapType.values()[mapType];
        this.mapLength = MapLength.values()[mapLength];
        this.difficulty = difficulty;
        this.releaseDate = releaseDate;
        this.builderNames = builderNames;
    }

    public StandardMap(String mapName, int mapType, int mapLength, double difficulty, String releaseDate) {
        this.mapName = mapName;
        this.mapType = MapType.values()[mapType];
        this.mapLength = MapLength.values()[mapLength];
        this.difficulty = difficulty;
        this.releaseDate = releaseDate;
    }

}
