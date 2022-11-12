package de.legoshi.linkcraft.map;

import de.legoshi.linkcraft.tag.TagRarity;

public enum MapType {

    RANK_UP,
    BONUS,
    STANDARD,
    SEGMENTED,
    MAZE;

    public static int getMapPosition(MapType type) {
        int typeCount = 0;
        for (MapType mapType : MapType.values()) {
            if (mapType.equals(type)) break;
            typeCount++;
        }
        return typeCount;
    }

}
