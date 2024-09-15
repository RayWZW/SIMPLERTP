package com.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RTPCommandTabCompleter implements TabCompleter {

    private final RANDOMTPPlugin plugin;

    public RTPCommandTabCompleter(RANDOMTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            if ("world".startsWith(args[0].toLowerCase())) {
                List<String> worlds = new ArrayList<>();
                for (World world : plugin.getServer().getWorlds()) {
                    worlds.add(world.getName());
                }
                return worlds;
            }
        }
        return new ArrayList<>();
    }
}