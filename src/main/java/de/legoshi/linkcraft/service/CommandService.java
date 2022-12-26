package de.legoshi.linkcraft.service;

import de.legoshi.linkcraft.command.SpawnCommand;
import de.legoshi.linkcraft.command.effectblock.EffectBlockCommand;
import de.legoshi.linkcraft.command.fun.FunCommand;
import de.legoshi.linkcraft.command.map.MapsCommand;
import de.legoshi.linkcraft.command.saves.SavesCommand;
import de.legoshi.linkcraft.command.tag.TagCommand;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.SimplePartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Injector;
import javax.inject.Inject;

public class CommandService implements Service {

    @Inject private CommandManager commandManager;

    @Inject private Plugin plugin;
    @Inject private Injector injector;

    @Inject private TagCommand tagCommand;
    @Inject private FunCommand funCommand;
    @Inject private MapsCommand mapsCommand;
    @Inject private EffectBlockCommand effectBlockCommand;
    @Inject private SavesCommand savesCommand;
    @Inject private SpawnCommand spawnCommand;

    @Override
    public void start() {
        System.out.println("Started command service.");
        register(
                tagCommand,
                funCommand,
                mapsCommand,
                effectBlockCommand,
                savesCommand,
                spawnCommand
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
