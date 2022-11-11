package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SaveableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.IPlayer;
import de.legoshi.linkcraft.player.playertype.StandardPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

public class PlayerManager implements SaveableManager<AbstractPlayer> {

    @Inject private DBManager dbManager;
    @Getter private final HashMap<Player, AbstractPlayer> hashMap;

    public PlayerManager() {
        this.hashMap = new HashMap<>();
    }

    public void playerJoin(Player player) {
        // issue: player might have joined before
        AbstractPlayer iPlayer;
        if (!player.hasPlayedBefore()) {
            iPlayer = new StandardPlayer(player, new PlayerTag());
        } else {
            // load tag from database
            // determine state of player
            iPlayer = new StandardPlayer(player, new PlayerTag());
        }

        hashMap.put(player, iPlayer);
    }

    public void playerQuit(Player player) {
        IPlayer iPlayer = hashMap.get(player);
        hashMap.remove(player);
    }

    public AbstractPlayer getPlayer(Player player) {
        return hashMap.get(player);
    }

    @Override
    public void initObject(AbstractPlayer abstractPlayer) {
        HashMap<String, Object> tupleList = abstractPlayer.getKeyValueList();
        String keys = "";
        String values = "";
        for (String key : tupleList.keySet()) {
            keys = keys.equals("") ? key : keys + ", " + key;
            String value = (String) tupleList.get(key);
            values = values.equals("") ? value : values + ", " + value;
        }
        dbManager.mySQL.query("INSERT INTO lc_player (" + keys + ") VALUES (" + values + ");");
    }

    @Override
    public void saveObject(AbstractPlayer abstractPlayer) {

    }

    @Override
    public void updateObject(AbstractPlayer abstractPlayer) {

    }

    @Override
    public AbstractPlayer requestObject(String where) {
        return null;
    }
}
