package com.md_5.district;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class Executor {

    static boolean checkLWC(final Block block) {
        final District plugin = District.instance;
        if (plugin.lwc != null && plugin.lwc.findProtection(block) != null) {
            // Let LWC handle it.
            return true;
        } else {
            // We have to handle it :(
            return false;
        }
    }

    static void handle(final Cancellable event, final Block block, final Player player) {
        final Location location = block.getLocation();
        if (Executor.checkLWC(block)) {
            return;
        }
        // Regions the block is in
        ArrayList<Region> currentRegionSet = Util.getRegions(location);
        // Check if they are denied from placing in ANY region he block is in
        for (Region r : currentRegionSet) {
            if (!r.canUse(player)) {
                r.sendDeny(player);
                event.setCancelled(true);
                return;
            }
        }
    }
}
