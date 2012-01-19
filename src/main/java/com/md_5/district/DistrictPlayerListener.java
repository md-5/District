package com.md_5.district;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class DistrictPlayerListener implements Listener {

    private final District plugin;

    public DistrictPlayerListener(final District plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        // Event details
        final Player eventPlayer = event.getPlayer();
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
        // Don't do anything for pressure plates
        final int type = block.getTypeId();
        if (type == Material.STONE_PLATE.getId() || type == Material.WOOD_PLATE.getId()) {
            return;
        }
        if (Executor.checkLWC(block)) {
            return;
        }
        final Location eventLocation = block.getLocation();
        // Regions the block is in
        final ArrayList<Region> currentRegionSet = Util.getRegions(eventLocation);
        String regions = "";
        // Check if they are denied from placing in ANY region the block is in
        if (currentRegionSet != null) {
            for (final Region r : currentRegionSet) {
                if (eventPlayer.hasPermission("district.wand") && eventPlayer.getItemInHand().getTypeId() == Config.wand) {
                    regions += r.getName() + " (" + r.getOwner() + "), ";
                }
                if (!r.canUse(eventPlayer)) {
                    r.sendDeny(eventPlayer);
                    event.setCancelled(true);
                }
            }
        }
        if (!regions.equals("")) {
            eventPlayer.sendMessage(ChatColor.GOLD + "District: Applicable regions: " + regions.substring(0, regions.length() - 2));
        } else if (eventPlayer.hasPermission("district.wand") && eventPlayer.getItemInHand().getTypeId() == Config.wand) {
            eventPlayer.sendMessage(ChatColor.GOLD + "District: There are no applicable regions here");
        }
    }
}
