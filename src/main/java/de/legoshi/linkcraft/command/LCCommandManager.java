package de.legoshi.linkcraft.command;

import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.LinkcraftModule;
import de.legoshi.linkcraft.command.tag.TagCommand;
import lombok.Getter;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.Namespace;
import me.fixeddev.commandflow.SimpleCommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.builder.AnnotatedCommandBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.SimplePartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.input.QuotedSpaceTokenizer;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;
import javax.swing.text.html.HTML;
import java.util.List;

@Getter
public class LCCommandManager {

    private final CommandManager commandManager;
    // private final AnnotatedCommandTreeBuilder treeBuilder;

    public LCCommandManager() {
        PartInjector partInjector = PartInjector.create();
        partInjector.install(new BukkitModule());
        partInjector.install(new DefaultsModule());

        this.commandManager = new BukkitCommandManager(JavaPlugin.getPlugin(Linkcraft.class).getName());

        Injector injector = Injector.create(new LinkcraftModule());
        AnnotatedCommandTreeBuilder treeBuilder = new AnnotatedCommandTreeBuilderImpl(
                new AnnotatedCommandBuilderImpl(partInjector),
                (clazz, parent) -> injector.getInstance(clazz)
        );
        commandManager.setInputTokenizer(new QuotedSpaceTokenizer());

        List<me.fixeddev.commandflow.command.Command> commands = treeBuilder.fromClass(injector.getInstance(TagCommand.class));
        commandManager.registerCommands(commands);
    }

}
