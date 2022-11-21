package de.legoshi.linkcraft.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TagData {
    private final int tagId;
    private final String name;
    private final int rarity;
    private final int type;
    private final String description;
    private final String date;
    private final int ownedBy;
}
