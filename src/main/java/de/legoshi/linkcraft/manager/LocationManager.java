package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import javax.inject.Inject;
import java.sql.ResultSet;

public class LocationManager implements SavableManager<Location, String> {

    @Inject private DBManager dbManager;

    @Override
    public void initObject(Location location) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        mySQL.update("INSERT INTO lc_locations (world, x, y, z, yaw, pitch) VALUES " +
                "('" + location.getWorld().getName() + "', '" + location.getX() + "', '" + location.getY() + "', '"
                + location.getZ() + "', '" + location.getYaw() + "', '" + location.getPitch() + "');");
    }

    @Override
    public void updateObject(Location location) {

    }

    @Override
    public void deleteObject(String id) {

    }

    @Override
    public Location requestObjectById(String id) {
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

    public int requestIdByPos(double x, double y, double z) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT id FROM lc_locations WHERE x = " + x + " AND y = " + y + " AND z = " + z + ";");
        try {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
