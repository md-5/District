package com.md_5.district.flatfile;

import com.md_5.district.District;
import com.md_5.district.Loader;
import com.md_5.district.Region;
import java.io.File;
import java.util.ArrayList;
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
            ArrayList<String> members = (ArrayList<String>) config.getStringList("friends");
            if (members == null) {
                members = new ArrayList<String>();
            }
            // Construct the region
            Region r = new Region(world, l1, l2, owner, members, name);
            // Set extra stuff
            regions.add(r);
        }
        for (Region r : regions) {
            Loader.save(r);
        }
    }
}
