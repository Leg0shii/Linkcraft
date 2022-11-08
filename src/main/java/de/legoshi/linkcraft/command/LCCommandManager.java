package de.legoshi.linkcraft.command;

import de.legoshi.linkcraft.command.tag.TagCommand;
import de.legoshi.linkcraft.database.DBManager;
import lombok.Getter;
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

@Getter
public class LCCommandManager {

    @Inject private CommandManager commandManager;

    @Inject private Plugin plugin;
    @Inject private Injector injector;
    // @Inject private DBManager dbManager;

    @Inject private TagCommand tagCommand;

    public LCCommandManager() {
        /* PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        injector.install(new BukkitModule());

        register(
                tagCommand
        ); */
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

}
