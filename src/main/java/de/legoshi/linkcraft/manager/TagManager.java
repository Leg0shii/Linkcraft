package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.gui.tag.TagHolder;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.*;
import de.legoshi.linkcraft.util.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class TagManager implements SaveableManager<PlayerTag, Integer> {

    @Inject private DBManager dbManager;
    @Inject private PlayerManager playerManager;

    @Override
    public int initObject(PlayerTag playerTag) {
        String sql = "INSERT INTO lc_tags (name, description, rarity, type) VALUES (?,?,?,?);";
        AsyncMySQL mySQL = dbManager.getMySQL();
        String name = playerTag.getDisplayName();
        String description = playerTag.getDescription();
        int rarity = TagRarity.getTagPosition(playerTag.getTagRarity());
        int type = TagType.getTagPosition(playerTag.getTagType());

        try(PreparedStatement stmt = mySQL.prepare(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, rarity);
            stmt.setInt(4, type);
            stmt.execute();
            return dbManager.getAutoGenID(stmt);
        } catch(SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateObject(PlayerTag playerTag) {
        String sql = "UPDATE lc_tags " +
                     "SET name=?,description=?,rarity=?,type=? " +
                     "WHERE tag_id=?;";

        AsyncMySQL mySQL = dbManager.getMySQL();
        int tagId = playerTag.getTagID();
        String name = playerTag.getDisplayName();
        String description = playerTag.getDescription();
        int rarity = playerTag.getTagRarity().ordinal();
        int type = playerTag.getTagType().ordinal();

        PreparedStatement stmt = mySQL.prepare(sql);
        try {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, rarity);
            stmt.setInt(4, type);
            stmt.setInt(5, tagId);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (AbstractPlayer abstractPlayer : playerManager.getHashMap().values()) {
            if (abstractPlayer.getPlayerTag().getTagID() == tagId) {
                abstractPlayer.setPlayerTag(playerTag);
            }
        }

        return true;
    }

    @Override
    public boolean deleteObject(Integer id) {
        if (!tagExists(id)) return false;

        AsyncMySQL mySQL = dbManager.getMySQL();
        String sqlTags = "DELETE FROM lc_tags WHERE tag_id=?;";
        String sqlPTags = "DELETE FROM lc_player_tags WHERE tag_id=?;";
        String sqlP = "UPDATE lc_players SET tag_id=0 WHERE tag_id=?;";

        // only the first delete matters, as this deletes the tag
        if (!executeSQL(id, mySQL, sqlTags)) return false;
        executeSQL(id, mySQL, sqlPTags);
        executeSQL(id, mySQL, sqlP);

        // issue with offline players
        // or are we loading tags on join? forgor...
        for (AbstractPlayer abstractPlayer : playerManager.getHashMap().values()) {
            if (abstractPlayer.getPlayerTag().getTagID() == id) {
                abstractPlayer.setPlayerTag(new PlayerTag());
            }
        }
        return true;
    }

    @Override
    public PlayerTag requestObjectById(Integer id) {
        PlayerTag playerTag = new PlayerTag();
        AsyncMySQL mySQL = dbManager.getMySQL();
        try(PreparedStatement stmt = mySQL.prepare("SELECT * FROM lc_tags WHERE tag_id=?;")) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                playerTag.setTagID(id);
                playerTag.setDisplayName(resultSet.getString("name"));
                playerTag.setDescription(resultSet.getString("description"));
                playerTag.setTagRarity(TagRarity.values()[resultSet.getInt("rarity")]);
                playerTag.setTagType(TagType.values()[resultSet.getInt("type")]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerTag;
    }

    public boolean tagExists(int tagId) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String sql = "SELECT * FROM lc_tags WHERE tag_id = ?;";
        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, tagId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasTag(String player, int tagId) {
        String sql = "SELECT * FROM lc_player_tags WHERE user_id=? AND tag_id=?;";
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uuid = playerManager.uuidByName(player);

        try (PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, uuid);
            stmt.setInt(2, tagId);
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean hasTag(Player player, int tagId) {
        String playerName = player.getName();
        return hasTag(playerName, tagId);
    }

    public boolean hasTag(Player player) {
        boolean result = false;
        try(PreparedStatement stmt = dbManager.getMySQL().prepare("SELECT * FROM lc_players WHERE user_id=? AND tag_id!=0;")) {
            stmt.setString(1, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            result = rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean giveTag(String player, int tagId) {
        String uuid = playerManager.uuidByName(player);
        PreparedStatement stmt = dbManager.getMySQL().prepare("INSERT INTO lc_player_tags (user_id, tag_id) VALUES (?, ?);");
        try {
            stmt.setString(1, uuid);
            stmt.setInt(2, tagId);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean removeTag(String name, int tagId) {
        String uuid = playerManager.uuidByName(name);

        Player player = Bukkit.getPlayer(name);
        if(player != null) {
            AbstractPlayer aPlayer = playerManager.getPlayer(player);
            if (aPlayer.getPlayerTag().getTagID() == tagId) {
                setDefaultTag(name);
            }
        }

        removeTag(tagId, uuid);

        return true;
    }

    public boolean removeTag(int tagId, String uuid) {
        try(PreparedStatement stmt = dbManager.getMySQL().prepare("DELETE FROM lc_player_tags WHERE user_id=? AND tag_id=?;")) {
            stmt.setString(1, uuid);
            stmt.setInt(2, tagId);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int removeAllTags(String name) {
        String uuid = playerManager.uuidByName(name);
        setDefaultTag(name);

        int amount = 0;
        PreparedStatement stmt = dbManager.getMySQL().prepare("DELETE FROM lc_player_tags WHERE user_id=?");
        try {
            stmt.setString(1, uuid);
            amount = stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return amount;
    }

    public boolean setTag(String name, int tagId) {
        Player player = Bukkit.getPlayer(name);
        boolean success;
        if(player != null) success = setTag(player, tagId);
        else success = setTagDb(playerManager.uuidByName(name), tagId);
        return success;
    }

    public boolean setTag(Player player, int tagId) {
        boolean success = setTagDb(player.getUniqueId().toString(), tagId);
        playerManager.getPlayer(player).setPlayerTag(requestObjectById(tagId));
        return success;
    }

    private boolean setTagDb(String uuid, int tagId) {
        try(PreparedStatement stmt = dbManager.getMySQL().prepare("UPDATE lc_players SET tag_id=? WHERE user_id=?;")) {
            stmt.setInt(1, tagId);
            stmt.setString(2, uuid);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean setDefaultTag(String name) {
        return setTag(name, 0);
    }

    public boolean setDefaultTag(Player player) {
        return setTag(player, 0);
    }


    private String getTagsSql(String collected, String uncollected, String limit, TagHolder.FilterState state) {
        switch (state) {
            case COLLECTED:
                return collected + limit;
            case UNCOLLECTED:
                return uncollected + limit;
            default:
                return "SELECT collected.* " +
                        "FROM (" +
                        collected + ") collected " +
                        "UNION " +
                        "SELECT uncollected.* " +
                        "FROM (" +
                        uncollected + ") uncollected " + limit;
        }
    }

    private String collectedTagsSql(int type) {
        String sql = "SELECT t.tag_id, t.name, t.rarity, t.type, t.description, l.date, COUNT(lt.tag_id) AS 'owned_by' " +
                     "FROM lc_tags t JOIN lc_player_tags l ON t.tag_id=l.tag_id JOIN lc_player_tags lt on t.tag_id=lt.tag_id " +
                     "WHERE {} l.user_id=? " +
                     "GROUP BY t.tag_id " +
                     "ORDER BY t.rarity DESC, t.tag_id ";

        return type != 0 ? CommonsUtils.format(sql, "{}", "t.type=? AND ") : CommonsUtils.format(sql, "{}", "");
    }

    private String uncollectedTagsSql(int type) {
        String sql = "SELECT t.tag_id, t.name, t.rarity, t.type, t.description, null as date, COUNT(l.tag_id) AS 'owned_by' " +
                     "FROM lc_tags t LEFT JOIN lc_player_tags l ON t.tag_id=l.tag_id " +
                     "WHERE {} t.type != 0 AND t.rarity != 0 AND t.tag_id NOT IN (" +
                        "SELECT tag_id FROM lc_player_tags " +
                        "WHERE user_id=?" +
                     ") " +
                     "GROUP BY t.tag_id " +
                     "ORDER BY t.rarity DESC, t.tag_id ";

        return type != 0 ? CommonsUtils.format(sql, "{}", "t.type=? AND ") : CommonsUtils.format(sql, "{}", "");
    }

    public ArrayList<TagData> getTags(Player player, int type, TagHolder.FilterState state, int page, int pageVolume) {
        int startPos = page * pageVolume;
        String uuid = player.getUniqueId().toString();
        ArrayList<TagData> tags = new ArrayList<>();
        String limit = "LIMIT ?, ?";

        String collected = collectedTagsSql(type);
        String uncollected = uncollectedTagsSql(type);
        String sql = getTagsSql(collected, uncollected, limit, state);

        // Should probably separate into different methods instead of this mess, but system will change in the future anyway...
        int i = 0;
        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            if(type != 0) {
                stmt.setInt(1, type);
            } else {
                i = 1;
            }
            stmt.setString(2 - i, uuid);

            if(state == TagHolder.FilterState.ALL) {
                if(type != 0) {
                    stmt.setInt(3, type);
                } else {
                    i = 2;
                }
                stmt.setString(4 - i, uuid);
            } else if(type != 0) {
                i = 2;
            } else {
                i = 3;
            }

            stmt.setInt(5 - i, startPos);
            stmt.setInt(6 - i, pageVolume);

            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                tags.add(new TagData(rs.getInt("tag_id"),
                        rs.getString("name"),
                        rs.getInt("rarity"),
                        rs.getInt("type"),
                        rs.getString("description"),
                        rs.getString("date"),
                        rs.getInt("owned_by")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    // This is a potential use case for saving player names. This would allow to sort by players that have a cached profile
    // Since if we transfer over tag data via UUID this method will return null Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
    public ArrayList<TagOwnedData> getOwnedBy(int tagId, int page, int pageVolume) {
        String sql = "SELECT user_id, date " +
                "FROM lc_player_tags " +
                "WHERE tag_id=? " +
                "ORDER BY date " +
                "LIMIT ?, ?;";
        int startPos = page * pageVolume;
        ArrayList<TagOwnedData> ownedBy = new ArrayList<>();


        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, tagId);
            stmt.setInt(2, startPos);
            stmt.setInt(3, pageVolume);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ownedBy.add(new TagOwnedData(rs.getString("user_id"),
                        rs.getString("date")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return ownedBy;
    }

    public HashMap<TagType, Integer> tagCounts() {
        String sql = "SELECT COUNT(*) AS 'count' " +
                     "FROM lc_tags " +
                     "WHERE rarity != 5 AND rarity != 0 AND type != 0 " +
                     "GROUP BY type " +
                     "ORDER BY type";

        HashMap<TagType, Integer> counts = new HashMap<>();

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            ResultSet rs = stmt.executeQuery();

            int i = 1;
            while(rs.next()) {
                counts.put(TagType.values()[i], rs.getInt("count"));
                i++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }

    public HashMap<TagType, Integer> tagCountPlayer(Player player) {

        String sql = "SELECT COUNT(*) AS 'tag_count_player' " +
                "FROM lc_player_tags p JOIN lc_tags t ON p.tag_id=t.tag_id " +
                "WHERE p.user_id=? AND t.type=?";

        HashMap<TagType, Integer> counts = new HashMap<>();
        String uuid = player.getUniqueId().toString();

        for(int i = 1; i < TagType.values().length; i++) {
            try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
                stmt.setString(1, uuid);
                stmt.setInt(2, i);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    counts.put(TagType.values()[i], rs.getInt("tag_count_player"));
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

        return counts;
    }

    private boolean executeSQL(int id, AsyncMySQL mySQL, String sql) {
        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setInt(1, id);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



}
