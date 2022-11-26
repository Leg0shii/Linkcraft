package de.legoshi.linkcraft.gui.map;

import de.legoshi.linkcraft.Linkcraft;
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
import team.unnamed.inject.Injector;

import javax.inject.Inject;
import java.util.Map;

public class MapMenu extends GUIPane {

    @Inject private Injector injector;
    private MapHolder mapHolder;

    private final String title = "Map Menu";
    private final String[] guiSetup = {
            "         ",
            "    e    ",
            "         ",
            " a b c d ",
            "         ",
            "        f"
    };

    @Override
    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
        this.mapHolder = injector.getInstance(MapHolder.class);
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

        StaticGuiElement noneElement = new StaticGuiElement('f', new ItemStack(Material.BARRIER), click -> {
            mapHolder.openGui(this.holder, this.current, MapType.NONE, 1);
            return true;
        }, "&c&lBroken");

        // permissions to check if player is staff (for noneElement maps)
        current.addElements(rankUpElement, bonusElement, mazeElement, rankUpStyleElement, segmentedElement, noneElement);
    }

}
