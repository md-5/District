package com.md_5.district;

import static com.md_5.district.Executor.handle;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DistrictListener implements Listener {

    public DistrictListener(final District plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void blockPlace(final BlockPlaceEvent event) {
        if (!event.isCancelled()) {
            handle(event, event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler
    public void blockBreak(final BlockBreakEvent event) {
        if (!event.isCancelled()) {
            handle(event, event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler
    public void playerWand(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getItemInHand().getTypeId() == Config.wand && event.getAction() == Action.RIGHT_CLICK_BLOCK && player.hasPermission("district.wand")) {
            final Location location = event.getClickedBlock().getLocation();
            final ArrayList<Region> currentRegionSet = Util.getRegions(location);
            final StringBuilder regions = new StringBuilder();
            if (currentRegionSet != null) {
                for (final Region r : currentRegionSet) {
                    regions.append(r.getName());
                    regions.append(" (");
                    regions.append(r.getOwner());
                    regions.append("), ");
                }
            }
            if (!regions.toString().isEmpty()) {
                player.sendMessage(ChatColor.GOLD + "District: Applicable regions: " + regions.substring(0, regions.length() - 2));
            } else if (player.hasPermission("district.wand") && player.getItemInHand().getTypeId() == Config.wand) {
                player.sendMessage(ChatColor.GOLD + "District: There are no applicable regions here");
            }
            event.setCancelled(true);
        }
    }
}
