package com.md_5.district;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.util.Vector;

public class OldLoader {

    public static void load(final District plugin) {
        // Get the stuff to loop
        File regionsRoot = new File(plugin.getDataFolder() + File.separator + "Regions");
        regionsRoot.mkdir();
        File[] regionFiles = regionsRoot.listFiles();
        ArrayList<Region> regions = new ArrayList<Region>();
        // Loop through all files
        for (File f : regionFiles) {
            // Load the file
            CustomConfig configFile = new CustomConfig(f.getAbsolutePath());
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
            List<String> members = config.getStringList("friends");
            if (members == null) {
                System.out.println("null members");
                members = new ArrayList<String>();
            }
            // Construct the region
            Region r = new Region(world, l1, l2, owner, members, name);
            // Set extra stuff
            r.setGreeting(config.getString("greeting", ""));
            r.setFarewell(config.getString("farewell", ""));
            regions.add(r);
        }
        for (Region r : regions){
            Loader.save(r);
        }
    }
}
