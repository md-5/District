package com.md_5.district;

import static com.md_5.district.Executor.handle;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class DistrictListener implements Listener {

    private HashMap<Location, RegionSearcher> futures = new HashMap<Location, RegionSearcher>();

    public DistrictListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, District.instance);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockPlaceInit(final BlockPlaceEvent event) {
        if (!event.isCancelled() && !event.getPlayer().hasPermission("district.ignore")) {
            final Location location = event.getBlock().getLocation();
            futures.put(location, new RegionSearcher(location));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void blockBreakInit(final BlockBreakEvent event) {
        if (!event.isCancelled() && !event.getPlayer().hasPermission("district.ignore")) {
            final Location location = event.getBlock().getLocation();
            futures.put(location, new RegionSearcher(location));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockPlacePost(final BlockPlaceEvent event) throws Exception {
        if (!event.isCancelled() && !event.getPlayer().hasPermission("district.ignore")) {
            final Region r = futures.get(event.getBlock().getLocation()).future.get();
            if (r == null) {
                return;
            }
            handle(event, r, event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blockBreakPost(final BlockBreakEvent event) throws Exception {
        if (!event.isCancelled() && !event.getPlayer().hasPermission("district.ignore")) {
            final Region r = futures.get(event.getBlock().getLocation()).future.get();
            if (r == null) {
                return;
            }
            handle(event, r, event.getPlayer());
        }
    }

    @EventHandler
    public void playerWand(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getItemInHand().getTypeId() == Config.wand && event.getAction() == Action.RIGHT_CLICK_BLOCK && player.hasPermission("district.wand")) {
            final Location location = event.getClickedBlock().getLocation();
            final Region r = Util.getRegion(location);
            String regions = new String();
            if (r != null) {
                regions = r.getName() + " (" + r.getOwner() + ")";
            }
            if (!regions.isEmpty()) {
                player.sendMessage(ChatColor.GOLD + "District: Applicable regions: " + regions);
            } else {
                player.sendMessage(ChatColor.GOLD + "District: There are no applicable regions here");
            }
            event.setCancelled(true);
        }
    }
}
