package com.md_5.district;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DistrictBlockListener extends BlockListener {

    @SuppressWarnings("unused")
    private final District plugin;

    public DistrictBlockListener(final District plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvent(Type.BLOCK_BREAK, this, Priority.Normal, plugin);
        Bukkit.getServer().getPluginManager().registerEvent(Type.BLOCK_PLACE, this, Priority.Normal, plugin);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        // Event details
        Player eventPlayer = event.getPlayer();
        Location eventLocation = event.getBlock().getLocation();
        
        // Regions the block is in
        ArrayList<Region> currentRegionSet = Util.getRegions(eventLocation);
        // Save resources
        if (currentRegionSet == null) {
            return;
        }
        // Check if they are denied from placing in ANY region he block is in
        for (Region r : currentRegionSet) {
            if (!r.canUse(eventPlayer)) {
                r.sendDeny(eventPlayer);
                event.setCancelled(true);
                return;
            }
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        // Event details
        Player eventPlayer = event.getPlayer();
        Location eventLocation = event.getBlock().getLocation();
        
        // Regions the block is in
        ArrayList<Region> currentRegionSet = Util.getRegions(eventLocation);
        // Save resources
        if (currentRegionSet == null) {
            return;
        }
        // Check if they are denied from placing in ANY region he block is in
        for (Region r : currentRegionSet) {
            if (!r.canUse(eventPlayer)) {
                r.sendDeny(eventPlayer);
                event.setCancelled(true);
                return;
            }
        }
    }
}
