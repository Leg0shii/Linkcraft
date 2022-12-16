package de.legoshi.linkcraft.gui.save;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SavesHolder extends GUIScrollable {

    @Inject private DBManager dbManager;

    protected String title;
    private final String[] guiSetup = {
            "ggggggggg",
            "ggggggggg",
            "ggggggggg",
            "aaaaaaaaa",
            "qggggggud",
    };

    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        this.title = this.title == null ? "Your Saves" : this.title;
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);

        fullCloseOnEsc();
        registerGuiElements();

        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String playerUUID = holder.getPlayer().getUniqueId().toString();

        ResultSet resultSet = mySQL.query("SELECT * FROM lc_saves as s, lc_play_through as p " +
                "WHERE s.play_through_id = p.id and p.user_id = '" + playerUUID + "' AND p.completion = false;");
        // ResultSet resultSet = mySQL.query("SELECT * FROM lc_maps WHERE type = " + mapTypePosition + " LIMIT " + (pageVolume * (page - 1)) + ", 40;");

        GuiElementGroup group = new GuiElementGroup('g');
        this.current.addElement(new StaticGuiElement('a', new ItemStack(Material.STAINED_GLASS_PANE, 1), click -> true, " "));

        try {
            if (resultSet != null) {
                while (resultSet.next()) {
                    group.addElement(mapElements(resultSet));
                }
                this.current.addElement(group);
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent);
    }

    private StaticGuiElement mapElements(ResultSet resultSet) throws SQLException {
        int saveStateID = resultSet.getInt("id");

        StaticGuiElement staticGuiElement;
        staticGuiElement = new StaticGuiElement('g', new ItemStack(Material.STONE), click -> {
            holder.sendMessage("Clicked save state.");
            return true;
        },
                "§r(" + saveStateID + ")",
                "\n§l§6-----SaveState Information-----\n"
        );
        return staticGuiElement;
    }

    @Override
    protected boolean getPage() {
        return false;
    }
}
