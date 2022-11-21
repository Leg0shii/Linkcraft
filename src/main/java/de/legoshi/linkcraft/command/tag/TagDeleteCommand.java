package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "delete", desc = "%translatable:tags.delete.desc%")
public class TagDeleteCommand implements CommandClass {
    @Inject private TagManager tagManager;

    @Command(names = "")
    public boolean deleteTag(CommandSender sender, @Named("tag") int tagId) {
        if(tagManager.tagExists(tagId)) {
            tagManager.deleteObject(tagId);
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_DELETE_TAG, true, tagId));
        } else {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_NO_TAG, true, tagId));
        }

        return true;
    }
}
