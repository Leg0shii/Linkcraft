package de.legoshi.linkcraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TagsCommand implements CommandExecutor {

    // Possible commands
    // /tags - opens a sorted overview of all the tags (for rarity)
    // /tags add <name> <rarity> [description]  - name has to be unique
    // /tags edit name <id> <new_name>
    // /tags edit rarity <id> <rarity>
    // /tags edit description <id> <description>
    // /tags remove <id>
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }

}
