package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.map.StandardMap;

import javax.inject.Inject;
import java.sql.ResultSet;

public class MapManager implements SavableManager<StandardMap> {

    @Inject private DBManager dbManager;

    @Override
    public void initObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String name = standardMap.getMapName();
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        double difficulty = standardMap.getDifficulty();
        String builders = standardMap.getBuilderNames();
        mySQL.update("INSERT INTO lc_maps (name, type, difficulty, builder_names) VALUES " +
                "('" + name + "', '" + mapType + "', '" + difficulty + "', '" + builders + "');");
    }

    @Override
    public void updateObject(StandardMap standardMap) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        int mapId = standardMap.getId();
        String name = standardMap.getMapName();
        int mapType = MapType.getMapPosition(standardMap.getMapType());
        double difficulty = standardMap.getDifficulty();
        String builders = standardMap.getBuilderNames();
        mySQL.update("INSERT INTO lc_maps (name, type, difficulty, builder_names) VALUES " +
                "('" + name + "', '" + mapType + "', '" + difficulty + "', '" + builders + "' WHERE id = " + mapId + ");");
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
                MapType mapType = MapType.values()[resultSet.getInt("type")];
                double difficulty = resultSet.getInt("difficulty");
                String builders = resultSet.getString("builder_names");
                String releaseDate = resultSet.getString("release_date");
                return new StandardMap(mapId, name, mapType, difficulty, builders, releaseDate);
            } else {
                // map doesnt exist
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new StandardMap();
    }

}
