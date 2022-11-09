package de.legoshi.linkcraft.service;

import de.legoshi.linkcraft.command.tag.TagCommand;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.SimplePartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.usage.DefaultUsageBuilder;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

public class CommandService implements Service {

    private CommandManager commandManager;

    @Inject private Plugin plugin;
    @Inject private Injector injector;

    @Inject private TagCommand tagCommand;

    @Override
    public void start() {
        System.out.println("Started command service.");

        this.commandManager = new BukkitCommandManager(plugin.getName());

        commandManager.setUsageBuilder(new DefaultUsageBuilder() {
            @Override
            public Component getUsage(CommandContext commandContext) {
                // So it seems like we can access the raw command from the context and from that we can access annotations
                // I feel like it would be beneficial to be able to add annotations, or perhaps I'm missing something
                // Command.class only contains names, desc, permission, permissionMessage, so I believe a '@Usage' annotation directly on the class would never work
                // This is a hack to use the description annotation as a usage annotation, temp ofc.
                // also im probably not understanding again because I have no clue what anything does lmao (seems like we might be able to do something with the part injector...)
                return TextComponent.of("")
                        .color(TextColor.RED)
                        .append(commandContext.getCommand().getDescription());
            }
        });

        register(
                tagCommand
        );
    }

    private void register(CommandClass... commandClasses) {
        PartInjector partInjector = new SimplePartInjector();
        partInjector.install(new BukkitModule());
        partInjector.install(new DefaultsModule());

        AnnotatedCommandTreeBuilder treeBuilder = new AnnotatedCommandTreeBuilderImpl(
                new AnnotatedCommandBuilderImpl(partInjector),
                (clazz, parent) -> injector.getInstance(clazz)
        );

        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(treeBuilder.fromClass(commandClass));
        }
    }

    @Override
    public void stop() {

    }
}
