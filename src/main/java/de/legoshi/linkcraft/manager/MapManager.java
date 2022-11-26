package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.map.StandardMap;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MapManager implements SavableManager<StandardMap, String> {

    @Inject private DBManager dbManager;

    @Override
    public boolean initObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_maps (name, type, difficulty, length, builder_names, spawn_location_id) VALUES (?,?,?,?,?,?);";

        String name = standardMap.getMapName();
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        int lengthType = MapLength.getMapLengthPosition(standardMap.getMapLength());
        double difficulty = standardMap.getDifficulty();
        int spawnLocId = standardMap.getLocationId();
        String builders = standardMap.getBuilderNames();

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, mapType);
            stmt.setDouble(3, difficulty);
            stmt.setInt(4, lengthType);
            stmt.setString(5, builders);
            stmt.setInt(6, spawnLocId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updateObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_maps SET name=?, type=?, difficulty=?, length=?, builder_names=?, spawn_location_id=? WHERE id=?;";
        int mapId = standardMap.getId();
        String name = standardMap.getMapName();
        // String releaseDate = standardMap.getReleaseDate(); , release_date='"+releaseDate+"'
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        int lengthType = MapLength.getMapLengthPosition(standardMap.getMapLength());
        int spawnLocId = standardMap.getLocationId();
        double difficulty = standardMap.getDifficulty();
        String builders = standardMap.getBuilderNames();

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, mapType);
            stmt.setDouble(3, difficulty);
            stmt.setInt(4, lengthType);
            stmt.setString(5, builders);
            stmt.setInt(6, spawnLocId);
            stmt.setInt(7, mapId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteObject(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String delMapsSQL = "DELETE FROM lc_maps WHERE id=?;";
        String delCompsSQL = "DELETE FROM lc_map_completions WHERE id=?;";

        try(PreparedStatement stmt = mySQL.prepare(delMapsSQL)) {
            stmt.setString(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try(PreparedStatement stmt = mySQL.prepare(delCompsSQL)) {
            stmt.setString(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            // intentionally removed the return here because the map itself got deleted
        }

        return true;
    }

    @Override
    public StandardMap requestObjectById(String id) {
        try(PreparedStatement stmt = dbManager.getMySQL().prepare("SELECT * FROM lc_maps WHERE id=?;")) {
            stmt.setString(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                StandardMap standardMap = new StandardMap();
                standardMap.setMapName(resultSet.getString("name"));
                standardMap.setMapType(resultSet.getString("type"));
                standardMap.setMapLength(resultSet.getString("length"));
                standardMap.setDifficulty(resultSet.getString("difficulty"));
                standardMap.setBuilderNames(resultSet.getString("builder_names"));
                standardMap.setReleaseDate(resultSet.getString("release_date"));
                standardMap.setLocationId(resultSet.getString("spawn_location_id"));
                standardMap.setId(Integer.parseInt(id));
                return standardMap;
            } else {
                // map doesnt exist
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return new StandardMap();
    }

    public boolean addVictor(int id, String uuid) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_map_completions SET map_id=?, user_id=?, completion=true;";
        return executeSQL(id, uuid, mySQL, sql);
    }

    public boolean removeVictor(int id, String uuid) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "DELETE FROM lc_map_completions WHERE map_id=? AND user_id=? AND completion=true;";
        return executeSQL(id, uuid, mySQL, sql);
    }

    public boolean updateStartLocation(int mapId, int locationId) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_maps SET spawn_location_id=? WHERE id=?;";
        return executeSQL(mapId, ""+locationId, mySQL, sql);
    }

    private boolean executeSQL(int id, String uuid, AsyncMySQL mySQL, String sql) {
        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, uuid);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
