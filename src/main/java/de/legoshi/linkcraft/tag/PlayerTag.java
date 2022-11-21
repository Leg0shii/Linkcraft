package de.legoshi.linkcraft.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerTag {

    private int tagID = 0;
    private String displayName = "";
    private String description = "Default Tag";
    private TagRarity tagRarity = TagRarity.COMMON;
    private TagType tagType = TagType.HIDDEN;

    public PlayerTag(String displayName, String description, int tagRarity, int type) {
        this.displayName = displayName;
        this.description = description;
        this.tagRarity = TagRarity.values()[tagRarity];
        this.tagType = TagType.values()[type];
    }
}
