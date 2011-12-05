package com.md_5.district;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class ChunkGrid {

    private Set<Chunk> chunkGrid = new HashSet<Chunk>();
    private Region region;

    public ChunkGrid(Location l1, Location l2, Region r) {
        World w = l1.getWorld();
        region = r;
        chunkGrid.add(w.getChunkAt(l1));
        chunkGrid.add(w.getChunkAt(l2));
        Location cuboid1 = new Location(l1.getWorld(), Math.max(l1.getX(), l2.getX()), Math.max(l1.getY(), l2.getY()), Math.max(l1.getZ(), l2.getZ()));
        Location cuboid2 = new Location(l1.getWorld(), Math.min(l1.getX(), l2.getX()), Math.min(l1.getY(), l2.getY()), Math.min(l1.getZ(), l2.getZ()));
        double y = l1.getY();
        cuboid2.subtract(16, 0, 16);
        cuboid1.add(16, 0, 16);
        for (double z = cuboid2.getZ(); z <= cuboid1.getZ(); z += 16) {
            for (double x = cuboid2.getX(); x <= cuboid1.getX(); x += 16) {
                chunkGrid.add(w.getChunkAt(new Location(w, x, y, z)));
            }
        }

    }

    public Region getRegion() {
        return this.region;
    }

    public boolean isInGrid(Chunk c) {
        return getChunkGrid().contains(c);
    }

    public Set<Chunk> getChunkGrid() {
        return chunkGrid;
    }
}
