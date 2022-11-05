package de.legoshi.linkcraft.command.tags;

import de.legoshi.linkcraft.util.message.Message;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

// /tags unset <user_name> <id>
// /tags unset <user_name> all
public class TagsUnsetCommand extends SubCommand {
    @Override
    public String getName() {
        return "unset";
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
        return Message.COMMAND_SYNTAX.msgTrimArgs("tags", "unset", "<user_name>", "<tag_id:all>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // remove player tag from certain player from database
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
