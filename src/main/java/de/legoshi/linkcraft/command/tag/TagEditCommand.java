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

@Command(names = "edit", desc = "%translatable:tags.edit.desc%")
public class TagEditCommand implements CommandClass {
    @Inject
    private TagManager tagManager;

    @Command(names = "")
    public boolean editTag(CommandSender sender, @Named("tag") int tagId, @Named("field") String field, @Named("value") String value, ArgumentStack desc) {
        PlayerTag tag = tagManager.requestObjectById(tagId);
        boolean valid = true;
        switch(field.toLowerCase()) {
            case "name" -> {
                tag.setDisplayName(value);
            }
            case "rarity" -> {
                if(!CommonsUtils.isNumeric(value)) {
                    sender.sendMessage(MessageUtils.composeMessage(Messages.REQUIRE_INTEGER, true, "Rarity"));
                    valid = false;
                } else if(!withinBounds(TagRarity.class, value)) {
                    sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_RARITY_ERROR, true, TagRarity.values().length - 1));
                    valid = false;
                } else {
                    // Scuffed but I don't see why not (parse as float, cast to int to stop non integer errors)
                    tag.setTagRarity(TagRarity.values()[(int)Float.parseFloat(value)]);
                }
            }
            case "type" -> {
                if(!CommonsUtils.isNumeric(value)) {
                    sender.sendMessage(MessageUtils.composeMessage(Messages.REQUIRE_INTEGER, true, "Type"));
                    valid = false;
                } else if(!withinBounds(TagType.class, value)) {
                    sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_TYPE_ERROR, true, TagRarity.values().length - 1));
                    valid = false;
                } else {
                    tag.setTagType(TagType.values()[(int)Float.parseFloat(value)]);
                }
            }
            case "desc" -> {
                String description = value + " " + CommonsUtils.joinArguments(desc);
                tag.setDescription(description);
            }
            default -> {
                sender.sendMessage(MessageUtils.composeMessage(Messages.INVALID_ARG, true, field));
                valid = false;
            }
        }
        if(valid) {
            tagManager.updateObject(tag);
            sender.sendMessage(MessageUtils.composeMessage(Messages.TAGS_UPDATE_SUCCESS, true, tag.getTagID()));
        }
        return true;
    }


    private boolean withinBounds(Class<? extends Enum<?>> clazz, String value) {
        if(!CommonsUtils.isNumeric(value)) {
            return false;
        }

        int num = (int)Float.parseFloat(value);
        return num >= 0 && num <= clazz.getEnumConstants().length - 1;
    }
}
