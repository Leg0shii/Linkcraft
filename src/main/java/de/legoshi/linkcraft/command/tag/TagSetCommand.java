package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

// Is this for admins or players (I assumed admins, so I made it override whether a player has the tag unlocked
@Command(names = "set", desc = "%translatable:tags.set.desc%")
public class TagSetCommand implements CommandClass {
    @Inject private TagManager tagManager;
    @Inject private PlayerManager playerManager;

    @Command(names = "")
    public boolean setTag(CommandSender sender, String player, int tagId) {
        if (!playerManager.playerExists(player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NEVER_JOINED, true, player));
        } else if (!tagManager.tagExists(tagId)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NO_TAG, true, tagId));
        } else {
            // Not sure who this should message...
            PlayerTag tag = tagManager.requestObjectById(tagId);
            boolean success = tagManager.setTag(player, tagId);
            if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_SELECT_OTHER, true, player, tag.getDisplayName()));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_SELECT_OTHER_ERROR, true, player, tag.getDisplayName()));
        }
        return true;
    }
}