package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(names = "unset", desc = "%translatable:tags.unset.desc%")
public class TagUnsetCommand implements CommandClass {
    @Inject private TagManager tagManager;

    @Command(names = "")
    public boolean unsetTag(CommandSender sender, @OptArg("") String player) {
        if(player != null && !player.isBlank()) {
            if(tagManager.playerExists(player)) {
                tagManager.setDefaultTag(player);
                sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_TAG_OTHER, true, player));
            } else {
                sender.sendMessage(MessageUtils.composeMessage(Messages.NEVER_JOINED, true, player));
            }
        } else if(sender instanceof Player p) {
            tagManager.setDefaultTag(p.getDisplayName());
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_TAG, true));
        }
        return true;
    }
}
