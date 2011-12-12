package com.md_5.district;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CustomConfig {

    @SuppressWarnings("unused")
    private final District plugin;
    private FileConfiguration customConfig = null;
    private File customConfigurationFile = null;

    public CustomConfig(final District plugin, String fileName) {
        this.plugin = plugin;
        this.customConfigurationFile = new File(fileName);
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            loadConfig();
        }
        return customConfig;
    }

    public void loadConfig() {
        customConfig = YamlConfiguration.loadConfiguration(customConfigurationFile);
    }

    public void saveConfig() {
        try {
            customConfig.save(customConfigurationFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
