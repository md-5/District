package com.md_5.district;

import static com.md_5.district.Executor.handle;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DistrictListener implements Listener {

    public DistrictListener(final District plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void blockPlace(final BlockPlaceEvent event) {
        handle(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void blockBreak(final BlockBreakEvent event) {
        handle(event, event.getBlock(), event.getPlayer());
    }

    @EventHandler
    public void playerInteract(final PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        final int type = block.getTypeId();
        if (type == 70 || type == 72) {
            return;
        }
        handle(event, block, event.getPlayer());
    }

    @EventHandler
    // TODO
    public void playerWand(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        final Player player = event.getPlayer();
        final Location location = event.getClickedBlock().getLocation();
        // Regions the block is in
        final ArrayList<Region> currentRegionSet = Util.getRegions(location);
        String regions = "";
        // Check if they are denied from placing in ANY region the block is in
        if (currentRegionSet != null) {
            for (final Region r : currentRegionSet) {
                if (player.hasPermission("district.wand") && player.getItemInHand().getTypeId() == Config.wand) {
                    regions += r.getName() + " (" + r.getOwner() + "), ";
                }
            }
        }
        if (!regions.equals("")) {
            player.sendMessage(ChatColor.GOLD + "District: Applicable regions: " + regions.substring(0, regions.length() - 2));
        } else if (player.hasPermission("district.wand") && player.getItemInHand().getTypeId() == Config.wand) {
            player.sendMessage(ChatColor.GOLD + "District: There are no applicable regions here");
        }
    }
}
