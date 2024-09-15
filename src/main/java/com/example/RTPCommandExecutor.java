package com.example;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RTPCommandExecutor implements CommandExecutor {

    private final RANDOMTPPlugin plugin;

    public RTPCommandExecutor(RANDOMTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2 && sender.hasPermission("randomtp.admin")) {

            Player target = Bukkit.getPlayer(args[0]);
            String worldName = args[1];
            if (target != null) {
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    teleportPlayer(target, world);
                } else {
                    sender.sendMessage("World not found.");
                }
            } else {
                sender.sendMessage("Player not found.");
            }
            return true;
        } else if (args.length == 1) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("randomtp.use")) {
                    player.sendMessage("You do not have permission to use this command.");
                    return true;
                }

                if (!plugin.canTeleport(player)) {
                    player.sendMessage("You must wait before teleporting again.");
                    return true;
                }

                String worldName = args[0];
                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    teleportPlayer(player, world);
                } else {
                    player.sendMessage("World not found.");
                }
                return true;
            }
        }
        return false;
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

                player.sendMessage("You have been teleported to coordinates: X=" + (int) x + " Y=" + (int) y + " Z=" + (int) z);

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