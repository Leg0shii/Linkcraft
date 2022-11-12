package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.util.CustomHeads;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class TagMenu extends GUIPane {

    @Inject private TagHolder tagHolder;

    private final String title = "Tag Menu";
    private final String[] guiSetup = {
            "         ",
            " a b c d ",
            "         ",
    };

    @Override
    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        this.current = new InventoryGui((JavaPlugin) plugin, player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        StaticGuiElement basicElement = new StaticGuiElement('a', CustomHeads.commonTagHead, click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.COMMON, 1);
            return true;
        }, "&7&lBasic");

        StaticGuiElement rareElement = new StaticGuiElement('b', CustomHeads.rareTagHead,click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.RARE, 1);
            return true;
        }, "&3&lRare");

        StaticGuiElement epicElement = new StaticGuiElement('c', CustomHeads.epicTagHead,click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.EPIC, 1);
            return true;
        }, "&5&lEpic");

        StaticGuiElement legendaryElement = new StaticGuiElement('d', CustomHeads.legendaryTagHead,click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.LEGENDARY, 1);
            return true;
        }, "&6&lLegendary");

        current.addElements(basicElement, rareElement, epicElement, legendaryElement);
    }

}
