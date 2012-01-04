package com.md_5.district;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static int xsmall, small, medium, large, xlarge;
    public static int wand, outline;
    public static String connectionString, prefix;

    public static void load(final District plugin) {
        // General config loading
        final FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();

        // Populate the variables
        xsmall = config.getInt("xsmall", 5);
        small = config.getInt("small", 7);
        medium = config.getInt("medium", 11);
        large = config.getInt("large", 15);
        xlarge = config.getInt("xlarge", 21);
        wand = config.getInt("wand", 286);
        outline = config.getInt("outline", 20);
        // Database
        connectionString = "jdbc:mysql://" + config.getString("mysql.host") + ":" + config.getInt("mysql.port")
                + "/" + config.getString("mysql.database") + "?user=" + config.getString("mysql.user")
                + "&password=" + config.getString("mysql.password");
        prefix = config.getString("mysql.prefix");
    }
}
