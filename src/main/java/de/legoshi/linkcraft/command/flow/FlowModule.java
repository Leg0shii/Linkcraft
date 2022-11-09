package de.legoshi.linkcraft.command.flow;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.usage.DefaultUsageBuilder;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.AbstractModule;
import team.unnamed.inject.Provides;

import javax.inject.Singleton;

public class FlowModule extends AbstractModule {

    @Override
    protected void configure() {}

    @Provides
    @Singleton
    public CommandManager provideCommandManager(Plugin plugin) {
        CommandManager commandManager = new BukkitCommandManager(plugin.getName());
        commandManager.setUsageBuilder(new DefaultUsageBuilder() {
            @Override
            public Component getUsage(CommandContext commandContext) {
                return TextComponent.of("Usage: ")
                        .color(TextColor.RED)
                        .append(super.getUsage(commandContext));
            }
        });
        return commandManager;
    }
}
