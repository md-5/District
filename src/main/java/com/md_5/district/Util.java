package com.md_5.district;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Util {

    @Deprecated
    private static ArrayList<Region> getCuboids(World w, int point_x, int point_y, int point_z) {
        String sql = ("SELECT name FROM ds_regions"
                + " WHERE `start_x` <= " + point_x + " AND `end_x` >= " + point_x
                + " AND `start_y` <= " + point_y + " AND `end_y` >= " + point_y
                + " AND `start_z` <= " + point_z + " AND `end_z` >= " + point_z
                + " AND `world` = '" + w.getName() + "';");
        HashMap<Integer, ArrayList<String>> result = Database.readRaw(sql);
        ArrayList<String> regionNames = new ArrayList<String>();
        for (ArrayList<String> s : result.values()) {
            regionNames.add(s.get(0));
        }
        ArrayList<Region> regions = new ArrayList<Region>();
        for (String s : regionNames) {
            regions.add(Loader.load(s));
        }
        return regions;
    }

    @Deprecated
    public static ArrayList<Region> getRegions(Location location) {
        return getCuboids(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    private static Region getRegion(final World w, final int point_x, final int point_y, final int point_z) {
        final String sql = ("SELECT name FROM ds_regions WHERE `start_x` <= ? AND `end_x` >= ? AND `start_y` <= ? AND `end_y` >= ? AND `start_z` <= ? AND `end_z` >= ? AND `world` = ? ;");
        final ArrayList<String> args = new ArrayList<String>();
        args.add(String.valueOf(point_x));
        args.add(String.valueOf(point_x));
        args.add(String.valueOf(point_y));
        args.add(String.valueOf(point_y));
        args.add(String.valueOf(point_z));
        args.add(String.valueOf(point_z));
        args.add(w.getName());
        final ArrayList<String> result = Database.read(sql, args).get(1);
        if (result == null) {
            return null;
        }
        return Loader.load(result.get(0));
    }

    public static Region getRegion(final Location location) {
        return getRegion(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static void outline(Player p, Region r) {
        World w = r.getWorld();
        int block = Config.outline;

        int minX = r.start_x;
        int minY = r.start_y;
        int minZ = r.start_z;
        int maxX = r.end_x;
        int maxY = r.end_y;
        int maxZ = r.end_z;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                setBlockClient(new Location(w, x, y, minZ), block, p);
                setBlockClient(new Location(w, x, y, maxZ), block, p);
            }
        }
        for (int y = minY; y <= maxY; ++y) {
            for (int z = minZ; z <= maxZ; ++z) {
                setBlockClient(new Location(w, minX, y, z), block, p);
                setBlockClient(new Location(w, maxX, y, z), block, p);
            }
        }
        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                setBlockClient(new Location(w, x, minY, z), block, p);
                setBlockClient(new Location(w, x, maxY, z), block, p);
            }
        }
        return;
    }

    public static void removeOutline(Player p, Region r) {
        World w = r.getWorld();

        int minX = r.start_x;
        int minY = r.start_y;
        int minZ = r.start_z;
        int maxX = r.end_x;
        int maxY = r.end_y;
        int maxZ = r.end_z;

        int block;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                block = w.getBlockTypeIdAt(x, y, minZ);
                setBlockClient(new Location(w, x, y, minZ), block, p);

                block = w.getBlockTypeIdAt(x, y, maxZ);
                setBlockClient(new Location(w, x, y, maxZ), block, p);
            }
        }
        for (int y = minY; y <= maxY; ++y) {
            for (int z = minZ; z <= maxZ; ++z) {
                block = w.getBlockTypeIdAt(minX, y, z);
                setBlockClient(new Location(w, minX, y, z), block, p);

                block = w.getBlockTypeIdAt(maxX, y, z);
                setBlockClient(new Location(w, maxX, y, z), block, p);
            }
        }
        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                block = w.getBlockTypeIdAt(x, minY, z);
                setBlockClient(new Location(w, x, minY, z), block, p);

                block = w.getBlockTypeIdAt(x, maxY, z);
                setBlockClient(new Location(w, x, maxY, z), block, p);
            }
        }
    }

    public static void timedOutline(final Player p, final Region r, int ticks) {
        outline(p, r);
        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(District.instance, new Runnable() {

            public void run() {
                removeOutline(p, r);
            }
        }, ticks);
    }

    public static void setBlockClient(Location loc, int b, Player p) {
        p.sendBlockChange(loc, b, (byte) 0);
    }

    public static int getTotalSize(String player) {
        int num = 0;
        for (Region r : Loader.byOwner(player)) {
            if (r.getOwner().equals(player)) {
                num += r.getSize();
            }
        }
        return num;
    }

    public static int getMaxSize(Player player) {
        if (player.hasPermission("district.infinite")) {
            return -1;
        }
        if (player.hasPermission("district.xlarge")) {
            return Config.xlarge;
        }
        if (player.hasPermission("district.large")) {
            return Config.large;
        }
        if (player.hasPermission("district.medium")) {
            return Config.medium;
        }
        if (player.hasPermission("district.small")) {
            return Config.small;
        }
        if (player.hasPermission("district.xsmall")) {
            return Config.xsmall;
        }
        return 0;
    }

    public static int getVolume(Location l1, Location l2) {
        Location min = getMin(l1, l2);
        Location max = getMax(l1, l2);
        int start_x = min.getBlockX();
        int start_y = min.getBlockY();
        int start_z = min.getBlockZ();
        int end_x = max.getBlockX();
        int end_y = max.getBlockY();
        int end_z = max.getBlockZ();

        return (end_x - start_x) * (end_y - start_y) * (end_z - start_z);
    }

    public static Location getMin(Location l1, Location l2) {
        return new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()));
    }

    public static Location getMax(Location l1, Location l2) {
        return new Location(l1.getWorld(), Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()), Math.max(l1.getBlockZ(), l2.getBlockZ()));
    }

    public static boolean isOverlapping(Location l1, Location l2) {
        Location min = getMin(l1, l2);
        Location max = getMax(l1, l2);
        ArrayList<Region> currentRegionSet = Util.getRegions(min);
        ArrayList<Region> currentRegionSet2 = Util.getRegions(max);
        if (!currentRegionSet.isEmpty() || !currentRegionSet2.isEmpty()) {
            return true;
        }
        return false;
    }

    public static int getTotalSize(Player player) {
        return getTotalSize(player.getName());
    }
}
