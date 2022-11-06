package de.legoshi.linkcraft.command;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TagCommand implements CommandClass, CommandExecutor {

    @Command(names = "test")
    public void test(@Named("name") String name) throws NoMoreArgumentsException {
        System.out.println("Hi " + name);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        return false;
    }
}
