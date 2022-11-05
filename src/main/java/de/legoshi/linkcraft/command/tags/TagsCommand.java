package de.legoshi.linkcraft.command.tags;

import de.legoshi.linkcraft.util.message.Message;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TagsCommand extends SubCommand {

    // /tags - opens a sorted overview of all the tags (for rarity)

    @Override
    public String getName() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String getSyntax() {
        return Message.COMMAND_SYNTAX.msgTrimArgs("tags");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // open GUI with tags
        System.out.println("Test");
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }

}
