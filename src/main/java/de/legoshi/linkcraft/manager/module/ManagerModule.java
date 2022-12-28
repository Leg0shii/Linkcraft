package de.legoshi.linkcraft.manager.module;

import de.legoshi.linkcraft.manager.*;
import team.unnamed.inject.AbstractModule;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlayerManager.class).singleton();
        bind(CooldownManager.class).singleton();
        bind(TagManager.class).singleton();
        bind(SaveStateManager.class).singleton();
        bind(PlayThroughManager.class).singleton();
    }

}
