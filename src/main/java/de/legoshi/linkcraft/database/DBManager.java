package de.legoshi.linkcraft.database;
import de.legoshi.linkcraft.util.FileWriter;
import de.legoshi.linkcraft.util.LCConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private Plugin plugin;
    @Getter private AsyncMySQL mySQL;
    
    @Getter private final String COMMAND_COOLDOWNS_SELECT = "SELECT * FROM lc_command_cooldowns WHERE LOWER(command)=LOWER(?);";

    public DBManager(Plugin plugin) {
        this.plugin = plugin;
        this.mySQL = connectToDB();

        initializeTables();
    }

    public void initializeTables() {
        if(mySQL != null) {
            Bukkit.getConsoleSender().sendMessage("[LinkCraft] DB connected");
            createSchema();
        } else {
            Bukkit.getConsoleSender().sendMessage("[LinkCraft] Cannot connect to DB");
        }
    }

    public AsyncMySQL connectToDB() {
        FileWriter config = LCConfig.getDBFileWriter();

        String host = config.getString("host");
        int port = config.getInt("port");
        String username = config.getString("username");
        String password = config.getString("password");
        String database = config.getString("database");

        try {
            mySQL = new AsyncMySQL(plugin, host, port, username, password, database);
            return mySQL;
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return null;
    }

    private void createSchema() {
        try (InputStream is = plugin.getResource("mysql.sql")) {
            for(String statement : getStatements(is)) {
                mySQL.update(statement);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getStatements(InputStream is) {
        List<String> statements = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {

                if(!line.startsWith("--") && !line.startsWith("//")) {
                    sb.append(line);

                    if(line.endsWith(";")) {
                        sb.deleteCharAt(sb.length() - 1);

                        String result = sb.toString().trim();
                        if(!result.isEmpty()) {
                            statements.add(result);
                        }

                        sb = new StringBuilder();
                    }
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return statements;
    }

    public int getAutoGenID(PreparedStatement stmt) {
        try {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /* public void insertObjectIntoDatabase(Saveable saveable) {
        HashMap<String, Object> tupleList = saveable.getKeyValueList();
        HashMap<String, HashMap<String, Object>> tableKeyValueList = new HashMap<>();

        for (String key : tupleList.keySet()) {
            String[] args = key.split("\\.");
            String table = args[0];
            if (!tableKeyValueList.containsKey(table)) {
                tableKeyValueList.put(table, new HashMap<>());
            }
            tableKeyValueList.get(table).put(key, tupleList.get(key));
        }

        for (String tableKey : tableKeyValueList.keySet()) {
            HashMap<String, Object> list = tableKeyValueList.get(tableKey);
            String keys = "";
            String values = "";
            for (String key : list.keySet()) {
                keys = keys.equals("") ? key : keys + ", " + key;
                String value = (String) list.get(key);
                values = values.equals("") ? value : values + ", " + value;
            }
            mySQL.query("INSERT INTO " + tableKey + " (" + keys + ") VALUES (" + values + ");");
        }
    } */

}
