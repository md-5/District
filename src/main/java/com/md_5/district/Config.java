package com.md_5.district;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static int xsmallSize, smallSize, mediumSize, largeSize, xlargeSize;
    public static int xsmallVolume, smallVolume, mediumVolume, largeVolume, xlargeVolume;
    public static int wand, outline;

    public static void load(final District plugin) {
        // General config loading
        final FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();

        // Populate the variables
        xsmallSize = config.getInt("sizes.xsmall", 5);
        smallSize = config.getInt("sizes.small", 7);
        mediumSize = config.getInt("sizes.medium", 11);
        largeSize = config.getInt("sizes.large", 15);
        xlargeSize = config.getInt("sizes.xlarge", 21);
        
        xsmallVolume = config.getInt("volumes.xsmall", 125); 
        smallVolume = config.getInt("volumes.small", 343);
        mediumVolume = config.getInt("volumes.medium", 1331);
        largeVolume = config.getInt("volumes.large", 11390625);
        xlargeVolume = config.getInt("volumes.xlarge", 85766121);
                
        wand = config.getInt("wand", 286);
        outline = config.getInt("outline", 20);
    }
}
