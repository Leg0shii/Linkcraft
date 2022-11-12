package de.legoshi.linkcraft.map;

public enum MapLength {

    EXTREMELY_LONG,
    STANDARD,
    EXTREMELY_SHORT;

    public static int getMapLengthPosition(MapLength type) {
        int typeCount = 0;
        for (MapLength mapType : MapLength.values()) {
            if (mapType.equals(type)) break;
            typeCount++;
        }
        return typeCount;
    }

}
