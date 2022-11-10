package de.legoshi.linkcraft.database;
import de.legoshi.linkcraft.util.Cooldown;
import de.legoshi.linkcraft.util.FileWriter;
import de.legoshi.linkcraft.util.LCConfig;
import de.legoshi.linkcraft.util.message.Messages;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class DBManager {

    private Plugin plugin;
    public AsyncMySQL mySQL;

    private Function<String, String> prefixProcessor;
    private final String COMMAND_COOLDOWNS_SELECT = "SELECT * FROM {p}command_cooldowns WHERE LOWER(command)=LOWER(?);";

    public DBManager(Plugin plugin) {
        this.plugin = plugin;
        this.prefixProcessor = (s -> s.replace("{p}", "lc_"));
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
        List<String> statements;
        try (InputStream is = plugin.getResource("mysql.sql")) {

            statements = getStatements(is).stream().map(prefixProcessor).collect(Collectors.toList());
            // mySQL.getMySQL().isConnected(true);
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

    // Not sure if we want this here, just putting it here atm
    public Cooldown getCooldownInfo(String commandName) {
        Cooldown cd = null;
        PreparedStatement stmt = mySQL.prepare(prefixProcessor.apply(COMMAND_COOLDOWNS_SELECT));
        try {
            stmt.setString(1, commandName);
            ResultSet rs = mySQL.query(stmt);

            if(rs.next()) {
                String message = rs.getString("message");
                if(message != null) {
                    // TODO: I don't like this code (should support custom messages not only from the Message enum)
                    // My guess would probably being storing the message as a string on the cooldown object, but I think it's ok not to support this for the time being
                    //Message msg = Message.fromString(message);
                    //cd = new Cooldown(rs.getInt("cooldown"), msg == null ? Message.CMD_COOLDOWN : msg);
                } else {
                    cd = new Cooldown(rs.getInt("cooldown"), Messages.CMD_COOLDOWN);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return cd;
    }

}
