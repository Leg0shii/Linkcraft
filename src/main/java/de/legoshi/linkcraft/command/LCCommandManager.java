package de.legoshi.linkcraft.command;

import lombok.Getter;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;

import java.util.List;

@Getter
public class LCCommandManager {

    private final CommandManager commandManager;
    private final AnnotatedCommandTreeBuilder treeBuilder;

    public LCCommandManager() {
        this.commandManager = new SimpleCommandManager();
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        PartInjector injector = PartInjector.create();
        injector.install(new DefaultsModule());
        this.treeBuilder = new AnnotatedCommandTreeBuilderImpl(injector);

        registerCommands();
    }

    private void registerCommands() {
        List<Command> commands = treeBuilder.fromClass(new TagCommand());
        commandManager.registerCommands(commands);
    }

}
