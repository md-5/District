package com.md_5.district;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class Loader {

    public static void load(final District plugin) {
        // Get the stuff to loop
        File regionsRoot = new File(plugin.getDataFolder() + File.separator + "Regions");
        regionsRoot.mkdir();
        File[] regionFiles = regionsRoot.listFiles();
        // Loop through all files
        for (File f : regionFiles) {
            // Load the file
            CustomConfig configFile = new CustomConfig(plugin, f.getAbsolutePath());
            FileConfiguration config = configFile.getConfig();
            // Set and get the variables
            String name = config.getString("name");
            String w = config.getString("world");
            World world = Bukkit.getServer().getWorld(w);
            // Location point 1
            Vector v1 = config.getVector("start");
            Location l1 = new Location(world, v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
            // Location point 2
            Vector v2 = config.getVector("end");
            Location l2 = new Location(world, v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
            // Owner
            String owner = config.getString("owner");
            // Friends
            @SuppressWarnings("unchecked")
            List<String> members = config.getList("friends");
            if (members == null){
                System.out.println("null members");
                members = new ArrayList<String>();
            }
            // Construct the region
            Region r = new Region(world, l1, l2, owner, members, name);
            // Set extra stuff
            r.setGreeting(config.getString("greeting", ""));
            r.setFarewell(config.getString("farewell", ""));
            Regions.addRegion(r);
        }
    }

    public static void save(final District plugin, Region region) {
        // Initialise the config
        CustomConfig configFile = new CustomConfig(plugin, plugin.getDataFolder()
                + File.separator + "Regions" + File.separator + region.getName() + ".yml");
        FileConfiguration config = configFile.getConfig();
        // Save all the stuff
        config.set("name", region.getName());
        config.set("world", region.getWorld().getName());
        config.set("start", new Vector(region.getL1().getBlockX(), region.getL1().getBlockY(), region.getL1().getBlockZ()));
        config.set("end", new Vector(region.getL2().getBlockX(), region.getL2().getBlockY(), region.getL2().getBlockZ()));
        config.set("owner", region.getOwner());
        config.set("friends", region.getMembers());
        config.set("greeting", region.getGreeting());
        config.set("farewell", region.getFarewell());
        configFile.saveConfig();
    }

    public static void remove(final District plugin, Region region) {
        File file = new File(plugin.getDataFolder() + File.separator + "Regions" + File.separator + region.getName() + ".yml");
        file.delete();
    }
}
