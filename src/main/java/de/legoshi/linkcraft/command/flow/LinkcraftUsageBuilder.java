package de.legoshi.linkcraft.command.flow;

import com.google.common.base.CaseFormat;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import lombok.RequiredArgsConstructor;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.part.defaults.SequentialCommandPart;
import me.fixeddev.commandflow.part.defaults.SubCommandPart;
import me.fixeddev.commandflow.translator.Translator;
import me.fixeddev.commandflow.usage.UsageBuilder;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import javax.inject.Inject;
import java.util.Arrays;

@RequiredArgsConstructor
public class LinkcraftUsageBuilder implements UsageBuilder {

    private final CommandManager commandManager;

    @Override
    public Component getUsage(CommandContext commandContext) {
        Command command = commandContext.getCommand();

        String label = String.join(" ", commandContext.getLabels());

        String labelComponent = MessageUtils.getMessageTranslated(Messages.USAGE_ENTRY, true);
        Component partComponents = command.getPart().getLineRepresentation();
        Component description = getTranslatedDescription(command);

        if (partComponents == null) {
            partComponents = TextComponent.empty();
        }
        if (MessageUtils.isNullOrEmpty(description)) {
            description = MessageUtils.messageOf(Messages.UNKNOWN_DESC);
        }

        TextComponent.Builder builder = TextComponent.builder(label).append(" ").append(formatPart(partComponents));

        // how do i remove the [subcommand] from the textcomponent
        CommandPart part = command.getPart();
        if (part instanceof SequentialCommandPart part1) {
            for (CommandPart subCommand : part1.getParts()) {
                if (subCommand instanceof SubCommandPart subCommandPart) {
                    for (Command command1 : subCommandPart.getSubCommands()) {
                        builder.append(":").append(command1.getName());
                    }
                }
            }
        }
        TextComponent component = builder.build();
        return MessageUtils.composeComponent(labelComponent, Arrays.asList(component, description), false);
    }

    private Component formatPart(Component component) {
        if (MessageUtils.componentIsEmpty(component)) {
            return component;
        }
        String[] parts = MessageUtils.toLegacy(component).split("<");
        TextComponent.Builder builder = TextComponent.builder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (i > 0) {
                part = "<" + part;
            }
            builder.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, part));
        }
        return builder.build();
    }

    Component getTranslatedDescription(Command command) {
        Component description = command.getDescription();
        Translator translator = commandManager.getTranslator();
        return translator.translate(description, null);
    }

}
