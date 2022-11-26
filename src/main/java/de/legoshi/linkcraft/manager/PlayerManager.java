package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.playertype.StandardPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PlayerManager implements SavableManager<AbstractPlayer, String> {

    @Inject private DBManager dbManager;
    @Inject private TagManager tagManager;
    @Getter private final HashMap<Player, AbstractPlayer> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public void playerJoin(Player player) {
        AbstractPlayer iPlayer;
        if (!player.hasPlayedBefore() || !isInDatabase(player)) {
            iPlayer = new StandardPlayer(player, new PlayerTag());
            initObject(iPlayer);
        } else {
            AbstractPlayer tempPlayer = requestObjectById(player.getUniqueId().toString());
            // determine state of player (maybe later)
            // currently I want the player to be at spawn on join
            // and create a save whenever he leaves to where he can return back
            iPlayer = new StandardPlayer(player, tempPlayer.getPlayerTag());
        }
        saveName(player);

        hashMap.put(player, iPlayer);
    }

    public void saveName(Player player) {
        String uniqueID = player.getUniqueId().toString();
        dbManager.getMySQL().update("UPDATE lc_players SET name='" + player.getName() + "' WHERE user_id='" + uniqueID + "';");
    }

    public void playerQuit(Player player) {
        AbstractPlayer iPlayer = hashMap.get(player);
        updateObject(iPlayer);
        hashMap.remove(player);
    }

    public AbstractPlayer getPlayer(Player player) {
        return hashMap.get(player);
    }

    @Override
    public boolean initObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();

        String uniqueID = abstractPlayer.getPlayer().getUniqueId().toString();
        String name = abstractPlayer.getPlayer().getDisplayName();
        mySQL.update("INSERT INTO lc_players (user_id, name) VALUES ('" + uniqueID + "', '" + name + "');");

        return true;
    }

    @Override
    public boolean updateObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uniqueId = abstractPlayer.getPlayer().getUniqueId().toString();
        int tagId = abstractPlayer.getPlayerTag().getTagID();
        mySQL.update("UPDATE lc_players SET tag_id = " + tagId + " WHERE user_id = '" + uniqueId + "';");
        return true;
    }

    @Override
    public boolean deleteObject(String id) {
        // delete everything player related in here
        // player_tags db
        // player saves
        // player permissions
        return true;
    }

    // this is implemented bad
    @Override
    public AbstractPlayer requestObjectById(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT tag_id FROM lc_players WHERE user_id ='" + id + "';");
        int tagId = 0;
        try {
            if (resultSet.next()) {
                tagId = resultSet.getInt("tag_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayerTag playerTag = new PlayerTag();
        if(tagId != 0) {
            playerTag = tagManager.requestObjectById(tagId);
        }
        StandardPlayer standardPlayer = new StandardPlayer(null, playerTag);
        return standardPlayer;
    }

    private boolean isInDatabase(Player player) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT user_id FROM lc_players WHERE user_id ='" + player.getUniqueId().toString() + "';");
        try {
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean playerExists(String player) {
        boolean result = false;
        ResultSet rs = dbManager.getMySQL().query("SELECT * FROM lc_players WHERE name='" + player + "';");
        try {
            result = rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String uuidByName(String name) {
        String uuid = "";
        ResultSet rs = dbManager.getMySQL().query("SELECT user_id FROM lc_players WHERE name='" + name + "';");

        try {
            if(rs.next()) {
                uuid = rs.getString("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    public int playerCount() {
        int count = 0;
        ResultSet rs = dbManager.getMySQL().query("SELECT COUNT(*) AS 'count' FROM lc_players;");

        try {
            if(rs.next()) {
                count = rs.getInt("count");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

}
