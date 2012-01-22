package com.md_5.district;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.bukkit.Location;

public final class RegionSearcher {

    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    Location location;
    public Future<Region> future;

    public RegionSearcher(Location location) {
        this.location = location;
        search();
    }

    public void search() {
        try {
            future = pool.submit(new Callable<Region>() {

                public Region call() {
                    final ArrayList<Region> regions = Util.getRegions(location);
                    if (regions == null || regions.isEmpty()) {
                        return null;
                    }
                    return regions.get(0);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
