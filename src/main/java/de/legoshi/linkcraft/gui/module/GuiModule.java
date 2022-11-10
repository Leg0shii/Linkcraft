package de.legoshi.linkcraft.gui.module;

import de.legoshi.linkcraft.gui.GUIPane;
import team.unnamed.inject.AbstractModule;

public class GuiModule extends AbstractModule {

    @Override
    public void configure() {
        bind(GUIPane.class);
    }

}
