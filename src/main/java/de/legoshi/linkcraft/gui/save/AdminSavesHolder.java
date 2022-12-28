package de.legoshi.linkcraft.gui.save;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.manager.MapManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.PlayThrough;
import de.legoshi.linkcraft.player.SaveState;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class AdminSavesHolder extends SavesHolder {

    @Inject private SaveStateManager saveStateManager;
    @Inject private MapManager mapManager;
    @Inject private PlayerManager playerManager;

    public void openGui(Player player, InventoryGui parent, String playerName) {
        this.title = "Admin - " + playerName + "'s saves";
        this.playerName = playerName;
        super.openGui(player, parent);
    }

    @Override
    protected StaticGuiElement mapElements(ResultSet resultSet) throws SQLException {
        int saveStateID = resultSet.getInt("s.id");
        SaveState saveState = saveStateManager.requestObjectById(saveStateID, holder);
        PlayThrough playThrough = saveState.getPlayThrough();
        StandardMap map = mapManager.requestObjectById(playThrough.getMap().getId());

        StaticGuiElement staticGuiElement;
        staticGuiElement = new StaticGuiElement('g', new ItemStack(Material.valueOf(saveState.getBlockTypeName())), click -> {
            if (click.getType().isRightClick() && click.getType().isShiftClick()) {
                String uuid = playerManager.uuidByName(this.playerName);
                Player delSavePlayer = playerManager.getPlayerFromUUID(uuid);
                boolean success = saveStateManager.deleteObject(delSavePlayer, saveStateID);
                if (success) holder.sendMessage("Deleted save state");
                else holder.sendMessage("Something went wrong while deleting the save state...");
                holder.closeInventory();
            }
            if (click.getType().isLeftClick()) {
                holder.teleport(saveState.getSaveLocation());
                holder.sendMessage("Selected save state");
            }
            return true;
        },
                "§r" + saveState.getSaveStateName() + " (" + saveStateID + ")\n",
                "§r§l§6» §eMap:           §7" + map.getMapName() + "\n" +
                        "§r§l§6» §eTotal jumps: §7" + playThrough.getCurrentJumps() + "\n" +
                        "§r§l§6» §eJoin date:    §7" + playThrough.getJoinDate() + "\n" +
                        "§r§l§6» §eTotal /prac: §7" + playThrough.getPracUsages() + "\n" +
                        "§r§l§6» §ePlaytime:      §7" + TimeUnit.MILLISECONDS.toHours(playThrough.getTimePlayedNormal()) + "h\n" +
                        "§r§l§6» §ePractime:     §7" + TimeUnit.MILLISECONDS.toHours(playThrough.getTimePlayedPrac()) + "h\n\n" +
                        "§8Teleport - (Left Click)\n" +
                        "§Delete - (Shift + Right Click)\n"
        );
        return staticGuiElement;
    }

}
