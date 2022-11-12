package de.legoshi.linkcraft.gui.map;

import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.util.CustomHeads;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;

public class MapMenu extends GUIPane {

    @Inject private MapHolder mapHolder;

    private final String title = "Map Menu";
    private final String[] guiSetup = {
            "         ",
            "    e    ",
            "         ",
            " a b c d ",
            "         ",
            "         "
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
        StaticGuiElement rankUpElement = new StaticGuiElement('e', new ItemStack(Material.NETHER_STAR), click -> {
            mapHolder.openGui(this.holder, this.current, MapType.RANK_UP, 1);
            return true;
        }, "&f&lRankUp");

        StaticGuiElement bonusElement = new StaticGuiElement('a', new ItemStack(Material.DIAMOND_BLOCK) ,click -> {
            mapHolder.openGui(this.holder, this.current, MapType.BONUS, 1);
            return true;
        }, "&3&lBonus");

        StaticGuiElement mazeElement = new StaticGuiElement('b', new ItemStack(Material.GOLD_BLOCK),click -> {
            mapHolder.openGui(this.holder, this.current, MapType.MAZE, 1);
            return true;
        }, "&6&lMaze");

        StaticGuiElement rankUpStyleElement = new StaticGuiElement('c', new ItemStack(Material.IRON_BLOCK) ,click -> {
            mapHolder.openGui(this.holder, this.current, MapType.STANDARD, 1);
            return true;
        }, "&f&lRankUp-Style");

        StaticGuiElement segmentedElement = new StaticGuiElement('d', new ItemStack(Material.HARD_CLAY),click -> {
            mapHolder.openGui(this.holder, this.current, MapType.SEGMENTED, 1);
            return true;
        }, "&5&lSegmented");

        current.addElements(rankUpElement, bonusElement, mazeElement, rankUpStyleElement, segmentedElement);
    }

}
