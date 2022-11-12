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
    private double difficulty = -1;
    private String builderNames = "null";
    private String releaseDate = "null";

}
