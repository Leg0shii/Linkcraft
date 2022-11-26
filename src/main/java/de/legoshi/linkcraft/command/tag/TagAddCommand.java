package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.tag.TagType;
import de.legoshi.linkcraft.util.CommonsUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "add", desc = "%translatable:tags.add.desc%")
public class TagAddCommand implements CommandClass {

    @Inject private TagManager tagManager;

    @Command(names = "")
    public boolean addTag(CommandSender sender, @Named("name") String name, @Named("rarity") int rarity, @Named("type") int type, ArgumentStack desc) {
        // Cannot use annotations to do this is it requires absolute constants. Static initializer does not even work (I guess you could change the value still)
        if(rarity < 0 || rarity > TagRarity.values().length - 1) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_RARITY_ERROR, true, TagRarity.values().length - 1));
        }
        else if(type < 0 || type > TagType.values().length - 1) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_TYPE_ERROR, true, TagRarity.values().length - 1));
        }
        else {
            PlayerTag playerTag = new PlayerTag(name, CommonsUtils.joinArguments(desc), rarity, type);
            boolean success = tagManager.initObject(playerTag);
            if (success) sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_ADD_SUCCESS, true, name));
            else sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_ADD_ERROR, true));
        }
        return true;
    }

}
