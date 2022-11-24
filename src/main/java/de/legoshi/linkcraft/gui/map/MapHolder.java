package de.legoshi.linkcraft.gui.map;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.manager.LocationManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MapHolder extends GUIScrollable {

    @Inject private DBManager dbManager;
    @Inject private PlayerManager playerManager;
    @Inject private LocationManager locationManager;

    private MapType mapType;
    private String title;
    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggf",
            "ggggggggq",
            "ggggggggf",
            "ggggggggd",
    };

    public void openGui(Player player, InventoryGui parent, MapType type, int page) {
        super.openGui(player, parent);
        this.page = page;
        this.mapType = type;
        this.title = type.name() + " maps";
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        fullCloseOnEsc();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        AsyncMySQL mySQL = dbManager.getMySQL();
        int mapTypePosition = MapType.getMapPosition(mapType);
        ResultSet resultSet = mySQL.query("SELECT * FROM lc_maps WHERE type = " + mapTypePosition + ";");
        // ResultSet resultSet = mySQL.query("SELECT * FROM lc_maps WHERE type = " + mapTypePosition + " LIMIT " + (pageVolume * (page - 1)) + ", 40;");

        GuiElementGroup group = new GuiElementGroup('g');
        this.current.addElement(new StaticGuiElement('f', new ItemStack(Material.STAINED_GLASS_PANE, 1), click -> true, " "));

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
        int mapId = resultSet.getInt("id");
        String name = resultSet.getString("name");
        MapType type = MapType.values()[resultSet.getInt("type")];
        MapLength length = MapLength.values()[resultSet.getInt("length")];
        double difficulty = resultSet.getDouble("difficulty");
        String builderNames = resultSet.getString("builder_names");
        String releaseDate = resultSet.getString("release_date");

        Location mapSpawn = locationManager.requestObjectById(""+resultSet.getInt("spawn_location_id"));

        StaticGuiElement staticGuiElement;
        staticGuiElement = new StaticGuiElement('g', new ItemStack(Material.NAME_TAG), click -> {
            AbstractPlayer abstractPlayer = playerManager.getPlayer((Player) click.getWhoClicked());
            abstractPlayer.getPlayer().sendMessage("Selected map " + name);
            abstractPlayer.getPlayer().teleport(mapSpawn);
            return true; // returning true will cancel the click event and stop taking the item
        },
                "§r" + name + ChatColor.WHITE + " (" + mapId + ")",
                "\n§l§6-----Map Information-----\n" +
                        "§r§l§6» §eType:                §7" + type.name() + "\n" +
                        "§r§l§6» §eDifficulty:           §7" + difficulty + "\n" +
                        "§r§l§6» §eLength:              §7" + length + "\n" +
                        "§r§l§6» §eBuilders:            §7" + builderNames + "\n" +
                        "§r§l§6» §eRelease date:      §7" + releaseDate + "\n" +
                        "§r§l§6» §eTotal completions: §7" + "TODO (0/0) 0%\n\n" +
                        "§l§6-----Your Stats-----\n" +
                        "§r§l§6» §cCompletion date: §7" + "TODO (DATE)\n" +
                        "§r§l§6» §cCompletion time: §7" + "TODO TIME SPENT\n"
        );
        return staticGuiElement;
    }

    @Override
    protected boolean getPage() {
        return false;
    }
}
