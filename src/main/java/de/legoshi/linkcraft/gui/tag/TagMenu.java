package de.legoshi.linkcraft.gui.tag;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.TagType;
import de.legoshi.linkcraft.util.CustomHeads;
import de.legoshi.linkcraft.util.Dye;
import de.legoshi.linkcraft.util.ItemUtils;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.HashMap;

public class TagMenu extends GUIPane {

    @Inject private Injector injector;
    @Inject private TagManager tagManager;
    private TagHolder tagHolder;
    private HashMap<TagType, Integer> tagCount;
    private HashMap<TagType, Integer> playerTagCount;
    private int tagCountTotal;
    private int playerTagCountTotal;

    private final String title = "Tag Menu";
    private final String[] guiSetup = {
            "         ",
            "    e    ",
            "         ",
            " a b c d ",
            "         ",
            "         ",
    };

    @Override
    public void openGui(Player player, InventoryGui parent) {
        super.openGui(player, parent);
        this.tagCount = tagManager.tagCounts();
        this.playerTagCount = tagManager.tagCountPlayer(player);
        this.tagCountTotal = tagCount.values().stream().mapToInt(Integer::intValue).sum();
        this.playerTagCountTotal = playerTagCount.values().stream().mapToInt(Integer::intValue).sum();
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        registerGuiElements();
        this.current.setFiller(new ItemStack(Material.STAINED_GLASS_PANE, 1, Dye.GRAY.data));
        fullCloseOnEsc();
        this.current.show(this.holder);
        this.tagHolder = injector.getInstance(TagHolder.class);
    }

    @Override
    protected void registerGuiElements() {
        StaticGuiElement hiddenElement = new StaticGuiElement('a', CustomHeads.hiddenHead, click -> {
            this.tagHolder.openGui(this.holder, this.current, TagType.HIDDEN, playerTagCount.get(TagType.HIDDEN));
            return true;
        }, "&d&lHidden Tags" + getMenuTitle(TagType.HIDDEN));

        StaticGuiElement victorElement = new StaticGuiElement('b', CustomHeads.victorHead, click -> {
            tagHolder.openGui(this.holder, this.current, TagType.VICTOR, playerTagCount.get(TagType.VICTOR));
            return true;
        }, "&6&lVictor Tags" + getMenuTitle(TagType.VICTOR));

        StaticGuiElement eventElement = new StaticGuiElement('c', getSeasonalHead(), click -> {
            tagHolder.openGui(this.holder, this.current, TagType.EVENT, playerTagCount.get(TagType.EVENT));
            return true;
        }, "&c&lEvent Tags" + getMenuTitle(TagType.EVENT));

        StaticGuiElement specialElement = new StaticGuiElement('d', CustomHeads.specialHead, click -> {
            tagHolder.openGui(this.holder, this.current, TagType.SPECIAL, playerTagCount.get(TagType.SPECIAL));
            return true;
        }, "&3&lSpecial Tags" + getMenuTitle(TagType.SPECIAL));

        ItemStack allTagsItem = new ItemStack(Material.NAME_TAG);

        if(tagCountTotal == playerTagCountTotal) {
            ItemUtils.addGlow(allTagsItem);
        }

        StaticGuiElement allElement = new StaticGuiElement('e', allTagsItem, click -> {
            tagHolder.openGui(this.holder, this.current, TagType.NONE, playerTagCountTotal);
            return true;
        }, "&7&lAll Tags" + " (" + playerTagCountTotal + "/" + tagCountTotal + ")");

        current.addElements(hiddenElement, victorElement, eventElement, specialElement, allElement);
    }

    private String getMenuTitle(TagType type) {
        return " (" + playerTagCount.get(type) + "/" + tagCount.get(type) + ")";
    }

    private ItemStack getSeasonalHead() {
        ItemStack result;
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        if(month == Calendar.MARCH || month == Calendar.APRIL || month == Calendar.MAY) {
            result = CustomHeads.eventSpringHead;
        } else if (month == Calendar.JUNE || month == Calendar.JULY || month == Calendar.AUGUST) {
            result = CustomHeads.eventSummerHead;
        } else if (month == Calendar.SEPTEMBER || month == Calendar.OCTOBER || month == Calendar.NOVEMBER) {
            result = CustomHeads.eventFallHead;
        } else {
            result = CustomHeads.eventWinterHead;
        }

        return result;
    }

}
