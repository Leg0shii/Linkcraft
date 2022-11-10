package de.legoshi.linkcraft;

import de.legoshi.linkcraft.gui.tag.TagMenu;
import de.legoshi.linkcraft.service.Service;
import de.legoshi.linkcraft.util.LCConfig;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

public final class Linkcraft extends JavaPlugin {

    @Getter private static Plugin plugin;

    @Inject private Service service;

    @Override
    public void onEnable() {
        LCConfig.initConfig();
        plugin = this;

        Injector injector = Injector.create(new LinkcraftModule(this));
        injector.injectMembers(this);

        service.start();
    }

    @Override
    public void onDisable() {
        // mySQL.getMySQL().closeConnection();
    }

}
