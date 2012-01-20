package com.md_5.district.flatfile;

import java.util.Collection;
import java.util.HashMap;

public class Regions {

    private static HashMap<String, Region> regions = new HashMap<String, Region>();

    public static Collection<Region> getRegions() {
        return regions.values();
    }

    public static void addRegion(Region region) {
        regions.put(region.getName(), region);
    }

    public static Region getRegion(String which) {
        return regions.get(which);
    }

    public static void removeRegion(String region) {
        regions.remove(region);
    }

    private class Region {

        protected String getName() {
            return null;
        }
    }
}
