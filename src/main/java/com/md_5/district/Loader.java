package com.md_5.district;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class Loader {

    private static HashMap<String, Region> cache = new HashMap<String, Region>();

    public static Region load(final String name) {
        //if (cache.containsKey(name)) {
        // System.out.println("cache");
        return loadFromCache(name);
        //} else {
        // return loadFromDisk(name);
        // }
    }

    private static Region loadFromDisk(final String name) {
        // World
        HashMap<Integer, ArrayList<String>> result = Database.Read("SELECT world FROM " + Config.prefix + "regions WHERE name='" + name + "'");
        if (result.get(1) == null) {
            return null;
        }
        String w = result.get(1).get(0);
        World world = Bukkit.getServer().getWorld(w);
        // Location point 1
        int start_x = Integer.parseInt(Database.Read("SELECT start_x FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        int start_y = Integer.parseInt(Database.Read("SELECT start_y FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        int start_z = Integer.parseInt(Database.Read("SELECT start_z FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        Vector v1 = new Vector(start_x, start_y, start_z);
        Location l1 = new Location(world, v1.getBlockX(), v1.getBlockY(), v1.getBlockZ());
        // Location point 2
        int end_x = Integer.parseInt(Database.Read("SELECT end_x FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        int end_y = Integer.parseInt(Database.Read("SELECT end_y FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        int end_z = Integer.parseInt(Database.Read("SELECT end_z FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0));
        Vector v2 = new Vector(end_x, end_y, end_z);
        Location l2 = new Location(world, v2.getBlockX(), v2.getBlockY(), v2.getBlockZ());
        // Owner
        String owner = Database.Read("SELECT owner FROM " + Config.prefix + "regions WHERE name='" + name + "'").get(1).get(0);
        ArrayList<String> friends = new ArrayList<String>();
        for (ArrayList<String> f : Database.Read("SELECT playerName FROM " + Config.prefix + "friends WHERE regionName='" + name + "'").values()) {
            friends.add(f.get(0));
        }
        // Construct the region
        final Region region = new Region(world, l1, l2, owner, friends, name);
        putCache(region);
        return region;
    }

    private static Region loadFromCache(final String name) {
        return cache.get(name);
    }

    private static void putCache(final Region region) {
        if (cache.size() > Config.cache) {
            cache.clear();
        }
        cache.put(region.getName(), region);
    }

    private static void delCache(final String region) {
        cache.remove(region);
    }

    public static void save(final Region r) {
        final String name = r.getName();
        remove(name);
        String sql = "INSERT INTO " + Config.prefix + "regions (`name`, `world`, `start_x`, `start_y`, `start_z`, `end_x`, `end_y`, `end_z`, `owner`) VALUES(?,?,?,?,?,?,?,?,?);";
        ArrayList<String> args = new ArrayList<String>();
        args.add(name);
        args.add(r.getWorld().getName());
        args.add(String.valueOf(r.start_x));
        args.add(String.valueOf(r.start_y));
        args.add(String.valueOf(r.start_z));
        args.add(String.valueOf(r.end_x));
        args.add(String.valueOf(r.end_y));
        args.add(String.valueOf(r.end_z));
        args.add(r.getOwner());
        Database.write(sql, args);
        for (String f : r.getMembers()) {
            final ArrayList<String> friends = new ArrayList<String>();
            friends.add(name);
            friends.add(f);
            Database.write("INSERT INTO " + Config.prefix + "friends VALUES(?,?);", friends);
        }
        putCache(r);
    }

    public static void remove(final String regionName) {
        ArrayList<String> args = new ArrayList<String>();
        args.add(regionName);
        Database.write("DELETE FROM " + Config.prefix + "regions WHERE name = ?;", args);
        Database.write("DELETE FROM " + Config.prefix + "friends WHERE regionName = ?;", args);
        delCache(regionName);
    }

    public static ArrayList<Region> byOwner(final String playerName) {
        ArrayList<Region> regions = new ArrayList<Region>();
        for (final ArrayList<String> name : Database.Read("SELECT name FROM " + Config.prefix + "regions WHERE owner='" + playerName + "'").values()) {
            regions.add(load(name.get(0)));
        }
        return regions;
    }

    public static ArrayList<String> listAll() {
        ArrayList<String> names = new ArrayList<String>();
        for (final ArrayList<String> name : Database.Read("SELECT name FROM " + Config.prefix + "regions").values()) {
            names.add(name.get(0));
        }
        return names;
    }

    public static void initCache() {
        for (final String name : listAll()) {
            putCache(loadFromDisk(name));
        }
    }
}
