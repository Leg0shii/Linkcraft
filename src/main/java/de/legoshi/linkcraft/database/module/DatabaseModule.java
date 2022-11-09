package de.legoshi.linkcraft.database.module;

import de.legoshi.linkcraft.database.DBManager;
import org.bukkit.plugin.Plugin;
import team.unnamed.inject.Binder;
import team.unnamed.inject.Module;

public class DatabaseModule implements Module {

    private final Plugin plugin;

    public DatabaseModule(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(DBManager.class).toProvider(() -> new DBManager(plugin)).singleton();
    }
}
