package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.tag.TagData;
import de.legoshi.linkcraft.tag.TagRarity;
import de.legoshi.linkcraft.tag.TagType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TagManager implements SaveableManager<PlayerTag, Integer> {

    @Inject private DBManager dbManager;
    @Inject private PlayerManager playerManager;

    @Override
    public boolean initObject(PlayerTag playerTag) {
        String sql = "INSERT INTO lc_tags (name, description, rarity, type) VALUES (?,?,?,?);";
        AsyncMySQL mySQL = dbManager.getMySQL();
        String name = playerTag.getDisplayName();
        String description = playerTag.getDescription();
        int rarity = TagRarity.getTagPosition(playerTag.getTagRarity());
        int type = TagType.getTagPosition(playerTag.getTagType());

        try(PreparedStatement stmt = mySQL.prepare(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, rarity);
            stmt.setInt(4, type);
            stmt.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean updateObject(PlayerTag playerTag) {
        String sql = """
        UPDATE lc_tags
        SET name=?,description=?,rarity=?,type=?
        WHERE tag_id=?;
        """;
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
        boolean result = false;
        ResultSet rs = dbManager.getMySQL().query("SELECT * FROM lc_tags WHERE tag_id=" + tagId + ";");
        try {
            result = rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean hasTag(String player, int tagId) {
        boolean result = false;
        ResultSet rs = dbManager.getMySQL().query("SELECT * FROM lc_player_tags t JOIN lc_players p ON t.user_id=p.user_id WHERE p.name='" + player + "' AND t.tag_id=" + tagId + ";");
        try {
            result = rs.next();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return result;
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


    public ArrayList<TagData> getTags(int rarity, Player player, int page, int pageVolume) {
        String sql = """
            SELECT collected.*
            FROM (
                SELECT t.tag_id, t.name, t.rarity, t.type, t.description, l.date, COUNT(lt.tag_id) AS 'owned_by'
                FROM lc_tags t JOIN lc_player_tags l ON t.tag_id=l.tag_id JOIN lc_player_tags lt on t.tag_id=lt.tag_id
                WHERE rarity=? AND l.user_id=?
                GROUP BY t.tag_id
                ORDER BY t.type, t.tag_id
            ) collected
            UNION
            SELECT uncollected.*
            FROM (
                SELECT t.tag_id, t.name, t.rarity, t.type, t.description, null as date, COUNT(l.tag_id) AS 'owned_by'
                FROM lc_tags t LEFT JOIN lc_player_tags l ON t.tag_id=l.tag_id
                WHERE t.rarity=? AND t.tag_id NOT IN (
                    SELECT tag_id FROM lc_player_tags
                    WHERE user_id=?
                )
                GROUP BY t.tag_id
                ORDER BY t.type, t.tag_id
            ) uncollected
            LIMIT ?, ?;
        """;
        int startPos = page * pageVolume;
        String uuid = player.getUniqueId().toString();
        ArrayList<TagData> tags = new ArrayList<>();

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, rarity);
            stmt.setString(2, uuid);
            stmt.setInt(3, rarity);
            stmt.setString(4, uuid);
            stmt.setInt(5, startPos);
            stmt.setInt(6, pageVolume);
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

    public ArrayList<TagData> getCollected(int rarity, Player player) {
        String sql = """
                    SELECT t.tag_id, t.name, t.rarity, t.type, t.description, l.date, COUNT(lt.tag_id) AS 'owned_by'
                    FROM lc_tags t JOIN lc_player_tags l ON t.tag_id=l.tag_id JOIN lc_player_tags lt ON t.tag_id=lt.tag_id
                    WHERE t.rarity=? AND l.user_id=?
                    GROUP BY t.tag_id
                    ORDER BY t.type, t.tag_id;
                """;
        ArrayList<TagData> collected = new ArrayList<>();

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, rarity);
            stmt.setString(2, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                collected.add(new TagData(rs.getInt("tag_id"),
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

        return collected;
    }

    public ArrayList<TagData> getUncollected(int rarity, Player player) {
        ArrayList<TagData> uncollected = new ArrayList<>();
        String sql = """
                    SELECT t.tag_id, t.name, t.rarity, t.type, t.description, COUNT(l.tag_id) AS 'owned_by'
                    FROM lc_tags t LEFT JOIN lc_player_tags l ON t.tag_id=l.tag_id
                    WHERE t.rarity=? AND t.tag_id NOT IN (
                    	SELECT tag_id FROM lc_player_tags
                        WHERE user_id=?
                    )
                    GROUP BY t.tag_id
                    ORDER BY t.type, t.tag_id;
                """;

        try(PreparedStatement stmt = dbManager.getMySQL().prepare(sql)) {
            stmt.setInt(1, rarity);
            stmt.setString(2, player.getUniqueId().toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                uncollected.add(new TagData(rs.getInt("tag_id"),
                        rs.getString("name"),
                        rs.getInt("rarity"),
                        rs.getInt("type"),
                        rs.getString("description"),
                        "",
                        rs.getInt("owned_by")));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return uncollected;
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
