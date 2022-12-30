package de.legoshi.linkcraft.command.prac;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.PracticeManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.playertype.PracticePlayer;
import de.legoshi.linkcraft.util.ItemUtils;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class UnpracCommand implements CommandClass {
    @Inject
    PlayerManager playerManager;
    @Inject PracticeManager practiceManager;

    @Command(names = "unprac", desc = "%translatable:unprac.desc%")
    public boolean prac(@Sender CommandSender sender) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player)sender;
        AbstractPlayer aPlayer = playerManager.getPlayer(player);

        if(!aPlayer.canUnpractice()) {
            player.sendMessage(MessageUtils.composeMessage(Messages.PRAC_NOT_IN_PRAC, true));
            return true;
        }


        PracticePlayer practicePlayer = (PracticePlayer)aPlayer;

        ItemUtils.removeItemIf(player, (itemStack -> ItemUtils.hasNbtId(itemStack, "prac")));

        practiceManager.unpractice(player);
        playerManager.updatePlayerState(aPlayer, practicePlayer.getPrevious());
        player.sendMessage(MessageUtils.composeMessage(Messages.PRAC_UNPRAC, true));


        return true;
    }
}
