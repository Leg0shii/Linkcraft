package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.map.MapLength;
import de.legoshi.linkcraft.map.MapType;
import de.legoshi.linkcraft.map.StandardMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocationManager implements SaveableManager<Location, Integer> {

    @Inject private DBManager dbManager;
    @Inject private MapManager mapManager;

    @Override
    public int initObject(Location location) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "INSERT INTO lc_locations (world, x, y, z, yaw, pitch) VALUES (?,?,?,?,?,?);";

        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, world);
            stmt.setDouble(2, x);
            stmt.setDouble(3, y);
            stmt.setDouble(4, z);
            stmt.setFloat(5, yaw);
            stmt.setFloat(6, pitch);
            stmt.execute();
            return dbManager.getAutoGenID(stmt);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // doesn't work for locations so can be ignored
    // rather use updateLocation
    @Override
    public boolean updateObject(Location location) {
        return true;
    }

    // rather save id in object (map) as well, makes this better
    public boolean updateLocation(Location oldLocation, Location newLocation) {
        // update x, y, z, yaw, pitch, world based
        return true;
    }

    @Override
    public boolean deleteObject(Integer id) {
        return true;
    }

    @Override
    public Location requestObjectById(Integer id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT * FROM lc_locations WHERE id = " + id + ";");
        try {
            if (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                float yaw = resultSet.getFloat("yaw");
                float pitch = resultSet.getFloat("pitch");
                World world = Bukkit.getWorld(resultSet.getString("world"));
                return new Location(world, x, y, z, yaw, pitch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // replace this with spawn
        return new Location(Bukkit.getWorld("world"), 0, 0, 0);
    }

}
