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
import java.util.HashMap;

public class PlayerManager implements SavableManager<AbstractPlayer> {

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

        hashMap.put(player, iPlayer);
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
    public void initObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uniqueID = abstractPlayer.getPlayer().getUniqueId().toString();
        mySQL.update("INSERT INTO lc_players (user_id) VALUES ('" + uniqueID + "');");
    }

    @Override
    public void updateObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uniqueId = abstractPlayer.getPlayer().getUniqueId().toString();
        String tagId = abstractPlayer.getPlayerTag().getTagID();
        mySQL.update("UPDATE lc_players SET tag_id = " + tagId + " WHERE user_id = '" + uniqueId + "';");
    }

    @Override
    public void deleteObject(String id) {
        // delete everything player related in here
        // player_tags db
        // player saves
        // player permissions
    }

    // this is implemented bad
    @Override
    public AbstractPlayer requestObjectById(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT tag_id FROM lc_players WHERE user_id ='" + id + "';");
        String tagId = "1";
        try {
            if (resultSet.next()) {
                tagId = "" + resultSet.getInt("tag_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayerTag playerTag = tagManager.requestObjectById(tagId);
        return new StandardPlayer(null, playerTag);
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

}
