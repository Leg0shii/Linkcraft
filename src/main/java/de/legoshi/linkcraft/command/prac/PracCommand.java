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
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class PracCommand implements CommandClass {

    @Inject PlayerManager playerManager;
    @Inject PracticeManager practiceManager;

    @Command(names = "prac", desc = "%translatable:prac.desc%")
    public boolean prac(@Sender CommandSender sender) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player)sender;
        AbstractPlayer aPlayer = playerManager.getPlayer(player);

        // This feels quite weird, unsure if I am using the AbstractPlayer class correctly.
        // In this case, it would be useful to determine if the player was already in practice vs in a maze (both can't practice)
        // The error message currently feels a bit strange and vague "You cannot practice right now."
        // But I do not see a way to do this without using aPlayer instanceof PracticePlayer, aPlayer instanceof MazePlayer... ect
        if(!aPlayer.canPractice()) {
            player.sendMessage(MessageUtils.composeMessage(Messages.PRAC_NOT_ALLOWED, true));
            return true;
        }

        if(!player.isOnGround()) {
            player.sendMessage(MessageUtils.composeMessage(Messages.PRAC_IN_AIR, true));
            return true;
        }

        if(ItemUtils.isFullInventory(player)) {
            player.sendMessage(MessageUtils.composeMessage(Messages.INVENTORY_FULL, true, "practice"));
            return true;
        }

        practiceManager.practice(player);

        // Not sure if this should be moved to the practice manager. I assume it should but would require getting the abstract player again
        playerManager.updatePlayerState(aPlayer, PracticePlayer.class);
        player.getInventory().addItem(ItemUtils.addNbtId(ItemUtils.setItemText(new ItemStack(Material.SLIME_BALL), "§3§l【§b§lLC§3§l】- §4Return"), "prac"));
        player.sendMessage(MessageUtils.composeMessage(Messages.PRAC_SUCCESS, true));


        return true;
    }
}
