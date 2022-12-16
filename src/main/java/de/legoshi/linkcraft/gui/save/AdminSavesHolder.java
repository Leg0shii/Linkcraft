package de.legoshi.linkcraft.gui.save;

import de.themoep.inventorygui.InventoryGui;
import org.bukkit.entity.Player;

public class AdminSavesHolder extends SavesHolder {

    private String playerName;

    public void openGui(Player player, InventoryGui parent, String playerName) {
        this.title = "Admin - " + playerName + "'s saves";
        super.openGui(player, parent);
        this.playerName = playerName;
    }

    @Override
    protected void registerGuiElements() {

    }

}
