package de.legoshi.linkcraft.service;

import de.legoshi.linkcraft.service.module.ServiceModule;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Named;

public class LinkcraftService implements Service {

    @Inject private Plugin plugin;
    @Inject @Named("database") private Service databaseService;
    @Inject @Named("command") private Service commandService;
    @Inject @Named("event") private Service eventService;

    @Override
    public void start() {
        databaseService.start();
        commandService.start();
        eventService.start();
    }

    @Override
    public void stop() {
        databaseService.stop();
        commandService.stop();
        eventService.stop();
    }
}
