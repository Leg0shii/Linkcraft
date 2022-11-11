package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.AsyncMySQL;
import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.database.SavableManager;
import de.legoshi.linkcraft.player.AbstractPlayer;
import de.legoshi.linkcraft.player.IPlayer;
import de.legoshi.linkcraft.player.playertype.StandardPlayer;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.Getter;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;

public class PlayerManager implements SavableManager<AbstractPlayer> {

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
        AsyncMySQL mySQL = dbManager.getMySQL();
        String uniqueID = abstractPlayer.getPlayer().getUniqueId().toString();
        mySQL.update("INSERT INTO lc_players (player_id) VALUES ('" + uniqueID + "');");
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
