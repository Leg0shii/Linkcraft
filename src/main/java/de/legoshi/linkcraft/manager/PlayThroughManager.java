package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.player.PlayThrough;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.*;

public class PlayThroughManager implements SaveableManager<PlayThrough, Integer> {

    @Inject private DBManager dbManager;
    @Inject private MapManager mapManager;

    public PlayThrough createPlayThrough(Player player, int mapId) {
        StandardMap map = mapManager.requestObjectById(mapId);
        PlayThrough playThrough = new PlayThrough(player, map);
        int id = initObject(playThrough);
        playThrough.setPtID(id);
        return playThrough;
    }

    @Override
    public int initObject(PlayThrough playThrough) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_play_through (map_id, user_id) VALUES (?,?);";

        int mapID = playThrough.getMap().getId();
        String userID = playThrough.getUserID();

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mapID);
            stmt.setString(2, userID);
            stmt.executeUpdate();
            return dbManager.getAutoGenID(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateObject(PlayThrough playThrough) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_play_through SET completion=?, completion_date=?, prac_usage=?, join_date=?, " +
                "time_played_normal=?, time_played_prac=?, current_jumps=? WHERE id=?;";

        int ptID = playThrough.getPtID();
        boolean completion = playThrough.isCompletion();
        Date completionDate = playThrough.getCompletionDate();
        int pracUsage = playThrough.getPracUsages();
        Date joinDate = playThrough.getJoinDate();
        long timePlayedNormal = playThrough.getTimePlayedNormal();
        long timePlayedPrac = playThrough.getTimePlayedPrac();
        int currentJumps = playThrough.getCurrentJumps();

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setBoolean(1, completion);
            stmt.setDate(2, completionDate);
            stmt.setInt(3, pracUsage);
            stmt.setDate(4, joinDate);
            stmt.setLong(5, timePlayedNormal);
            stmt.setLong(6, timePlayedPrac);
            stmt.setInt(7, currentJumps);
            stmt.setInt(8, ptID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteObject(Integer id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "DELETE FROM lc_play_through WHERE id=?;";
        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public PlayThrough requestObjectById(Integer id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "SELECT * FROM lc_play_through WHERE id = ?;";

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                PlayThrough playThrough = new PlayThrough();
                StandardMap map = mapManager.requestObjectById(resultSet.getInt("map_id"));
                playThrough.setPtID(id);
                playThrough.setTimePlayedNormal(resultSet.getLong("time_played_normal"));
                playThrough.setMap(map);
                playThrough.setTimePlayedPrac(resultSet.getLong("time_played_prac"));
                playThrough.setCompletion(resultSet.getBoolean("completion"));
                playThrough.setCompletionDate(resultSet.getDate("completion_date"));
                playThrough.setCurrentJumps(resultSet.getInt("current_jumps"));
                playThrough.setJoinDate(resultSet.getDate("join_date"));
                playThrough.setPracUsages(resultSet.getInt("prac_usage"));
                playThrough.setUserID(resultSet.getString("user_id"));
                return playThrough;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PlayThrough requestObjectById(Integer id, Player player) {
        PlayThrough playThrough = requestObjectById(id);
        playThrough.setPlayer(player);
        playThrough.startSession();
        return playThrough;
    }
}
