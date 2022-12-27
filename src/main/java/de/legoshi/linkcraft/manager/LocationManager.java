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

    public boolean updateLocation(int locID, Location newLocation) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "UPDATE lc_locations SET world=?, x=?, y=?, z=?, yaw=?, pitch=? WHERE id=?;";

        String world = newLocation.getWorld().getName();
        double x = newLocation.getX();
        double y = newLocation.getY();
        double z = newLocation.getZ();
        float yaw = newLocation.getYaw();
        float pitch = newLocation.getPitch();

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, world);
            stmt.setDouble(2, x);
            stmt.setDouble(3, y);
            stmt.setDouble(4, z);
            stmt.setFloat(5, yaw);
            stmt.setFloat(6, pitch);
            stmt.setInt(7, locID);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean deleteObject(Integer id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "DELETE FROM lc_locations WHERE id=?;";
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
    public Location requestObjectById(Integer id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "SELECT * FROM lc_locations WHERE id = ?;";

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);

            ResultSet resultSet = stmt.executeQuery();
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
        // TODO: replace this with spawn
        return new Location(Bukkit.getWorld("world"), 0, 0, 0);
    }

}
