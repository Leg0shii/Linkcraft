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

    private String tagID = "-1";
    private String displayName = "[Test]";
    private String description = "Test Description";
    private TagRarity tagRarity = TagRarity.RARE;

}
