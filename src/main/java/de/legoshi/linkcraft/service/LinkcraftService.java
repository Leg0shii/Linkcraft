package de.legoshi.linkcraft.service;

import javax.inject.Inject;
import javax.inject.Named;

public class LinkcraftService implements Service {

    @Inject @Named("command") private Service commandService;
    @Inject @Named("event") private Service eventService;

    @Override
    public void start() {
        commandService.start();
        eventService.start();
    }

    @Override
    public void stop() {
        commandService.stop();
        eventService.stop();
    }
}
