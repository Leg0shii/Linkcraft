package de.legoshi.linkcraft.database;
import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.util.FileWriter;
import de.legoshi.linkcraft.util.LCConfig;
import org.bukkit.Bukkit;

import java.sql.SQLException;

public class DBManager {

    public AsyncMySQL mySQL;

    public AsyncMySQL initializeTables() {
        this.mySQL = connectToDB();
        if(mySQL != null) {
            Bukkit.getConsoleSender().sendMessage("DB connected.");
            //mySQL.update("CREATE TABLE IF NOT EXISTS playerclip (clipid INT NOT NULL AUTO_INCREMENT, playerUUID VARCHAR(255), world VARCHAR(255), playerjoin BOOL, reviewed BOOL, saved BOOL, publicclip BOOL, date BigInt, clip BLOB, PRIMARY KEY(clipid));");
        } else {
            Bukkit.getConsoleSender().sendMessage("DB couldn't be connected.");
        }
        return mySQL;
    }

    public AsyncMySQL connectToDB() {
        FileWriter config = LCConfig.getDBFileWriter();

        String host = config.getString("host");
        int port = config.getInt("port");
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");

        try {
            mySQL = new AsyncMySQL(Linkcraft.getInstance(), host, port, username, password, database);
            return mySQL;
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return null;
    }

}
