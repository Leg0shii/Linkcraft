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
        FileWriter replayConfigDB = new FileWriter(configPath, dbConfigName);
        if (!fileDB.exists()) {
            try {
                fileDB.createNewFile();
                replayConfigDB.setValue("host", "localhost");
                replayConfigDB.setValue("port", 3306);
                replayConfigDB.setValue("username", "root");
                replayConfigDB.setValue("password", "root");
                replayConfigDB.setValue("database", "replaytest");
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("Error with loading dbconfig....");
            }
            replayConfigDB.save();
        }
    }

}
