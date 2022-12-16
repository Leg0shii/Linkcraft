package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.player.PlayThrough;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayThroughManager implements SaveableManager<PlayThrough, Integer> {
    @Inject
    private DBManager dbManager;

    public PlayThrough createPlayThrough(Player player, int mapId) {
        PlayThrough playThrough = new PlayThrough(player, mapId);
        int id = initObject(playThrough);
        playThrough.setPtID(id);
        return playThrough;
    }

    @Override
    public int initObject(PlayThrough playThrough) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_play_through (map_id, user_id, time_played) VALUES (?,?,?);";

        int mapID = playThrough.getMapID();
        String userID = playThrough.getUserID();

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mapID);
            stmt.setString(2, userID);
            stmt.setLong(3, 0);
            stmt.executeUpdate();
            return dbManager.getAutoGenID(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateObject(PlayThrough playThrough) {
        return false;
    }

    @Override
    public boolean deleteObject(Integer id) {
        return false;
    }

    @Override
    public PlayThrough requestObjectById(Integer id) {
        return null;
    }
}
