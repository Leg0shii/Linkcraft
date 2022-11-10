package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.tag.TagRarity;
import de.themoep.inventorygui.InventoryGui;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TagHolder extends GUIScrollable {

    private String title;
    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggf",
            "ggggggggq",
            "ggggggggf",
            "ggggggggd",
    };

    public void openGui(Player player, InventoryGui parent, TagRarity rarity) {
        super.openGui(player, parent);
        this.title = rarity.name() + " tags";
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        // load in all tags for the category from the player
        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent);
    }

}
