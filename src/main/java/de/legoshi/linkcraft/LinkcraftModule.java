package de.legoshi.linkcraft;

import de.legoshi.linkcraft.blockeffect.module.BlockEffectModule;
import de.legoshi.linkcraft.command.flow.FlowModule;
import de.legoshi.linkcraft.database.module.DatabaseModule;
import de.legoshi.linkcraft.gui.module.GuiModule;
import de.legoshi.linkcraft.manager.module.ManagerModule;
import de.legoshi.linkcraft.service.module.ServiceModule;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.AbstractModule;

public class LinkcraftModule extends AbstractModule {

    private final Plugin plugin;

    public LinkcraftModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(this.plugin);

        install(new DatabaseModule(plugin));

        install(new FlowModule());

        install(new ServiceModule());
        install(new ManagerModule());

        install(new GuiModule());

        install(new BlockEffectModule());
    }

}

