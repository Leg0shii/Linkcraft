package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.map.StandardMap;
import org.bukkit.Location;

import javax.inject.Inject;
import java.sql.ResultSet;

public class MapManager implements SavableManager<StandardMap> {

    @Inject private DBManager dbManager;

    @Override
    public void initObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String name = standardMap.getMapName();
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        int lengthType = MapLength.getMapLengthPosition(standardMap.getMapLength());
        double difficulty = standardMap.getDifficulty();
        int spawnLocId = standardMap.getLocationId();
        String builders = standardMap.getBuilderNames();
        mySQL.update("INSERT INTO lc_maps (name, type, difficulty, length, builder_names, spawn_location_id) VALUES " +
                "('" + name + "', '" + mapType + "', '" + difficulty + "', '" + lengthType + "', '" + builders + "', '" + spawnLocId + "');");
    }

    @Override
    public void updateObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        int mapId = standardMap.getId();
        String name = standardMap.getMapName();
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        int lengthType = MapLength.getMapLengthPosition(standardMap.getMapLength());
        int spawnLocId = standardMap.getLocationId();
        double difficulty = standardMap.getDifficulty();
        String builders = standardMap.getBuilderNames();
        mySQL.update("INSERT INTO lc_maps (name, type, difficulty, length, builder_names, spawn_location_id) VALUES " +
                "('" + name + "', '" + mapType + "', '" + difficulty + "', '" + lengthType + "', '" + builders + "', '" + spawnLocId +
                "' WHERE id = " + mapId + ");");
    }

    @Override
    public void deleteObject(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        mySQL.update("DELETE FROM lc_maps WHERE id = " + id + ");");
        mySQL.update("DELETE FROM lc_map_completions WHERE map_id = " + id + ");");
    }

    @Override
    public StandardMap requestObjectById(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT FROM lc_maps WHERE id = " + id + ";");
        try {
            if (resultSet.next()) {
                int mapId = Integer.getInteger(id);
                String name = resultSet.getString("name");
                int mapType = resultSet.getInt("type");
                int mapLength = resultSet.getInt("length");
                double difficulty = resultSet.getInt("difficulty");
                String builders = resultSet.getString("builder_names");
                String releaseDate = resultSet.getString("release_date");
                return new StandardMap(name, mapType, mapLength, difficulty, builders, releaseDate);
            } else {
                // map doesnt exist
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StandardMap();
    }

    public void addVictor(int id, String uuid) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        mySQL.query("INSERT INTO lc_map_completions SET map_id = " + id +", user_id = '" + uuid + "', map_completion = true;");
    }

    public void removeVictor(int id, String uuid) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        mySQL.query("DELETE FROM lc_map_completions WHERE map_id = " + id + " AND user_id = '" + uuid + "' AND map_completion = true;");
    }

}
