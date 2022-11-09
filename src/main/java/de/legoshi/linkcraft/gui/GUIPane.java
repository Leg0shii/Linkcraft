package de.legoshi.linkcraft.gui;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GUIPane {

    private GUIPane parent;
    private Player player;
    private String[] guiSetup;

    private StaticGuiElement returnToParent;

    private void loadReturnToParent() {
        this.returnToParent = new StaticGuiElement('a', new ItemStack(Material.REDSTONE), click -> {
            player.closeInventory();
            // player.openInventory(parent.get);
            return true;
        }, "&a&lNew Replays");
    }

}
