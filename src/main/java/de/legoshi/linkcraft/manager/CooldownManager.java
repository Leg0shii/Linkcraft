package de.legoshi.linkcraft.manager;

import de.legoshi.linkcraft.database.DBManager;
import de.legoshi.linkcraft.util.Cooldown;
import de.legoshi.linkcraft.util.message.Prefix;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

public class CooldownManager {

    @Inject private DBManager databaseService;

    // TODO: Possibly switch to config value/ect
    private final long GLOBAL_COOLDOWN = 1000;

    private final Map<Player, Cooldown> globalCooldowns = new HashMap<>();
    private final Map<Player, Map<String, Cooldown>> cooldowns = new HashMap<>();

    public boolean hasCooldown(Player player, String commandName) {
        if(containsPlayer(player)) {
            Map<String, Cooldown> pCds = cooldowns.get(player);
            if(pCds.get(commandName.toLowerCase()) != null) {
                return !pCds.get(commandName.toLowerCase()).isCooledDown();
            }
        }
        return false;
    }

    public boolean hasGlobalCooldown(Player player) {
        if(globalCooldowns.containsKey(player)) {
            return !globalCooldowns.get(player).isCooledDown();
        }

        return false;
    }

    public void addGlobalCooldown(Player player) {
        globalCooldowns.put(player, new Cooldown(GLOBAL_COOLDOWN));
    }

    public boolean isGlobalShorter(Player player, String commandName) {
        return getGlobalCooldown(player).timeLeft() < getCooldown(player, commandName).timeLeft();
    }

    public boolean hasBothCooldowns(Player player, String commandName) {
        return hasCooldown(player, commandName) && hasGlobalCooldown(player);
    }

    public Cooldown getCooldown(Player player, String commandName) {
        return cooldowns.get(player).get(commandName.toLowerCase());
    }

    public Cooldown getGlobalCooldown(Player player) {
        return globalCooldowns.get(player);
    }

    public void addCooldown(Player player, String commandName) {
        if(!hasCooldown(player, commandName)) {
            if (!containsPlayer(player)) {
                cooldowns.put(player, new HashMap<>());
            }

            Cooldown cooldown = databaseService.getCooldownInfo(commandName.toLowerCase());
            if(cooldown != null) {
                cooldowns.get(player).put(commandName.toLowerCase(), cooldown);
            }
        }

    }

    private boolean containsPlayer(Player player) {
        return cooldowns.containsKey(player);
    }

    public String getMessage(Cooldown cooldown) {
        return cooldown.getMessage().msg(Prefix.INFO, Long.toString(cooldown.timeLeft()));
    }
}
