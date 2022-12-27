package de.legoshi.linkcraft.gui.save;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.PlayThrough;
import de.legoshi.linkcraft.player.SaveState;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class SavesHolder extends GUIScrollable {

    @Inject private DBManager dbManager;
    @Inject private PlayerManager playerManager;
    @Inject private SaveStateManager saveStateManager;
    @Inject private MapManager mapManager;

    protected String title;
    protected String playerName;
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
        this.playerName = this.playerName == null ? player.getName() : this.playerName;
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);

        fullCloseOnEsc();
        registerGuiElements();

        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String playerUUID = playerManager.uuidByName(playerName);
        int currentPage = pageVolume * (page - 1);
        String sql = "SELECT s.id, s.loaded FROM lc_saves as s, lc_play_through as p " +
                "WHERE s.play_through_id = p.id and p.user_id = ? AND p.completion = false;";
                // "LIMIT ?, 40;";

        GuiElementGroup group = new GuiElementGroup('g');
        this.current.addElement(new StaticGuiElement('a', new ItemStack(Material.STAINED_GLASS_PANE, 1), click -> true, " "));

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, playerUUID);
            // stmt.setInt(2, currentPage);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    group.addElement(mapElements(resultSet));
                }
                this.current.addElement(group);
            } else {
                holder.sendMessage("Couldn't load any saves...");
            }
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ResultSet resultSet = mySQL.query("SELECT * FROM lc_maps WHERE type = " + mapTypePosition + " LIMIT " + (pageVolume * (page - 1)) + ", 40;");
        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent);
    }

    protected StaticGuiElement mapElements(ResultSet resultSet) throws SQLException {
        int saveStateID = resultSet.getInt("s.id");
        SaveState saveState = saveStateManager.requestObjectById(saveStateID, holder);
        PlayThrough playThrough = saveState.getPlayThrough();
        StandardMap map = mapManager.requestObjectById(playThrough.getMap().getId());

        boolean loaded = resultSet.getBoolean("s.loaded");
        ItemStack itemStack = new ItemStack(Material.STONE);
        if (loaded) itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        StaticGuiElement staticGuiElement;
        staticGuiElement = new StaticGuiElement('g', itemStack, click -> {
            if (loaded) {
                holder.sendMessage("You are already in this save state.");
                return true;
            }
            playerManager.playerJoinSaveState(holder, saveState);
            holder.teleport(saveState.getSaveLocation());
            holder.sendMessage("Joined save state.");
            return true;
        },
                "§r" + saveState.getSaveStateName() + " (" + saveStateID + ")\n",
                        "§r§l§6» §eMap:           §7" + map.getMapName() + "\n" +
                        "§r§l§6» §eTotal jumps: §7" + playThrough.getCurrentJumps() + "\n" +
                        "§r§l§6» §eJoin date:    §7" + playThrough.getJoinDate() + "\n" +
                        "§r§l§6» §eTotal /prac: §7" + playThrough.getPracUsages() + "\n" +
                        "§r§l§6» §ePlaytime:      §7" + TimeUnit.MILLISECONDS.toHours(playThrough.getTimePlayedNormal()) + "h\n" +
                        "§r§l§6» §ePractime:     §7" + TimeUnit.MILLISECONDS.toHours(playThrough.getTimePlayedPrac()) + "h\n\n" +
                        "§8Join - (Right Click)\n" +
                        "§8Edit - (Left Click)\n"
        );
        return staticGuiElement;
    }

    @Override
    protected boolean getPage() {
        return false;
    }
}
