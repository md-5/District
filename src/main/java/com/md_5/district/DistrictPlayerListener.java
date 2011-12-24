package com.md_5.district;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class DistrictPlayerListener extends PlayerListener {

    private final District plugin;

    public DistrictPlayerListener(final District plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvent(Type.PLAYER_INTERACT, this, Priority.Normal, plugin);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Event details
        Player eventPlayer = event.getPlayer();
        Block block = event.getClickedBlock();
         Location eventLocation = block.getLocation();
        if (block == null) {
            return;
        }
        // Don't do anything for pressure plates
        Material type = block.getType();
        if (type == Material.WOOD_PLATE || type == Material.STONE_PLATE) {
            return;
        }
        if (plugin.lwc != null && plugin.lwc.findProtection(block) != null) {
            // Let LWC handle it.
            return;
        }  
        // Regions the block is in
        ArrayList<Region> currentRegionSet = Util.getRegions(eventLocation);
        String regions = "";
        // Check if they are denied from placing in ANY region the block is in
        if (currentRegionSet != null) {
            for (Region r : currentRegionSet) {
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
            eventPlayer.sendMessage(ChatColor.GOLD + "District: Applicable regions: " + regions);
        } else if (eventPlayer.hasPermission("district.wand") && eventPlayer.getItemInHand().getTypeId() == Config.wand) {
            eventPlayer.sendMessage(ChatColor.GOLD + "District: There are no applicable regions here");
        }
        return;
    }
}
