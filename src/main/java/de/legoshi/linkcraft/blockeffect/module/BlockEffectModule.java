package de.legoshi.linkcraft.blockeffect.module;

import de.legoshi.linkcraft.blockeffect.BlockEffect;
import team.unnamed.inject.AbstractModule;

public class BlockEffectModule extends AbstractModule {
    @Override
    public void configure() {
        bind(BlockEffect.class);
    }
}
