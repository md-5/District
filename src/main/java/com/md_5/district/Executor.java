package com.md_5.district;

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

    static void handle(final Cancellable event, final Region region, final Player player) {
        if (!region.canUse(player)) {
            Region.sendDeny(player);
            event.setCancelled(true);
        }
    }
}
