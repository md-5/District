package com.md_5.district;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Loader {

    private static final District plugin = District.instance;

    public static Region load(String name) {
        // World
        HashMap<Integer, ArrayList<String>> result = plugin.db.Read("SELECT world FROM " + Config.prefix + "regions WHERE name='" + name + "'");
        if (result.get(1) == null){
            return null;
        }
        String w = result.get(1).get(0);
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
        ArrayList<String> friends = new ArrayList<String>();
        for (ArrayList<String> f : plugin.db.Read("SELECT playerName FROM " + Config.prefix + "friends WHERE regionName='" + name + "'").values()) {
            friends.add(f.get(0));
        }
        // Construct the region
        return new Region(world, l1, l2, owner, friends, name);
    }

    public static void save(Region r) {
        remove(r);
        String sql = "INSERT INTO " + Config.prefix + "regions VALUES ('"
                + r.getName() + "', '"
                + r.getWorld().getName() + "', '"
                + r.start_x + "', '" + r.start_y + "', '" + r.start_z + "', '" + r.end_x + "', '" + r.end_y + "', '" + r.end_z + "', '" + r.getOwner() + "');";
        plugin.db.write(sql);
        for (String f : r.getMembers()) {
            plugin.db.write("INSERT INTO " + Config.prefix + "friends VALUES ('" + r.getName() + "', '" + f + "');");
        }
    }

    public static void remove(Region r) {
        String name = r.getName();
        plugin.db.write("DELETE FROM " + Config.prefix + "regions WHERE name='" + name + "';");
        plugin.db.write("DELETE FROM " + Config.prefix + "friends WHERE regionName='" + name + "';");
    }

    public static ArrayList<Region> byPlayer(String p) {
        ArrayList<Region> r = new ArrayList<Region>();
        for (ArrayList<String> f : plugin.db.Read("SELECT name FROM " + Config.prefix + "regions WHERE owner='" + p + "'").values()) {
            r.add(load(f.get(0)));
        }
        return r;
    }

    public static ArrayList<Region> loadAll() {
        ArrayList<Region> r = new ArrayList<Region>();
        for (ArrayList<String> f : plugin.db.Read("SELECT name FROM " + Config.prefix + "regions").values()) {
            r.add(load(f.get(0)));
        }
        return r;
    }
}
