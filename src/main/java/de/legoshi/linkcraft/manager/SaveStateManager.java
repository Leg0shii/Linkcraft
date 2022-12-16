package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.PlayThrough;
import de.legoshi.linkcraft.player.SaveState;
import de.legoshi.linkcraft.player.playertype.CoursePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveStateManager implements SaveableManager<SaveState, Integer> {

    @Inject private DBManager dbManager;
    @Inject private LocationManager locationManager;

    public SaveState createSaveState(CoursePlayer coursePlayer) {
        Location saveLocation = getSaveStateLocation(coursePlayer);
        long quitTime = System.currentTimeMillis();
        Date quitDate = new Date(quitTime);
        PlayThrough playThrough = coursePlayer.getPlayThrough();

        SaveState saveState = new SaveState(playThrough, saveLocation, quitDate);
        initObject(saveState);
        return saveState;
    }

    private Location getSaveStateLocation(AbstractPlayer aPlayer) {
        // is player in air?
        return aPlayer.getPlayer().getLocation();
    }

    @Override
    public int initObject(SaveState saveState) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_saves (play_through_id, quit_date, location_id) VALUES (?,?,?);";

        int ptID = saveState.getPlayThrough().getPtID();
        Date quitDate = saveState.getQuitDate();

        Location location = saveState.getSaveLocation();
        int locationID = locationManager.initObject(location);

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ptID);
            stmt.setDate(2, quitDate);
            stmt.setInt(3, locationID);
            stmt.execute();
            return dbManager.getAutoGenID(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateObject(SaveState location) {
        return false;
    }

    @Override
    public boolean deleteObject(Integer id) {
        return false;
    }

    @Override
    public SaveState requestObjectById(Integer id) {
        return null;
    }

}
