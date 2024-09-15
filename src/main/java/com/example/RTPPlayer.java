package com.example;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RTPPlayer implements CommandExecutor {

    private final RANDOMTPPlugin plugin;

    public RTPPlayer(RANDOMTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Usage: /rtp-player <player> <world>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayer(args[0]);
        if (targetPlayer == null) {
            sender.sendMessage("Player not found.");
            return true;
        }

        String worldName = args[1];
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            sender.sendMessage("World not found.");
            return true;
        }

        if (!sender.hasPermission("randomtp.admin")) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }

        teleportPlayer(targetPlayer, world);
        return true;
    }

    private void teleportPlayer(final Player player, final World world) {

        player.playSound(player.getLocation(), Sound.valueOf(plugin.getSoundBefore()), 1.0F, 1.0F);

        new BukkitRunnable() {
            @Override
            public void run() {
                double radius = plugin.getRadius();
                double x = (Math.random() * (2 * radius)) - radius;
                double z = (Math.random() * (2 * radius)) - radius;
                double y = world.getHighestBlockYAt((int) x, (int) z);

                player.teleport(new org.bukkit.Location(world, x, y, z));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.valueOf(plugin.getSoundAfter()), 1.0F, 1.0F);
                    }
                }.runTaskLater(plugin, 20L); 

                plugin.setLastTeleportTime(player);
            }
        }.runTaskLater(plugin, plugin.getDelay() * 20L); 
    }
}