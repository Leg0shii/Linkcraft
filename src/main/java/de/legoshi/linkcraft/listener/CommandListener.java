package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.manager.CooldownManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@RequiredArgsConstructor
public class CommandListener implements Listener {

    private final CooldownManager cooldownManager;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();

        if(!player.hasPermission("lc.ignoreCmdCooldown")) {

            String commandName = e.getMessage().split(" ")[0];
            e.setCancelled(true);

            if (cooldownManager.hasBothCooldowns(player, commandName)) {
                if (cooldownManager.isGlobalShorter(player, commandName)) {
                    player.sendMessage(cooldownManager.getMessage(cooldownManager.getCooldown(player, commandName)));
                } else {
                    player.sendMessage(cooldownManager.getMessage(cooldownManager.getGlobalCooldown(player)));
                }
            }
            else if(cooldownManager.hasGlobalCooldown(player)) {
                player.sendMessage(cooldownManager.getMessage(cooldownManager.getGlobalCooldown(player)));
            }
            else if(cooldownManager.hasCooldown(player, commandName)) {
                player.sendMessage(cooldownManager.getMessage(cooldownManager.getCooldown(player, commandName)));
            }
            else {
                e.setCancelled(false);
                cooldownManager.addGlobalCooldown(player);
                cooldownManager.addCooldown(player, commandName);
            }
        }
    }
}
