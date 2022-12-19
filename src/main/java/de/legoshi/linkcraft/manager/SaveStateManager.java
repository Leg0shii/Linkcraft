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
import java.sql.*;

public class SaveStateManager implements SaveableManager<SaveState, Integer> {

    @Inject private DBManager dbManager;
    @Inject private LocationManager locationManager;
    @Inject private PlayThroughManager playThroughManager;

    public void saveSaveState(AbstractPlayer abstractPlayer) {
        Location saveLocation = getSaveStateLocation(abstractPlayer);
        long quitTime = System.currentTimeMillis();
        Date quitDate = new Date(quitTime);
        PlayThrough playThrough = abstractPlayer.getPlayThrough();
        playThrough.updatePT();

        SaveState saveState = new SaveState(playThrough, saveLocation, quitDate);
        AsyncMySQL mySQL = dbManager.getMySQL();

        String sql = "SELECT id FROM lc_saves WHERE play_through_id = ?;";
        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, playThrough.getPtID());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) updateSaveState(abstractPlayer, saveState);
            else initObject(saveState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSaveState(AbstractPlayer abstractPlayer, SaveState saveState) {
        Location saveLocation = getSaveStateLocation(abstractPlayer);
        Date quitDate = new Date(System.currentTimeMillis());
        PlayThrough playThrough = abstractPlayer.getPlayThrough();

        saveState.setSaveLocation(saveLocation);
        saveState.setPlayThrough(playThrough);
        saveState.setQuitDate(quitDate);

        playThroughManager.updateObject(playThrough);
        updateObject(saveState);
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
        playThroughManager.updateObject(saveState.getPlayThrough());

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
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "SELECT * FROM lc_saves WHERE id = ?;";

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();

            if(resultSet.next()) {
                int ptID = resultSet.getInt("play_through_id");
                PlayThrough playThrough = playThroughManager.requestObjectById(ptID);

                int lID = resultSet.getInt("location_id");
                Location saveLocation = locationManager.requestObjectById(lID);
                Date quitDate = resultSet.getDate("quit_date");

                SaveState saveState = new SaveState(playThrough, saveLocation, quitDate);
                saveState.setSaveID(id);
                saveState.setSaveStateName(resultSet.getString("save_name"));
                saveState.setBlockTypeName(resultSet.getString("block_type_name"));

                return saveState;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SaveState requestObjectById(Integer id, Player player) {
        SaveState saveState = requestObjectById(id);
        saveState.getPlayThrough().setPlayer(player);
        saveState.getPlayThrough().startSession();
        return saveState;
    }

}
