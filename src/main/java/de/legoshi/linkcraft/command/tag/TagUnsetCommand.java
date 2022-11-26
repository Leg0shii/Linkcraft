package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.PlayerManager;
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
    @Inject private PlayerManager playerManager;

    @Command(names = "")
    public boolean unsetTag(CommandSender sender, @OptArg("") String player) {
        if(player != null && !player.isBlank()) {
            if(playerManager.playerExists(player)) {
                tagManager.setDefaultTag(player);
                sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_TAG_OTHER, true, player));
            } else {
                sender.sendMessage(MessageUtils.composeMessage(Messages.NEVER_JOINED, true, player));
            }
        // this command is for admins
        // why does it change the tag of the admin?
        } else if(sender instanceof Player p) {
            boolean success = tagManager.setDefaultTag(p.getDisplayName());
            if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_TAG, true));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UNSET_TAG, true));
        }
        return true;
    }
}
