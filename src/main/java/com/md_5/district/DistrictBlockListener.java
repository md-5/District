package com.md_5.district;

import static com.md_5.district.Executor.handle;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class DistrictBlockListener implements Listener {

    public DistrictBlockListener(final District plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        handle(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        handle(event, event.getBlock(), event.getPlayer());
    }
}
