package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.playertype.PracticePlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class PracticeManager {

    @Inject LocationManager locationManager;
    @Inject PlayerManager playerManager;
    @Inject private DBManager dbManager;

    private HashMap<Player, Location> locationCache = new HashMap<>();

    public void init(AbstractPlayer aPlayer) {
        Player player = aPlayer.getPlayer();
        Location location = getPracLocationDb(player);

        if(location != null) {
            addToCache(player, location);
            playerManager.updatePlayerState(aPlayer, PracticePlayer.class);
        }
    }

    public void back(Player player) {
        Location location = locationCache.get(player);
        if(location != null) player.teleport(location);

        // TODO: check if on playthrough and increment stats?
    }

    public int practice(Player player) {
        String sql = "INSERT INTO lc_practice (user_id, location_id) VALUES (?,?);";
        Location location = player.getLocation();
        int id = locationManager.initObject(location);
        String userId = player.getUniqueId().toString();

        addToCache(player, location);

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setString(1, userId);
            stmt.setInt(2, id);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public void unpractice(Player player) {

        int locationId = getLocationId(player);
        Location location = getPracLocation(player);

        if(locationId != -1) {
            // This will delete the related row in lc_practice due to on delete cascade
            locationManager.deleteObject(locationId);
        }

        if(location != null) {
            player.teleport(location);
        }

        removeFromCache(player);
    }

    private void addToCache(Player player, Location location) {
        locationCache.put(player, location);
    }

    private Location getPracLocation(Player player) {
        return locationCache.get(player);
    }

    private Location getPracLocationDb(Player player) {
        int id = getLocationId(player);
        return id != -1 ? locationManager.requestObjectById(id) : null;
    }

    private int getLocationId(Player player) {
        String sql = "SELECT location_id FROM lc_practice WHERE user_id=?";
        int id = -1;
        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                id = rs.getInt("location_id");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Call on player leave to save memory
    public void removeFromCache(Player player) {
        locationCache.remove(player);
    }
}
