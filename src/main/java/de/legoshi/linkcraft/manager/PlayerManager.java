package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.*;
import de.legoshi.linkcraft.player.playertype.*;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class PlayerManager implements SaveableManager<AbstractPlayer, String> {

    @Inject private DBManager dbManager;
    @Inject private TagManager tagManager;
    @Inject private SaveStateManager saveStateManager;
    @Inject private PlayThroughManager playThroughManager;
    @Getter private final HashMap<Player, AbstractPlayer> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public void playerJoin(Player player) {
        AbstractPlayer abstractPlayer;
        if (!player.hasPlayedBefore() || !playerExists(player.getName())) {
            abstractPlayer = new SpawnPlayer(player, new PlayerTag());
            initObject(abstractPlayer);
        } else {
            String uuid = player.getUniqueId().toString();
            abstractPlayer = requestObjectById(uuid, player);
            // determine state of player (maybe later)
            // currently I want the player to be at spawn on join
            // and create a save whenever he leaves to where he can return back
        }

        abstractPlayer.setPlayThroughManager(playThroughManager);
        abstractPlayer.setSaveStateManager(saveStateManager);
        saveName(player);

        hashMap.put(player, abstractPlayer);
    }

    public void playerJoinMap(Player player, StandardMap map) {
        AbstractPlayer abstractPlayer = hashMap.get(player);
        if(abstractPlayer.isPlayingCourse()) {
            playerLeaveMap(player);
        }

        PlayThrough playThrough = playThroughManager.createPlayThrough(player, map.getId());
        abstractPlayer.setPlayThrough(playThrough);
        player.teleport(map.getMapSpawn());

        switch (map.getMapType()) {
            case MAZE:
                updatePlayerState(abstractPlayer, MazePlayer.class);
                break;
            case SEGMENTED:
                updatePlayerState(abstractPlayer, SegmentedPlayer.class);
                break;
            case RANK_UP: case BONUS:
                updatePlayerState(abstractPlayer, RankUpPlayer.class);
                break;
        }
    }

    public void playerJoinSaveState(Player player, SaveState saveState) {
        AbstractPlayer abstractPlayer = hashMap.get(player);
        if(abstractPlayer.isPlayingCourse()) {
            playerLeaveMap(player);
        }

        abstractPlayer.setPlayThrough(saveState.getPlayThrough());
        player.teleport(saveState.getSaveLocation());

        switch (saveState.getPlayThrough().getMap().getMapType()) {
            case MAZE:
                updatePlayerState(abstractPlayer, MazePlayer.class);
                break;
            case SEGMENTED:
                updatePlayerState(abstractPlayer, SegmentedPlayer.class);
                break;
            case RANK_UP:
                updatePlayerState(abstractPlayer, RankUpPlayer.class);
                break;
        }
    }

    public void playerLeaveMap(Player player) {
        AbstractPlayer abstractPlayer = hashMap.get(player);
        if (abstractPlayer.getPlayThrough() == null) return;
        saveStateManager.saveSaveState(abstractPlayer);
        updatePlayerState(abstractPlayer, SpawnPlayer.class);
    }

    public void updatePlayerState(AbstractPlayer abstractPlayer, Class<?> clazz) {
        AbstractPlayer newPlayer = PlayerFactory.getPlayerByType(abstractPlayer, clazz);
        hashMap.remove(abstractPlayer.getPlayer());
        hashMap.put(newPlayer.getPlayer(), newPlayer);
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
    public int initObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();

        String uniqueID = abstractPlayer.getPlayer().getUniqueId().toString();
        String name = abstractPlayer.getPlayer().getDisplayName();

        String sql = "INSERT INTO lc_players (user_id, name) VALUES (?,?);";
        try (PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, uniqueID);
            stmt.setString(2, name);
            stmt.execute();
            return dbManager.getAutoGenID(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean updateObject(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uniqueId = abstractPlayer.getPlayer().getUniqueId().toString();
        int tagId = abstractPlayer.getPlayerTag().getTagID();

        String sql = "UPDATE lc_players SET tag_id = ? WHERE user_id = ?;";
        try (PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, tagId);
            stmt.setString(2, uniqueId);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteObject(String id) {
        // delete everything player related in here
        // player_tags db
        // player saves
        // play_through
        // player permissions
        return true;
    }

    @Deprecated
    @Override
    public AbstractPlayer requestObjectById(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();

        String sql = "SELECT tag_id FROM lc_players WHERE user_id =?;";
        try (PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            PlayerTag playerTag = new PlayerTag();
            if (resultSet.next()) {
                int tagId = resultSet.getInt("tag_id");
                playerTag = tagManager.requestObjectById(tagId);
            }
            return new SpawnPlayer(null, playerTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AbstractPlayer requestObjectById(String id, Player player) {
        AbstractPlayer abstractPlayer = requestObjectById(id);
        abstractPlayer.setPlayer(player);
        return abstractPlayer;
    }

    public boolean playerExists(String player) {
        AsyncMySQL mySQL = dbManager.getMySQL();

        String sql = "SELECT * FROM lc_players WHERE user_id = ?;";
        String uuid = uuidByName(player);

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, uuid);
            ResultSet result = stmt.executeQuery();
            return result.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String uuidByName(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        if (offlinePlayer != null) { // is only null if player didn't play before
            return offlinePlayer.getUniqueId().toString();
        }
        return "";
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
