package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIScrollable;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.tag.TagData;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.tag.TagType;
import de.legoshi.linkcraft.util.CommonsUtils;
import de.legoshi.linkcraft.util.CustomHeads;
import de.legoshi.linkcraft.util.ItemUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import javax.inject.Inject;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class TagHolder extends GUIScrollable {

    @Inject private PlayerManager playerManager;
    @Inject private TagManager tagManager;

    private TagRarity tagRarity;
    private int tagRarityPos;
    private int playerCount;
    private DecimalFormat df;
    private String title;
    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggq",
            "ggggggggr",
            "ggggggggq",
            "ggggggggd",
    };

    public void openGui(Player player, InventoryGui parent, TagRarity rarity) {
        super.openGui(player, parent);
        this.tagRarity = rarity;
        this.tagRarityPos = TagRarity.getTagPosition(tagRarity);
        // TODO: add total player tag count to title?
        this.title = CommonsUtils.capatalize(rarity.name().toLowerCase()) + " tags";
        this.current = new InventoryGui((JavaPlugin)Linkcraft.getPlugin(), player, title, guiSetup);
        this.playerCount = playerManager.playerCount();
        this.df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        fullCloseOnEsc();
        registerGuiElements();
        this.current.show(this.holder);
    }

    @Override
    protected boolean getPage() {
        ArrayList<TagData> tagData = tagManager.getTags(tagRarityPos, holder, page, pageVolume);
        if(tagData.isEmpty()) {
            return false;
        }
        this.current.removeElement('g');

        GuiElementGroup group = new GuiElementGroup('g');
        for (TagData tag : tagData) {
            group.addElement(addTag(tag));
            this.current.addElement(group);
        }
        return true;
    }

    @Override
    protected void registerGuiElements() {
        getPage();

        StaticGuiElement resetTag = new StaticGuiElement('r', CustomHeads.resetHead, click -> {
            if(tagManager.hasTag(holder)) {
                holder.performCommand("tags unset");
                current.close();
            } else {
                holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_NONE, true));
            }

            return true;
        }, "§cRemove tag");

        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent, resetTag);
    }

    private StaticGuiElement addTag(TagData tagData) {
        int tagId = tagData.getTagId();
        String name = tagData.getName();
        String desc = tagData.getDescription();
        int rarity = tagData.getRarity();
        int type = tagData.getType();
        String date = tagData.getDate() != null ? tagData.getDate() : "Not collected";
        TagRarity tagRarity = TagRarity.values()[rarity];
        TagType tagType = TagType.values()[type];
        String cName = MessageUtils.translateChatColor(name);
        String cDesc = ChatColor.GRAY + desc;
        String plural = tagData.getOwnedBy() == 1 ? "player" : "players";
        String example = playerManager.getPlayer(holder).sampleChat(name, "msg");
        //String percentOwned = df.format((tagData.getOwnedBy() / (float)playerCount) * 100L) + "%";

        ItemStack tagItem = new ItemStack(Material.NAME_TAG, 1);

        // If the tag is collected, add enchantment glow
        if(tagData.getDate() != null) {
            ItemUtils.addGlow(tagItem);
        }
        // TODO: Add some way to let the player know their current tag?

        return new StaticGuiElement('g', tagItem, click -> {
            if(tagManager.hasTag(holder, tagId)) {
                AbstractPlayer abstractPlayer = playerManager.getPlayer(holder);
                tagManager.setTag(holder, tagId);
                abstractPlayer.setPlayerTag(new PlayerTag(tagId, name, desc, TagRarity.values()[rarity], TagType.values()[type]));
                holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_SELECT, true, name));
                current.close();
            } else {
                holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NOT_UNLOCKED, true, name));
            }
            return true; // returning true will cancel the click event and stop taking the item
        },
                // TODO: Carry description colour over to next line?
                "§r" + cName + ChatColor.WHITE + " (" + tagId + ")",
                "§l§6-----Tag Information-----\n" +
                "§r§l§6» §eType: §7" + TagType.toColor(tagType) + tagType + "\n" +
                "§r§l§6» §eRarity: §7" + TagRarity.toColor(tagRarity) + tagRarity + "\n" +
                "§r§l§6» §eDescription: §7" + CommonsUtils.wrap(cDesc, 17, 30) + "\n\n" +
                "§l§6-----Tag Stats-----\n" +
                "§r§l§6» §eCollected: §7" + date + "\n" +
                "§r§l§6» §eOwned by: §7" + difficulty(tagData.getOwnedBy()) +  tagData.getOwnedBy() + " " + plural + "\n\n" + //+ "/" + playerCount + ") " + percentOwned + "\n" +
                example + "\n"
                );
    }

    // im tired... ik this is horrible
    // TODO: get from config?
    private ChatColor difficulty(int beaten) {
        ChatColor c;
        if(beaten <= 5) {
            c = ChatColor.DARK_PURPLE;
        } else if(beaten <= 15) {
            c = ChatColor.DARK_RED;
        } else if(beaten <= 30) {
            c = ChatColor.RED;
        } else if(beaten <= 75) {
            c = ChatColor.GOLD;
        } else if(beaten <= 150) {
            c = ChatColor.YELLOW;
        } else if(beaten <= 350) {
            c = ChatColor.GREEN;
        } else {
            c = ChatColor.DARK_GREEN;
        }
        return c;
    }


}
