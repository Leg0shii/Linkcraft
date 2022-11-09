package de.legoshi.linkcraft;

import de.legoshi.linkcraft.service.Service;
import de.legoshi.linkcraft.util.LCConfig;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.inject.Injector;

import javax.inject.Inject;

public final class Linkcraft extends JavaPlugin {

    @Inject private Service service;

    @Override
    public void onEnable() {
        LCConfig.initConfig();

        Injector injector = Injector.create(new LinkcraftModule(this));
        injector.injectMembers(this);

        service.start();
    }

    @Override
    public void onDisable() {
        // mySQL.getMySQL().closeConnection();
    }

}
