package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.PlayThrough;
import de.legoshi.linkcraft.player.SaveState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.*;

public class SaveStateManager implements SaveableManager<SaveState, Integer> {

    @Inject private DBManager dbManager;
    @Inject private LocationManager locationManager;
    @Inject private PlayThroughManager playThroughManager;

    /* TODO:
        - save stats every x amount of time because of crash
        - dont save location, this will be memorized by server (if we save it as well, reset for players is very annoying)
     */

    public void saveSaveState(AbstractPlayer abstractPlayer) {
        System.out.println("SAVE SAVE-STATE RAN");
        Location saveLocation = getSaveStateLocation(abstractPlayer);
        long quitTime = System.currentTimeMillis();
        Date quitDate = new Date(quitTime);
        PlayThrough playThrough = abstractPlayer.getPlayThrough();
        playThrough.updatePT();

        SaveState saveState = new SaveState(playThrough, saveLocation, quitDate);
        AsyncMySQL mySQL = dbManager.getMySQL();

        String sql = "SELECT id, location_id FROM lc_saves WHERE play_through_id = ?;";
        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, playThrough.getPtID());
            ResultSet resultSet = stmt.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt("id");
                int locID = resultSet.getInt("location_id");
                saveState.setSaveID(id);
                saveState.setLocationID(locID);
                updateSaveState(saveState);
            }
            else initObject(saveState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSaveState(SaveState saveState) {
        PlayThrough playThrough = saveState.getPlayThrough();
        playThroughManager.updateObject(playThrough);
        updateObject(saveState);
    }

    public void setLoaded(AbstractPlayer abstractPlayer, boolean value) {
        if (abstractPlayer.getPlayThrough() == null) return;
        int ptID = abstractPlayer.getPlayThrough().getPtID();
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_saves SET loaded=? WHERE play_through_id=?;";

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setBoolean(1, value);
            stmt.setInt(2, ptID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*public void setLoadedAllFalse(Player player) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_saves as s, lc_play_through as p SET s.loaded=false WHERE s.play_through_id=p.id AND p.user_id=?;";
        String playerID = player.getUniqueId().toString();

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, playerID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    public SaveState getLoadedSaveStates(AbstractPlayer abstractPlayer) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "SELECT s.id FROM lc_saves as s, lc_play_through as p WHERE s.play_through_id=p.id AND s.loaded=1 AND p.user_id=?;";
        String userID = abstractPlayer.getPlayer().getUniqueId().toString();

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, userID);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                int sID = resultSet.getInt("s.id");
                return requestObjectById(sID, abstractPlayer.getPlayer());
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
    public boolean updateObject(SaveState saveState) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_saves SET quit_date=? WHERE id=?;";
        locationManager.updateLocation(saveState.getLocationID(), saveState.getSaveLocation());
        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, saveState.getQuitDate());
            stmt.setInt(2, saveState.getSaveID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteObject(Integer id) {
        return false;
    }

    @Deprecated
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
                saveState.setLocationID(lID);
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
