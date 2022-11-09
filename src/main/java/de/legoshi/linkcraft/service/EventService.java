package de.legoshi.linkcraft.service;

import de.legoshi.linkcraft.listener.ChatListener;
import de.legoshi.linkcraft.listener.CommandListener;
import de.legoshi.linkcraft.listener.JoinListener;
import de.legoshi.linkcraft.listener.QuitListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

public class EventService implements Service {

    @Inject private Plugin plugin;

    @Inject private JoinListener joinListener;
    @Inject private QuitListener quitListener;
    @Inject private ChatListener chatListener;
    @Inject private CommandListener commandListener;

    @Override
    public void start() {
        System.out.println("Event service started.");
        registerEvents(
                joinListener,
                quitListener,
                chatListener,
                commandListener
        );
    }

    private void registerEvents(Listener... events) {
        for (Listener listener : events) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

    @Override
    public void stop() {

    }

}
