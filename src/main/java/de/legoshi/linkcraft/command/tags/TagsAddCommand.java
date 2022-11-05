package de.legoshi.linkcraft.command.tags;

import de.legoshi.linkcraft.util.message.Message;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

// /tags add <name> <rarity> [description]  - name has to be unique
public class TagsAddCommand extends SubCommand {

    @Override
    public String getName() {
        return "add";
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
        return Message.COMMAND_SYNTAX.msgTrimArgs("tags", "add", "<name>", "<rarity>", "[description]");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // save a new tag to the database
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
