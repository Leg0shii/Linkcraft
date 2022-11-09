package de.legoshi.linkcraft.command.tag;

import de.legoshi.linkcraft.database.DBManager;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Usage;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

@Command(names = "add", desc = "%translatable:command.tag-description%")
public class TagAddCommand implements CommandClass {

    @Inject private DBManager databaseService;

    @Command(names = "")
    public boolean addTags(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Add a playertag");
        return true;
    }

}
