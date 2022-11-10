package de.legoshi.linkcraft.gui;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIScrollable extends GUIPane {

    protected StaticGuiElement pageUp;
    protected StaticGuiElement pageDown;

    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        registerPageElements();
    }

    private void registerPageElements() {
        this.pageUp = new StaticGuiElement('u', new ItemStack(Material.ARROW, 1), click -> {
            // simply reload the page but with an SQL offset by the pageVolume * page
            return true;
        }, "Previous Page");

        this.pageDown = new StaticGuiElement('d', new ItemStack(Material.ARROW, 1), click -> {

            return true;
        }, "Next Page");
    }

}
