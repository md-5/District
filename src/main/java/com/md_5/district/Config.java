package com.md_5.district;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static int xsmall, small, medium, large, xlarge;
    public static int wand, outline, cache;
    public static String connectionString;

    public static void load() {
        // General config loading
        final FileConfiguration config = District.instance.getConfig();
        config.options().copyDefaults(true);
        District.instance.saveConfig();
        // Populate the variables
        xsmall = config.getInt("xsmall", 5);
        small = config.getInt("small", 7);
        medium = config.getInt("medium", 11);
        large = config.getInt("large", 15);
        xlarge = config.getInt("xlarge", 21);
        wand = config.getInt("wand", 286);
        outline = config.getInt("outline", 20);
        cache = config.getInt("cache", 200);
        // Database
        connectionString = "jdbc:mysql://127.0.0.1:" + config.getInt("mysql.port")
                + "/" + config.getString("mysql.database") + "?user=" + config.getString("mysql.user")
                + "&password=" + config.getString("mysql.password");
    }
}
