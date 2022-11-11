package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import de.legoshi.linkcraft.tag.TagRarity;

import javax.inject.Inject;
import java.sql.ResultSet;

public class TagManager implements SavableManager<PlayerTag> {

    @Inject private DBManager dbManager;
    @Inject private PlayerManager playerManager;

    @Override
    public void initObject(PlayerTag playerTag) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String name = playerTag.getDisplayName();
        String description = playerTag.getDescription();
        int rarity = TagRarity.getTagPosition(playerTag.getTagRarity());
        mySQL.update("INSERT INTO lc_tags (name, description, rarity) VALUES ('" + name + "', '" + description + "', '" + rarity + "');");
    }

    @Override
    public void updateObject(PlayerTag playerTag) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        String tagId = playerTag.getTagID();
        String name = playerTag.getDisplayName();
        String description = playerTag.getDescription();
        String rarity = playerTag.getTagRarity().toString();
        mySQL.update("UPDATE lc_players SET name = '" + name + "', description = '" + description + "', rarity = '" + rarity + "' WHERE tag_id = '" + tagId + "';");
        for (AbstractPlayer abstractPlayer : playerManager.getHashMap().values()) {
            if (abstractPlayer.getPlayerTag().getTagID().equals(tagId)) {
                abstractPlayer.setPlayerTag(playerTag);
            }
        }
    }

    @Override
    public void deleteObject(String id) {
        AsyncMySQL mySQL = dbManager.getMySQL();
        mySQL.update("DELETE FROM lc_tags WHERE tag_id = " + id + ";");
        mySQL.update("DELETE FROM lc_player_tags WHERE tag_id = " + id + ";");
        for (AbstractPlayer abstractPlayer : playerManager.getHashMap().values()) {
            if (abstractPlayer.getPlayerTag().getTagID().equals(id)) {
                abstractPlayer.setPlayerTag(new PlayerTag());
            }
        }
    }

    @Override
    public PlayerTag requestObjectById(String id) {
        PlayerTag playerTag = new PlayerTag();
        AsyncMySQL mySQL = dbManager.getMySQL();
        ResultSet resultSet = mySQL.query("SELECT * FROM lc_tags WHERE tag_id = " + id + ";");
        try {
            if (resultSet.next()) {
                playerTag.setTagID(id);
                playerTag.setDisplayName(resultSet.getString("name"));
                playerTag.setDescription(resultSet.getString("description"));
                playerTag.setTagRarity(TagRarity.valueOf(resultSet.getString("rarity")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerTag;
    }

}
