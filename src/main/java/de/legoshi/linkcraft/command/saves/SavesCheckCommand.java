package de.legoshi.linkcraft.command.saves;

import de.legoshi.linkcraft.gui.save.AdminSavesHolder;
import de.legoshi.linkcraft.gui.save.SavesHolder;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

// /saves check <player>
@Command(names = "check")
public class SavesCheckCommand implements CommandClass {

    @Inject private Injector injector;

    @Command(names = "")
    public boolean check(CommandSender sender, String playerName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessageUtils.composeMessage(Messages.NOT_A_PLAYER.getMessage(), true));
            return true;
        }

        Player player = (Player)sender;

        // check if playerName correlates to a player

        injector.getInstance(AdminSavesHolder.class).openGui(player, null, playerName);
        return true;
    }

}
