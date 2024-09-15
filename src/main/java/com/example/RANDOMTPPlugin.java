package com.example;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RANDOMTPPlugin extends JavaPlugin {

    private FileConfiguration config;
    private final Map<Player, Long> lastTeleportTime = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();  
        config = getConfig(); 
        getCommand("rtp").setExecutor(new RTPCommandExecutor(this));
        getCommand("rtp-player").setExecutor(new RTPPlayer(this));
        getCommand("rtp").setTabCompleter(new RTPCommandTabCompleter(this));
    }

    public int getCooldown() {
        return config.getInt("cooldown", 10); 
    }

    public int getDelay() {
        return config.getInt("delay", 3); 
    }

    public String getSoundBefore() {
        return config.getString("sound1", "ENTITY_CREEPER_PRIMED"); 
    }

    public String getSoundAfter() {
        return config.getString("sound2", "DRAGON_FIREBALL_EXPLODE"); 
    }

    public double getRadius() {
        return config.getDouble("radius", 100); 
    }

    public boolean canTeleport(Player player) {
        long now = System.currentTimeMillis();
        long lastTeleport = lastTeleportTime.getOrDefault(player, 0L);
        long cooldownMillis = TimeUnit.SECONDS.toMillis(getCooldown());
        return (now - lastTeleport) >= cooldownMillis;
    }

    public void setLastTeleportTime(Player player) {
        lastTeleportTime.put(player, System.currentTimeMillis());
    }
}