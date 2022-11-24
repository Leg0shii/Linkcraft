package de.legoshi.linkcraft.gui;

import de.legoshi.linkcraft.database.DBManager;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;


public abstract class GUIPane {

    protected InventoryGui parent;
    protected InventoryGui current;

    protected Player holder;
    protected StaticGuiElement returnToParent;

    public void openGui(Player player, InventoryGui parent) {
        this.holder = player;
        this.parent = parent;
        loadReturnToParent();
    }

    protected abstract void registerGuiElements();

    private void loadReturnToParent() {
        this.returnToParent = new StaticGuiElement('q', new ItemStack(Material.REDSTONE), click -> {
            if (parent != null) {
                parent.show(holder);
            }
            return true;
        }, "&c&lBack");
    }

    protected void fullCloseOnEsc() {
        this.current.setCloseAction(close -> false);
    }

}
