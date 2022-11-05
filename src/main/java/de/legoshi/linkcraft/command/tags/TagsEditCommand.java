package de.legoshi.linkcraft.command.tags;

import de.legoshi.linkcraft.util.message.Message;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

// /tags edit name <id> <new_name>
// /tags edit rarity <id> <rarity>
// /tags edit description <id> <description>
public class TagsEditCommand extends SubCommand {

    @Override
    public String getName() {
        return "edit";
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
        return Message.COMMAND_SYNTAX.msgTrimArgs("tags", "edit", "<name:rarity:desc>", "<tag_id>", "<value>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // update values of tag in database
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
