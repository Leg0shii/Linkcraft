package de.legoshi.linkcraft.command.effectblock;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.ArgOrSub;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;

@Command(names = "effectblock", desc = "%translatable:effectblock.desc%")
@SubCommandClasses({
        EffectBlockAddCommand.class,
        EffectBlockRemoveCommand.class
})
@ArgOrSub(value = true)
public class EffectBlockCommand implements CommandClass {
    @Inject
    private EffectBlockHelpCommand effectBlockHelpCommand;

    @Command(names = "")
    public boolean effectBlocks(@Sender CommandSender sender, CommandContext commandContext) {
        effectBlockHelpCommand.effectBlockHelp(sender);
        return true;
    }
}
