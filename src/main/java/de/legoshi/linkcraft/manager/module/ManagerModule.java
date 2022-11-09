package de.legoshi.linkcraft.manager.module;

import de.legoshi.linkcraft.manager.CooldownManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import team.unnamed.inject.AbstractModule;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlayerManager.class).toProvider(PlayerManager::new).singleton();
        bind(CooldownManager.class).toProvider(CooldownManager::new).singleton();

        // bind(PlayerManager.class).toProvider(PlayerManager::new).singleton();
        // bind(CooldownManager.class).toProvider(CooldownManager::new).singleton();
    }

}
