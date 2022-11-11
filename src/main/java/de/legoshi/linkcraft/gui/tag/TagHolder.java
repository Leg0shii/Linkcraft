package de.legoshi.linkcraft.gui.tag;

import com.sun.tools.javac.Main;
import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.tag.TagRarity;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.swing.text.html.HTML;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TagHolder extends GUIScrollable {

    @Inject private PlayerManager playerManager;
    private TagRarity tagRarity;

    private String title;
    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggf",
            "ggggggggq",
            "ggggggggf",
            "ggggggggd",
    };

    public void openGui(Player player, InventoryGui parent, TagRarity rarity, int page) {
        super.openGui(player, parent);
        this.page = page;
        this.tagRarity = rarity;
        this.title = rarity.name() + " tags";
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        AsyncMySQL mySQL = dbManager.getMySQL();
        int tagRarityPos = TagRarity.getTagPosition(tagRarity);
        ResultSet resultSet = mySQL.query("SELECT tag_id, name, rarity, description " +
                "FROM lc_tags WHERE rarity = " + tagRarityPos + " LIMIT " + (pageVolume * (page - 1)) + ", 40;");

        GuiElementGroup group = new GuiElementGroup('g');
        this.current.addElement(new StaticGuiElement('h', new ItemStack(Material.STAINED_GLASS_PANE, 1), click -> true, " "));

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
        int tagId = resultSet.getInt("tag_id");
        String name = resultSet.getString("name");
        String desc = resultSet.getString("description");
        int rarity = resultSet.getInt("rarity");
        TagRarity tagRarity = TagRarity.values()[rarity];

        StaticGuiElement staticGuiElement;
        staticGuiElement = new StaticGuiElement('g', new ItemStack(Material.NAME_TAG), click -> {
            AbstractPlayer abstractPlayer = playerManager.getPlayer((Player) click.getWhoClicked());
            abstractPlayer.setPlayerTag(new PlayerTag("" + tagId, name, desc, TagRarity.values()[rarity]));
            return true; // returning true will cancel the click event and stop taking the item
        },
                "§r" + name + ChatColor.WHITE + " (" + tagId + ")",
                "\n§l§6-----Tag Information-----\n" +
                "§r§l§6» §eType:        §7" + "TODO" + "\n" +
                "§r§l§6» §eRarity:      §7" + TagRarity.toColor(tagRarity) + tagRarity + "\n" +
                "§r§l§6» §eDescription: §7" + desc + "\n\n" +
                "§l§6-----Your Stats-----\n" +
                "§r§l§6» §eCollected: §7" + "TODO (DATE)\n" +
                "§r§l§6» §eTotal:      §7" + "TODO (0/0) 0%\n" +
                "§8 right click - show leaderboard\n"
                );
        return staticGuiElement;
    }

}
