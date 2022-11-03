package de.legoshi.linkcraft.util;

import com.sun.tools.sjavac.comp.FileObjectWithLocation;
import org.bukkit.Bukkit;

import java.io.File;

public class LCConfig {

    private static final String configPath = "./plugins/LinkcraftConfiguration/";
    private static final String dbConfigName = "lcdbconfig.yaml";

    public static void initConfig() {
        initConfigFolder();
        initDBConfig();
    }

    public static FileWriter getDBFileWriter() {
        return new FileWriter(LCConfig.configPath, LCConfig.dbConfigName);
    }

    private static void initConfigFolder() {
        File dir = new File(configPath);
        if (!dir.exists()) dir.mkdir();
    }

    private static void initDBConfig() {
        File fileDB = new File(configPath + dbConfigName);
        FileWriter lcDbConfig = new FileWriter(configPath, dbConfigName);
        if (!fileDB.exists()) {
            try {
                if(fileDB.createNewFile()) {
                    lcDbConfig.setValue("host", "localhost");
                    lcDbConfig.setValue("port", 3306);
                    lcDbConfig.setValue("username", "root");
                    lcDbConfig.setValue("password", "root");
                    lcDbConfig.setValue("database", "replaytest");
                    lcDbConfig.setValue("db_prefix", "lc_");
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("[LinkCraft] Error loading dbconfig....");
            }
            lcDbConfig.save();
        }
    }

}
