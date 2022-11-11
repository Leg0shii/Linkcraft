package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.util.Cooldown;
import de.legoshi.linkcraft.util.message.MessageUtils;
import de.legoshi.linkcraft.util.message.Messages;
import de.legoshi.linkcraft.util.message.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    @Inject private DBManager dbManager;

    // TODO: Possibly switch to config value/ect
    private final long GLOBAL_COOLDOWN = 1000;

    private final Map<Player, Cooldown> globalCooldowns = new HashMap<>();
    private final Map<Player, Map<String, Cooldown>> cooldowns = new HashMap<>();

    public boolean hasCooldown(Player player, String commandName) {
        if(containsPlayer(player)) {
            Map<String, Cooldown> pCds = cooldowns.get(player);
            if(pCds.get(commandName.toLowerCase()) != null) {
                return !pCds.get(commandName.toLowerCase()).isCooledDown();
            }
        }
        return false;
    }

    public boolean hasGlobalCooldown(Player player) {
        if(globalCooldowns.containsKey(player)) {
            return !globalCooldowns.get(player).isCooledDown();
        }

        return false;
    }

    public void addGlobalCooldown(Player player) {
        globalCooldowns.put(player, new Cooldown(GLOBAL_COOLDOWN));
    }

    public boolean isGlobalShorter(Player player, String commandName) {
        return getGlobalCooldown(player).timeLeft() < getCooldown(player, commandName).timeLeft();
    }

    public boolean hasBothCooldowns(Player player, String commandName) {
        return hasCooldown(player, commandName) && hasGlobalCooldown(player);
    }

    public Cooldown getCooldown(Player player, String commandName) {
        return cooldowns.get(player).get(commandName.toLowerCase());
    }

    public Cooldown getGlobalCooldown(Player player) {
        return globalCooldowns.get(player);
    }

    public void addCooldown(Player player, String commandName) {
        if(!hasCooldown(player, commandName)) {
            if (!containsPlayer(player)) {
                cooldowns.put(player, new HashMap<>());
            }

            Cooldown cooldown = getCooldownInfo(commandName.toLowerCase());
            if(cooldown != null) {
                cooldowns.get(player).put(commandName.toLowerCase(), cooldown);
            }
        }

    }

    private boolean containsPlayer(Player player) {
        return cooldowns.containsKey(player);
    }

    public String getMessage(Cooldown cooldown) {
        return MessageUtils.composeMessage(cooldown.getMessage(), true, Long.toString(cooldown.timeLeft()));
    }

    public Cooldown getCooldownInfo(String commandName) {
        Cooldown cd = null;
        AsyncMySQL mySQL = dbManager.getMySQL();
        PreparedStatement stmt = mySQL.prepare(dbManager.getPrefixProcessor().apply(dbManager.getCOMMAND_COOLDOWNS_SELECT()));
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
