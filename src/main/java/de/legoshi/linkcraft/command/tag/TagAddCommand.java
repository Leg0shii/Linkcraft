package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.manager.TagManager;
import de.legoshi.linkcraft.tag.PlayerTag;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Named;

@Command(names = "add", desc = "%translatable:tags.add.desc%")
public class TagAddCommand implements CommandClass {

    @Inject private TagManager tagManager;

    @Command(names = "")
    public boolean addTags(CommandSender sender, @Named("name") String name, @Named("rarity") int rarity, @OptArg("description") String desc) {
        String tagDesc = desc.isBlank() ? "" : desc;
        PlayerTag playerTag = new PlayerTag(name, tagDesc, rarity);
        tagManager.initObject(playerTag);
        return true;
    }

}
