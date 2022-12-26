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
import de.themoep.inventorygui.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;
import java.util.ArrayList;

public class TagHolder extends GUIScrollable {

    @Inject private Injector injector;
    @Inject private PlayerManager playerManager;
    @Inject private TagManager tagManager;

    public enum FilterState { ALL, COLLECTED, UNCOLLECTED }

    private TagLeaderboard tagLeaderboard;
    private FilterState filter;
    private TagType tagType;
    private int tagTypePos;
    private final String[] guiSetup = {
            "ggggggggu",
            "ggggggggr",
            "ggggggggq",
            "ggggggggs",
            "ggggggggd",
    };
    private final String[] filterNames = {
            "All",
            "Collected",
            "Uncollected",
    };

    public void openGui(Player player, InventoryGui parent, TagType type, int count) {
        super.openGui(player, parent);
        this.tagType = type;
        this.tagTypePos = TagType.getTagPosition(type);
        this.current = new InventoryGui((JavaPlugin)Linkcraft.getPlugin(), player, formattedName() + " Tags " + "(" + count + ")", guiSetup);
        this.filter = FilterState.ALL;


        fullCloseOnEsc();
        registerGuiElements();

        this.current.show(this.holder);
        this.tagLeaderboard = injector.getInstance(TagLeaderboard.class);
    }

    @Override
    protected boolean getPage() {
        ArrayList<TagData> tagData = tagManager.getTags(holder, tagTypePos, filter, page, pageVolume);

        if(tagData.isEmpty()) {
            if(page == 0) {
                noDataItem('g', "No tags found");
            }
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

        GuiStateElement sortTags = new GuiStateElement('s',
                new GuiStateElement.State(click -> changeFilter(click, FilterState.ALL), "all", new ItemStack(Material.REDSTONE_COMPARATOR), getDescription("Filter by", 0)),
                new GuiStateElement.State(click -> changeFilter(click, FilterState.COLLECTED), "collected", new ItemStack(Material.REDSTONE_COMPARATOR), getDescription("Filter by", 1)),
                new GuiStateElement.State(click -> changeFilter(click, FilterState.UNCOLLECTED), "uncollected", new ItemStack(Material.REDSTONE_COMPARATOR), getDescription("Filter by", 2))
        );

        this.current.addElements(this.pageUp, this.pageDown, this.returnToParent, resetTag, sortTags);
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
        String cDesc = desc;
        String plural = tagData.getOwnedBy() == 1 ? "player" : "players";
        String example = playerManager.getPlayer(holder).sampleChat(name, "msg");

        ItemStack tagItem = new ItemStack(Material.NAME_TAG, 1);

        // If the tag is collected, add enchantment glow
        if(tagData.getDate() != null) {
            ItemUtils.addGlow(tagItem);
        } else {
            cDesc = ChatColor.MAGIC + cDesc;
        }
        cDesc = ChatColor.GRAY + cDesc;


        String tagName = "§r" + cName + ChatColor.WHITE + " (" + tagId + ")";
        String tagDescriptionLeaderboard = "§l§6-----Tag Information-----\n" +
                                "§r§l§6» §eType: §7" + TagType.toColor(tagType) + tagType + "\n" +
                                "§r§l§6» §eRarity: §7" + TagRarity.toColor(tagRarity) + tagRarity + "\n" +
                                "§r§l§6» §eDescription: " + CommonsUtils.wrap(cDesc, 17, 30) + "\n\n" +
                                "§l§6-----Tag Stats-----\n" +
                                "§r§l§6» §eCollected: §7" + date + "\n" +
                                "§r§l§6» §eOwned by: §7" + difficulty(tagData.getOwnedBy()) +  tagData.getOwnedBy() + " " + plural + "\n\n" +
                                example + "\n\n";
        String tagDescription = tagDescriptionLeaderboard + "§6» Right click to display tag owners\n";

        // TODO: Add some way to let the player know their current tag?
        return new StaticGuiElement('g', tagItem, click -> {
            if(click.getType().isRightClick()) {
                this.tagLeaderboard.openGui(holder, current, tagId, tagName, tagDescriptionLeaderboard, cName);
            } else {
                if (tagManager.hasTag(holder, tagId)) {
                    AbstractPlayer abstractPlayer = playerManager.getPlayer(holder);
                    tagManager.setTag(holder, tagId);
                    abstractPlayer.setPlayerTag(new PlayerTag(tagId, name, desc, TagRarity.values()[rarity], TagType.values()[type]));
                    holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_SELECT, true, name));
                    current.close();
                } else {
                    holder.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NOT_UNLOCKED, true, name));
                }
            }
            return true; // returning true will cancel the click event and stop taking the item
        },
                // TODO: Carry description colour over to next line?
                tagName, tagDescription);

    }


    // This is probably terrible and also should be moved somewhere else later
    private String[] getDescription(String name, int position) {
        String[] description = new String[filterNames.length + 1];
        description[0] = name;
        for(int i = 0; i < filterNames.length; i++) {
            if(i == position) {
                description[i + 1] = ChatColor.GREEN + "➤ " + filterNames[i];
            } else {
                description[i + 1] = ChatColor.RED + "  " + filterNames[i];
            }
        }

        return description;
    }

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

    private String formattedName() {
        if(tagTypePos == 0) {
            return "All";
        }
        return CommonsUtils.capatalize(tagType.name().toLowerCase());
    }

    private void changeFilter(GuiElement.Click click, FilterState filter) {
        this.filter = filter;
        page = 0;
        this.current.removeElement('g');
        getPage();
        pageUp.setNumber(1);
        pageDown.setNumber(2);
        click.getGui().setPageNumber(0);
        click.getGui().playClickSound();
    }


}
