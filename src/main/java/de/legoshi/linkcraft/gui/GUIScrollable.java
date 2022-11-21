package de.legoshi.linkcraft.gui;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIScrollable extends GUIPane {

    protected StaticGuiElement pageUp;
    protected StaticGuiElement pageDown;
    protected int page = 0;
    protected int pageVolume = 40;

    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        registerPageElements();
    }

    protected abstract boolean getPage();

    private void registerPageElements() {
        this.pageUp = new StaticGuiElement('u', new ItemStack(Material.ARROW, 1), (click -> {
            if(page > 0) {
                page--;
                getPage();
                click.getGui().setPageNumber(page);
            }
            return true;
        }), "Previous page");

        this.pageDown = new StaticGuiElement('d', new ItemStack(Material.ARROW, 1), (click -> {
            page++;
            if(getPage()) {
                click.getGui().setPageNumber(page);
            } else {
                page--;
            }
            return true;
        }), "Next page");
    }
}
