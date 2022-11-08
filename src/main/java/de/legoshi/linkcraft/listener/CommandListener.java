package de.legoshi.linkcraft.listener;

import de.legoshi.linkcraft.command.LCCommandManager;
import de.legoshi.linkcraft.manager.CooldownManager;
import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.NamespaceImpl;
import me.fixeddev.commandflow.exception.CommandException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.Arrays;

@RequiredArgsConstructor
public class CommandListener implements Listener {

    private final CooldownManager cooldownManager;
    private final LCCommandManager lcCommandManager;

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
