package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.util.CommonsUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "remove", desc = "%translatable:tags.remove.desc%")
public class TagRemoveCommand implements CommandClass {
    @Inject private TagManager tagManager;
    @Inject private PlayerManager playerManager;

    @Command(names = "")
    public boolean removeTag(CommandSender sender, @Named("player") String player, @Named("tag") String tagId) {
        if (!playerManager.playerExists(player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NEVER_JOINED, true, player));
            return true;
        }

        if (tagId.equalsIgnoreCase("all")) {
            int amountRemoved = tagManager.removeAllTags(player);
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_REMOVE_TAGS, true, amountRemoved, player));
            return true;
        }

        if (!CommonsUtils.isNumeric(tagId)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.INVALID_ARG, true, tagId));
            return true;
        }

        int id = (int) Float.parseFloat(tagId);
        if (!tagManager.tagExists(id)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NO_TAG, true, id));
        } else if (!tagManager.hasTag(player, id)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_HASNT_UNLOCKED, true, player, id));
        } else {
            PlayerTag tag = tagManager.requestObjectById(id);
            boolean success = tagManager.removeTag(player, id);
            if(success) sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_REMOVE_TAG, true, player, tag.getDisplayName()));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_REMOVE_TAG_ERROR, true, player, tag.getDisplayName()));
        }
        return true;
    }
}
