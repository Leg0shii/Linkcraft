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
import javax.inject.Named;

@Command(names = "give", desc = "%translatable:tags.give.desc%")
public class TagGiveCommand implements CommandClass {
    @Inject
    private TagManager tagManager;
    @Inject
    private PlayerManager playerManager;

    @Command(names = "")
    public boolean giveTag(CommandSender sender, @Named("player") String player, @Named("tag") int tagId) {
        if (!playerManager.playerExists(player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NEVER_JOINED, true, player));
        } else if (!tagManager.tagExists(tagId)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NO_TAG, true, tagId));
        } else if (tagManager.hasTag(player, tagId)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_HAS_TAG, true, player, tagId));
        } else {
            tagManager.giveTag(player, tagId);
            PlayerTag tag = tagManager.requestObjectById(tagId);
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_GAVE_TAG, true, player, tag.getDisplayName()));
        }
        return true;
    }
}
