package de.legoshi.linkcraft.tag;

import de.legoshi.linkcraft.map.MapType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerTag {

    private String tagID = "1";
    private String displayName = "";
    private String description = "Default Tag";
    private TagRarity tagRarity = TagRarity.COMMON;

    public PlayerTag(String displayName, String description, int tagRarity) {
        this.displayName = displayName;
        this.description = description;
        this.tagRarity = TagRarity.values()[tagRarity];
    }

    public void setTagRarity(String value) {
        try {
            this.tagRarity = TagRarity.values()[Integer.parseInt(value)];
        } catch (Exception e) {
            this.tagRarity = TagRarity.NONE;
        }
    }

}
