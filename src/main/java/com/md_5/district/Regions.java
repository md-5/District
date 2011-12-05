package com.md_5.district;

import java.util.HashMap;

public class Regions {

    private static HashMap<String, Region> regions = new HashMap<String, Region>();

    public static HashMap<String, Region> getRegions() {
        return regions;
    }

    public static void addRegion(Region region) {
        regions.put(region.getName(), region);
    }

   public static Region getRegion(String which){
       return regions.get(which);
   }
   public static void removeRegion(String region){
       regions.remove(region);
   }
}
