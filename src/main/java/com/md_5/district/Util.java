package com.md_5.district;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Util {

    public static boolean isInCuboid(Location loc, Location l1, Location l2) {
        double x1 = l1.getX(), x2 = l2.getX(),
                y1 = l1.getY(), y2 = l2.getY(),
                z1 = l1.getZ(), z2 = l2.getZ(),
                pointx = loc.getX(),
                pointy = loc.getY(),
                pointz = loc.getZ();

        Location max = new Location(l1.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
        Location min = new Location(l1.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));

        return (pointx >= min.getX() && pointx <= max.getX() + 1
                && pointy >= min.getY() && pointy <= max.getY() && pointz >= min.getZ() && pointz <= max.getZ() + 1);
    }

    public static ArrayList<Region> getRegions(Location location) {
        // Select the exact region(s) they are in
        ArrayList<Region> currentRegionSet = new ArrayList<Region>();
        for (Region reg : Regions.getRegions()) {
            if (Util.isInCuboid(location, reg.getL1(), reg.getL2())) {
                currentRegionSet.add(reg);
            }
        }
        // Save resources
        if (currentRegionSet.isEmpty()) {
            return null;
        }
        return currentRegionSet;
    }

    public static void outline(Player p, Region r) {
        Location min = r.getMin();
        Location max = r.getMax();
        World w = min.getWorld();
        int block = Config.outline;

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

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
        Location min = r.getMin();
        Location max = r.getMax();
        World w = min.getWorld();

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

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
        return;
    }
    
    public static void timedOutline(final Player p, final Region r, 
            int ticks, District plugin) {
        outline(p, r);
        plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
            public void run() {
                removeOutline(p, r);
            }
        }, ticks);
    }

    public static void setBlockClient(Location loc, int b, Player p) {
        p.sendBlockChange(loc, b, (byte) 0);
    }

    public static int getTotalVolume(String player) {
        int num = 0;
        for (Region r : Regions.getRegions()) {
            if (r.getOwner().equals(player)) {
                num += r.getVolume();
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
    
    public static int getMaxVolume(Player player) {
        int maxSize = getMaxSize(player);
        if(maxSize == -1) {
            return -1;
        }
        else
        {
          return (int) Math.pow(getMaxSize(player), 3);
        }
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

        return (int) (end_x - start_x) * (end_y - start_y) * (end_z - start_z);
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

        int start_x = min.getBlockX();
        int start_y = min.getBlockY();
        int start_z = min.getBlockZ();
        int end_x = max.getBlockX();
        int end_y = max.getBlockY();
        int end_z = max.getBlockZ();

        ArrayList<Location> blocks = new ArrayList<Location>();

        for (int x = start_x; x <= end_x; x++) {
            for (int y = start_y; y <= end_y; y++) {
                for (int z = start_z; z <= end_z; z++) {
                    blocks.add(new Location(min.getWorld(), x, y, z));
                }
            }
        }
        for (Location loc : blocks) {
            ArrayList<Region> currentRegionSet = Util.getRegions(loc);
            if (currentRegionSet != null) {
                return true;
            }
        }
        return false;
    }

    public static int getTotalVolume(Player player) {
        return getTotalVolume(player.getName());
    }
}