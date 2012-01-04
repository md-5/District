package com.md_5.district;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Loader {

    private static final District plugin = District.instance;

    public static void load() {
        HashMap<Integer, ArrayList<String>> regions = plugin.db.Read("SELECT name FROM " + Config.prefix + "regions");
        for (ArrayList<String> set : regions.values()) {
            // Name
            String name = set.get(0);
            // World
            String w = plugin.db.Read("SELECT world FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0);
            World world = Bukkit.getServer().getWorld(w);
            // Location point 1
            int start_x = Integer.parseInt(plugin.db.Read("SELECT start_x FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            int start_y = Integer.parseInt(plugin.db.Read("SELECT start_y FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            int start_z = Integer.parseInt(plugin.db.Read("SELECT start_z FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            Vector v1 = new Vector(start_x, start_y, start_z);
            Location l1 = new Location(world, v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
            // Location point 2
            int end_x = Integer.parseInt(plugin.db.Read("SELECT end_x FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            int end_y = Integer.parseInt(plugin.db.Read("SELECT end_y FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            int end_z = Integer.parseInt(plugin.db.Read("SELECT end_z FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
            Vector v2 = new Vector(end_x, end_y, end_z);
            Location l2 = new Location(world, v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
            // Owner
            String owner = plugin.db.Read("SELECT owner FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0);
            // Construct the region
            Region r = new Region(world, l1, l2, owner, new ArrayList<String>(), name);
            // And register it
            Regions.addRegion(r);
        }
    }

    public static void save(Region r) {
    }

    public static void remove(Region r) {
    }
}
