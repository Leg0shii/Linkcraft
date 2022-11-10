package de.legoshi.linkcraft.gui;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;


public abstract class GUIPane {

    protected InventoryGui parent;
    protected InventoryGui current;

    protected Player holder;
    protected StaticGuiElement returnToParent;

    public GUIPane(Player player, InventoryGui parent) {
        this.holder = player;
        this.parent = parent;
        loadReturnToParent();
    }

    public void openGui() {
        this.current.show(this.holder);
    }

    protected abstract void registerGuiElements();

    private void loadReturnToParent() {
        this.returnToParent = new StaticGuiElement('q', new ItemStack(Material.REDSTONE), click -> {
            if (parent != null) {
                this.holder.closeInventory();
                parent.draw(holder);
            }
            return true;
        }, "&c&lBack");
    }

}
