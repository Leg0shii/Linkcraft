package de.legoshi.linkcraft;

import de.legoshi.linkcraft.command.LCCommandManager;
import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.listener.ChatListener;
import de.legoshi.linkcraft.listener.CommandListener;
import de.legoshi.linkcraft.listener.JoinListener;
import de.legoshi.linkcraft.listener.QuitListener;
import de.legoshi.linkcraft.manager.CooldownManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.util.LCConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Linkcraft extends JavaPlugin {

    private DBManager dbManager;
    private AsyncMySQL mySQL;

    private PlayerManager playerManager;
    private CooldownManager cooldownManager;
    private LCCommandManager lcCommandManager;

    @Getter
    private static Linkcraft instance;

    @Override
    public void onEnable() {
        LCConfig.initConfig();

        // Injector injector = Injector.create(new LinkcraftModule(this));
        // injector.injectMembers(this);

        this.dbManager = new DBManager(this, LCConfig.getDBFileWriter().getString("db_prefix"));
        this.mySQL = this.dbManager.initializeTables();
        this.playerManager = new PlayerManager();
        this.cooldownManager = new CooldownManager(dbManager);

        this.lcCommandManager = new LCCommandManager();

        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        mySQL.getMySQL().closeConnection();
    }

    private void registerEvents() {
        PluginManager pM = Bukkit.getPluginManager();
        pM.registerEvents(new JoinListener(playerManager), this);
        pM.registerEvents(new QuitListener(playerManager), this);
        pM.registerEvents(new ChatListener(playerManager), this);
        pM.registerEvents(new CommandListener(cooldownManager, lcCommandManager), this);
    }

    private void registerCommands() {
        // getCommand("").setExecutor();
    }

}
