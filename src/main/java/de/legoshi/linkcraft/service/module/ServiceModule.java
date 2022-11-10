package de.legoshi.linkcraft.service.module;

import de.legoshi.linkcraft.service.*;
import team.unnamed.inject.AbstractModule;

public class ServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Service.class).named("command").to(CommandService.class).singleton();
        bind(Service.class).named("event").to(EventService.class).singleton();

        bind(Service.class).to(LinkcraftService.class).singleton();
    }

}
