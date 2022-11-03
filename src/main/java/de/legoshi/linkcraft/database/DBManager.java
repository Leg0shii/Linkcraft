package de.legoshi.linkcraft.database;
import de.legoshi.linkcraft.Linkcraft;
import de.legoshi.linkcraft.util.FileWriter;
import de.legoshi.linkcraft.util.LCConfig;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBManager {

    public AsyncMySQL mySQL;
    private final Function<String, String> prefixProcessor;
    private final Linkcraft plugin;

    public DBManager(Linkcraft plugin, String tablePrefix) {
        this.plugin = plugin;
        this.prefixProcessor = (s -> s.replace("{p}", tablePrefix));
    }

    public AsyncMySQL initializeTables() throws SQLException {
        this.mySQL = connectToDB();
        if(mySQL != null) {
            Bukkit.getConsoleSender().sendMessage("[LinkCraft] DB connected");
            boolean exists;
            try(Connection conn = mySQL.getMySQL().getConnection()) {
                exists = tableExists(conn, prefixProcessor.apply("{p}players"));
            }

            if(!exists) {
                createSchema();
            }

        } else {
            Bukkit.getConsoleSender().sendMessage("[LinkCraft] Cannot connect to DB");
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
            mySQL = new AsyncMySQL(plugin, host, port, username, password, database);
            return mySQL;
        } catch (SQLException | ClassNotFoundException throwables) { throwables.printStackTrace(); }

        return null;
    }

    private void createSchema() {
        List<String> statements;
        try (InputStream is = plugin.getResource("mysql.sql")) {

            statements = getStatements(is).stream().map(prefixProcessor).collect(Collectors.toList());
            mySQL.getMySQL().isConnected(true);
            for(String statement : statements) {
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

                if(!line.startsWith("--")) {
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

    private boolean tableExists(Connection conn, String table) throws SQLException {
        try(ResultSet rs = conn.getMetaData().getTables(null, null, "%", null)) {
            while (rs.next()) {
                if(rs.getString("TABLE_NAME").equals(table)) {
                    return true;
                }
            }
        }
        return false;
    }

}
