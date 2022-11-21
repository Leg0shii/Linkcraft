package de.legoshi.linkcraft.command.sign;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import javax.inject.Named;

@Command(names = "create", permission = "sign.create")
public class SignCreateCommand implements CommandClass {

    // /sign create tag <tag-id>
    // /sign create finish <type> <map-id>
    // /sign create cp
    // /sign create teleport

    @Command(names = "tag")
    public boolean signCreateTag(CommandSender commandSender, @Named(value = "tagId") String tagId) {
        return true;
    }

    @Command(names = "finish")
    public boolean signCreateFinish(Player player, String type, String mapId) {

        return true;
    }

    @Command(names = "cp")
    public boolean signCreateCp(Player player) {

        return true;
    }

    @Command(names = "teleport")
    public boolean signCreateTeleport(Player player) {

        return true;
    }

}
