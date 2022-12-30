package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.TagOwnedData;
import de.legoshi.linkcraft.util.Dye;
import de.legoshi.linkcraft.util.ItemUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import de.themoep.inventorygui.GuiElement;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.UUID;

public class TagLeaderboard extends GUIScrollable {

    private int tag;
    private String name;
    private String description;
    private boolean canEdit;
    private String cName;
    @Inject private TagManager tagManager;
    @Inject private PlayerManager playerManager;

    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggt",
            "ggggggggq",
            "ggggggggf",
            "ggggggggd",
    };

    public void openGui(Player player, InventoryGui parent, int tag, String tagName, String tagDescription, String cName) {
        super.openGui(player, parent);
        this.tag = tag;
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, "Tag Owners", guiSetup);
        this.name = tagName;
        this.description = tagDescription;

        this.canEdit = playerManager.getPlayer(player).canRemoveTags();
        this.cName = cName;
        fullCloseOnEsc();
        registerGuiElements();
        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        getPage();
        StaticGuiElement tag = new StaticGuiElement('t', new ItemStack(Material.NAME_TAG, 1), click -> true, name, description);
        StaticGuiElement filler = new StaticGuiElement('f', new ItemStack(Material.STAINED_GLASS_PANE, 1, Dye.GRAY.data), click -> true, " ");
        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent, tag, filler);
    }

    @Override
    protected boolean getPage() {
        ArrayList<TagOwnedData> players = tagManager.getOwnedBy(tag, page, pageVolume);
        if(players.isEmpty()) {
            if(page == 0) {
                noDataItem('g', "No players found");
            }
            return false;
        }

        String opText = canEdit ? "\n" + ChatColor.RED + "Shift right click to remove tag from player" : "";

        this.current.removeElement('g');
        GuiElementGroup group = new GuiElementGroup('g');

        for(TagOwnedData player : players) {
            String uuid = player.getUUID();
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            String name = offlinePlayer.getName();
            if(name != null) {
                group.addElement(new StaticGuiElement('g', ItemUtils.playerHead(name), click -> deleteTag(click, uuid, name), ChatColor.GREEN + name,
                        ChatColor.GRAY + player.getDate() + opText));
            } else {
                group.addElement(new StaticGuiElement('g', new ItemStack(Material.BARRIER, 1), click -> deleteTag(click, uuid, uuid), ChatColor.RED + player.getUUID(),
                        ChatColor.GRAY + "Player data could not be retrieved\n" +
                        ChatColor.GRAY + player.getDate() +
                        opText));
            }

            this.current.addElement(group);
        }

        return true;
    }

    private boolean deleteTag(GuiElement.Click click, String uuid, String name) {
        if(!canEdit) {
            return true;
        }

        if(click.getType().isShiftClick() && click.getType().isRightClick()) {
                if(tagManager.removeTag(tag, uuid)) {
                    holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_REMOVE_TAG, true, name, cName));
                }
                getPage();
                click.getGui().setPageNumber(click.getGui().getPageNumber(holder));
                click.getGui().playClickSound();
        }
        return true;
    }
}
