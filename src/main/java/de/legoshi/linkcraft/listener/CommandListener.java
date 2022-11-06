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
        event.setCancelled(true);

        if(!player.hasPermission("lc.ignoreCmdCooldown")) {
            if (cooldownManager.hasBothCooldowns(player, commandName)) {
                if (cooldownManager.isGlobalShorter(player, commandName)) {
                    player.sendMessage(cooldownManager.getMessage(cooldownManager.getCooldown(player, commandName)));
                } else {
                    player.sendMessage(cooldownManager.getMessage(cooldownManager.getGlobalCooldown(player)));
                }
                return;
            }
            else if(cooldownManager.hasGlobalCooldown(player)) {
                player.sendMessage(cooldownManager.getMessage(cooldownManager.getGlobalCooldown(player)));
                return;
            }
            else if(cooldownManager.hasCooldown(player, commandName)) {
                player.sendMessage(cooldownManager.getMessage(cooldownManager.getCooldown(player, commandName)));
                return;
            }
            else {
                cooldownManager.addGlobalCooldown(player);
                cooldownManager.addCooldown(player, commandName);
            }
        }

        executeCommand(event, event.getPlayer(), args);
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        event.setCancelled(true);
        String[] args = event.getCommand().split(" ");
        executeCommand(event, event.getSender(), args);
    }

    private void executeCommand(Event event, CommandSender sender, String[] args) {
        CommandManager commandManager = lcCommandManager.getCommandManager();
        if (commandManager.getCommands().stream().anyMatch((command -> command.getName().equals(args[0])))) {
            try {
                commandManager.execute(new NamespaceImpl(), Arrays.asList(args));
            } catch (CommandException exception) {
                // TODO: print out help for command
                sender.sendMessage("/help bro..");
            }
        } else {
            if (event instanceof ServerCommandEvent cEvent) cEvent.setCancelled(false);
            if (event instanceof PlayerCommandPreprocessEvent cEvent) cEvent.setCancelled(false);
        }
    }
}
