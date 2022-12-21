package de.legoshi.linkcraft.command.effectblock;

import de.legoshi.linkcraft.manager.EffectBlockManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(names = "remove", desc = "%translatable:effectblockremove.desc%")
public class EffectBlockRemoveCommand implements CommandClass {
    @Inject
    private EffectBlockManager effectBlockManager;

    @Command(names = "")
    public boolean removeEffectBlock(CommandSender sender) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            effectBlockManager.storeRemoving(player);
            sender.sendMessage(MessageUtils.composeMessage(Messages.EFFECT_BLOCK_REMOVE, true));
        }
        return true;
    }
}