package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.database.DatabaseService;
import de.legoshi.linkcraft.manager.CooldownManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import javax.inject.Inject;

public class CommandListener implements Listener {

    // @Inject private DatabaseService dbManager;
    @Inject private CooldownManager cooldownManager;

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String[] args = event.getMessage().replace("/", "").split(" ");
        String commandName = args[0];

        if(!player.hasPermission("lc.ignoreCmdCooldown")) {
            event.setCancelled(true);
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
                event.setCancelled(false);
                cooldownManager.addGlobalCooldown(player);
                cooldownManager.addCooldown(player, commandName);
            }
        }
    }
}
