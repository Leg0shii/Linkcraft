package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.tag.TagRarity;
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

    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        this.current = new InventoryGui((JavaPlugin) plugin, player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        // use different heads as items later
        StaticGuiElement basicElement = new StaticGuiElement('a', new ItemStack(Material.COAL),click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.BASIC);
            return true;
        }, "&a&lBasic");

        StaticGuiElement rareElement = new StaticGuiElement('b', new ItemStack(Material.IRON_INGOT),click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.RARE);
            return true;
        }, "&a&lRare");

        StaticGuiElement epicElement = new StaticGuiElement('c', new ItemStack(Material.GOLD_INGOT),click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.EPIC);
            return true;
        }, "&a&lEpic");

        StaticGuiElement legendaryElement = new StaticGuiElement('d', new ItemStack(Material.DIAMOND),click -> {
            tagHolder.openGui(this.holder, this.current, TagRarity.LEGENDARY);
            return true;
        }, "&a&lLegendary");

        current.addElements(basicElement, rareElement, epicElement, legendaryElement);
    }

}
