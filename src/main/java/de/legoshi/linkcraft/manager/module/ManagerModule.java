package de.legoshi.linkcraft.manager.module;

import de.legoshi.linkcraft.manager.EffectBlockManager;
import de.legoshi.linkcraft.manager.CooldownManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.TagManager;
import team.unnamed.inject.AbstractModule;

public class ManagerModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PlayerManager.class).singleton();
        bind(CooldownManager.class).singleton();
        bind(EffectBlockManager.class).singleton();
        bind(TagManager.class).singleton();
    }

}
