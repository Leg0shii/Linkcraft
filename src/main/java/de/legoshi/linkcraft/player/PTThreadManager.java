package de.legoshi.linkcraft.player;

import de.legoshi.linkcraft.manager.PlayerManager;
import de.legoshi.linkcraft.manager.SaveStateManager;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PTThreadManager {

    @Inject private SaveStateManager saveStateManager;
    private final HashMap<Player, ScheduledExecutorService> hashMap = new HashMap<>();

    public void startPTThread(AbstractPlayer abstractPlayer) {
        Player player = abstractPlayer.getPlayer();
        stopPTThread(player);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> update(abstractPlayer), 0, 15, TimeUnit.SECONDS);
        hashMap.put(player, scheduler);
    }

    public void stopPTThread(Player player) {
        if (hashMap.containsKey(player)) {
            hashMap.get(player).shutdown();
            hashMap.remove(player);
            System.out.println("STOP THREAD");
        }
    }

    private void update(AbstractPlayer abstractPlayer) {
        saveStateManager.saveSaveState(abstractPlayer);
        abstractPlayer.getPlayThrough().startSession();
    }

}
