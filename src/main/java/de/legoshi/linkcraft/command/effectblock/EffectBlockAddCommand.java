package de.legoshi.linkcraft.command.effectblock;

import de.legoshi.linkcraft.effectblock.Effect;
import de.legoshi.linkcraft.effectblock.ExecutorType;
import de.legoshi.linkcraft.manager.EffectBlockManager;
import de.legoshi.linkcraft.util.CommonsUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "add", desc = "%translatable:effectblockadd.desc%")
public class EffectBlockAddCommand implements CommandClass {
    @Inject private EffectBlockManager effectBlockManager;

    @Command(names = "")
    public boolean addEffectBlock(CommandSender sender, @Named("command") String command, ArgumentStack args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            String combined = command + " " + CommonsUtils.joinArguments(args);
            Effect effect = new Effect(combined, ExecutorType.CONSOLE);

//            if(executor != null && !executor.isBlank() && CommonsUtils.isNumeric(executor)) {
//                effect = new Effect(command, ExecutorType.values()[(int)Float.parseFloat(executor)]);
//            } else {
//                effect = new Effect(command, ExecutorType.values()[0]);
//            }

            effectBlockManager.storeEffect(player, effect);
            sender.sendMessage(MessageUtils.composeMessage(Messages.EFFECT_BLOCK_ADD, true, combined));
        }
        return true;
    }
}
