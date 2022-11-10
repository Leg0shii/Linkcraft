package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.tag.TagRarity;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class TagMenu extends GUIPane {

    private final String title = "Tag Menu";
    private final String[] guiSetup = {
            "         ",
            " a b c d ",
            "         ",
    };

    public TagMenu(Player player, InventoryGui parent) {
        super(player, parent);
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
    }

    @Override
    protected void registerGuiElements() {
        // use different heads as items later
        StaticGuiElement basicElement = new StaticGuiElement('a', new ItemStack(Material.COAL),click -> {
            new TagHolder(this.holder, this.current, TagRarity.BASIC).openGui();
            return true;
        }, "&a&lBasic");

        StaticGuiElement rareElement = new StaticGuiElement('b', new ItemStack(Material.IRON_INGOT),click -> {
            new TagHolder(this.holder, this.current, TagRarity.RARE).openGui();
            return true;
        }, "&a&lRare");

        StaticGuiElement epicElement = new StaticGuiElement('c', new ItemStack(Material.GOLD_INGOT),click -> {
            new TagHolder(this.holder, this.current, TagRarity.EPIC).openGui();
            return true;
        }, "&a&lEpic");

        StaticGuiElement legendaryElement = new StaticGuiElement('d', new ItemStack(Material.DIAMOND),click -> {
            new TagHolder(this.holder, this.current, TagRarity.LEGENDARY).openGui();
            return true;
        }, "&a&lLegendary");

        current.addElements(basicElement, rareElement, epicElement, legendaryElement);
    }

}
