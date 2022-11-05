package de.legoshi.linkcraft.command.tags;

import de.legoshi.linkcraft.util.message.Message;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

// /tags set <user_name> <id>
public class TagsSetCommand extends SubCommand {

    @Override
    public String getName() {
        return "set";
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
        return Message.COMMAND_SYNTAX.msgTrimArgs("tags", "set", "<user_name>", "<tag_id>");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        // update database so user has this tag set
    }

    @Override
    public List<String> getSubcommandArguments(Player player, String[] args) {
        return null;
    }
}
