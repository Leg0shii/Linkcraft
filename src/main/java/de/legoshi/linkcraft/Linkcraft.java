package de.legoshi.linkcraft;

import de.legoshi.linkcraft.command.LCCommandManager;
import de.legoshi.linkcraft.command.tags.*;
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
import me.kodysimpson.simpapi.colors.ColorTranslator;
import me.kodysimpson.simpapi.command.CommandList;
import me.kodysimpson.simpapi.command.CommandManager;
import me.kodysimpson.simpapi.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.List;

public final class Linkcraft extends JavaPlugin {

    private DBManager dbManager;
    private AsyncMySQL mySQL;

    private PlayerManager playerManager;
    private LCCommandManager commandManager;

    @Getter
    private static Linkcraft instance;

    @Override
    public void onEnable() {
        LCConfig.initConfig();

        instance = this;

        this.dbManager = new DBManager(this, LCConfig.getDBFileWriter().getString("db_prefix"));
        this.mySQL = this.dbManager.initializeTables();
        this.playerManager = new PlayerManager();
        this.cooldownManager = new CooldownManager();
        this.commandManager = new LCCommandManager();

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
        pM.registerEvents(new CommandListener(cooldownManager), this);
    }

    private void registerCommands() {
        // getCommand("").setExecutor();
    }

}
