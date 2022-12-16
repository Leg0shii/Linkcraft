package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.manager.PlayThroughManager;
import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SaveStateManager;
import de.legoshi.linkcraft.map.StandardMap;
import de.legoshi.linkcraft.tag.PlayerTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@AllArgsConstructor
public abstract class AbstractPlayer implements IPlayer {

    protected Player player;
    @Setter protected PlayerTag playerTag;
    @Setter protected PlayThroughManager playThroughManager;
    @Setter protected SaveStateManager saveStateManager;
    @Setter protected PlayThrough playThrough;

    public AbstractPlayer(Player player, PlayerTag playerTag) {
        this.player = player;
        this.playerTag = playerTag;
    }

    public String chat(String message) {
        String result = "";
        if(!playerTag.getDisplayName().isBlank()) {
            result += playerTag.getDisplayName() + " §r";
        }
        // TODO: Add bonus rank
        // TODO: Add rank
        // TODO: switch to coloured name once that feature is available
        // TODO: Add star
        result += player.getDisplayName() + "§r §7» §r";
        result += message;
        // Don't colour this as it allows players to freely colour their messages
        return ChatColor.translateAlternateColorCodes('&', result);
    }

    // this is temporary for tags exmaple
    public String sampleChat(String tag, String message) {
        String result = "";
        result += tag + " §r";
        // TODO: Add bonus rank
        // TODO: Add rank
        // TODO: switch to coloured name once that feature is available
        // TODO: Add star
        result += player.getDisplayName() + "§r §7» §r";
        result += message;
        // Don't colour this as it allows players to freely colour their messages
        return ChatColor.translateAlternateColorCodes('&', result);
    }

    @Override
    public void playerCPSignClick(String location) {

    }

    @Override
    public void playerEndSignClick() {

    }

    @Override
    public void playerTagSignClick() {

    }

    @Override
    public void playerTeleportSignClick() {

    }

    // Could make abstract...
    @Override
    public boolean canUseEffectBlocks() {
        return true;
    }

    @Override
    public void playerJoinMap(StandardMap map) {
        int mapId = map.getId();
        Location mapSpawn = map.getMapSpawn();
        // execute creation of new save state
        this.getPlayer().sendMessage("Selected map " + mapId);
        this.getPlayer().teleport(mapSpawn);
        this.playThrough = playThroughManager.createPlayThrough(player, mapId);
    }

    @Override
    public void playerLeaveMap() {

    }

}
