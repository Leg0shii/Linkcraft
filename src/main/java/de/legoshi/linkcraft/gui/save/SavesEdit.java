package de.legoshi.linkcraft.gui.save;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.gui.GUIPane;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.player.SaveState;
import de.legoshi.linkcraft.util.MaterialUtils;
import de.themoep.inventorygui.*;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class SavesEdit extends GUIPane {

    @Inject private SaveStateManager saveStateManager;
    @Inject private SavesHolder savesHolder;

    protected String title;
    private int saveID;

    private String blockName;
    private String saveName;

    private final String[] guiSetup = {
            "ggggggggg",
            "ggagbgcgg",
            "ggggggggq",
    };

    public void openGui(Player player, InventoryGui parent, int saveID) {
        super.openGui(player, parent);
        this.title = "§lEdit Save";
        this.current = new InventoryGui((JavaPlugin) Linkcraft.getPlugin(), player, title, guiSetup);
        this.saveID = saveID;

        SaveState saveState = saveStateManager.requestObjectById(saveID);
        this.blockName = saveState.getBlockTypeName();
        this.saveName = saveState.getSaveStateName();

        fullCloseOnEsc();
        registerGuiElements();

        this.current.show(this.holder);
    }

    @Override
    protected void registerGuiElements() {
        this.current.setFiller(new ItemStack(Material.STAINED_GLASS_PANE));
        AtomicReference<ItemStack> displayItem = new AtomicReference<>(new ItemStack(Material.getMaterial(this.blockName)));
        DynamicGuiElement dynamicGuiElement = new DynamicGuiElement('a', (viewer) -> new StaticGuiElement('a',
                displayItem.get(),
                click -> {
                    displayItem.set(MaterialUtils.getRandomVisibleBlock());
                    this.blockName = displayItem.get().getType().toString();
                    click.getGui().draw();
                    return true;
                },
                "§r§eDisplay Block",
                "§r§7Click to change block."
        ));

        this.current.addElement(new StaticGuiElement('b', new ItemStack(Material.NAME_TAG, 1), click -> {
            new AnvilGUI.Builder()
                    .onComplete((completion) -> {
                        if (completion.getText().isEmpty()) {
                            return Collections.singletonList(AnvilGUI.ResponseAction.replaceInputText("Try again"));
                        }
                        saveStateManager.updateName(saveID, completion.getText());
                        holder.sendMessage("Successfully saved new name");
                        return Collections.singletonList(AnvilGUI.ResponseAction.run(() -> savesHolder.openGui(holder, null)));
                    })
                    .text(saveName)
                    .itemLeft(new ItemStack(Material.NAME_TAG))
                    .plugin(Linkcraft.getPlugin())
                    .open(holder);
            return true;
        }, "§r§eChange Name",
                "§r§7Click to change name."));


        this.current.addElement(new StaticGuiElement('c', new ItemStack(Material.BARRIER, 1), click -> {
            saveStateManager.deleteObject(saveID);
            this.current.close();
            savesHolder.openGui(holder, null);
            return true;
        }, "§r§c§lDelete",
                "§r§7Click to delete save!\n§r§cThis is not reversible"));

        this.current.setCloseAction((action) -> {
            saveGUI();
            return false;
        });

        this.returnToParent.setAction(action -> {
            saveGUI();
            this.current.close();
            savesHolder.openGui(holder, null);
            return true;
        });

        this.current.addElements(dynamicGuiElement, this.returnToParent);
    }

    private void saveGUI() {
        saveStateManager.updateBlock(saveID, blockName);
        saveStateManager.updateName(saveID, saveName);
    }

}
