package com.md_5.district;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Region {

    private String name;
    private Location l1;
    private Location l2;
    private World w;
    private String owner;
    private List<String> members;
    private String greeting;
    private String farewell;
    private ChunkGrid grid;

    public Region(World w, Location l1, Location l2, String owner, List<String> members, String name) {
        this.w = w;
        this.l1 = l1;
        this.l2 = l2;
        this.owner = owner;
        this.members = members;
        this.grid = new ChunkGrid(l1, l2, this);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return w;
    }

    public void setWorld(World w) {
        this.w = w;
    }

    public Location getL1() {
        return l1;
    }

    public void setL1(Location l1) {
        this.l1 = l1;
    }

    public Location getL2() {
        return l2;
    }

    public void setL2(Location l2) {
        this.l2 = l2;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOwner(Player p) {
        String player = p.getName();
        if (player.equals(owner) || p.hasPermission("district.ignore")) {
            return true;
        }
        return false;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public boolean isMember(String name) {
        return members.contains(name);
    }

    public void addMember(String name) {
        this.members.add(name);
    }

    public void removeMember(String name) {
        this.members.remove(name);
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public void setFarewell(String farewell) {
        this.farewell = farewell;
    }

    public ChunkGrid getChunkGrid() {
        return this.grid;
    }

    // Mesage sending
    public void sendGreeting(Player p) {
        return;
    }

    public void sendFarewell(Player p) {
        return;
    }

    public void sendDeny(Player p) {
        p.sendMessage(ChatColor.RED + "District: You cannot do that in this region!");
        return;
    }

    // Check if they can be part of the region
    public boolean canUse(Player player) {
        String p = player.getName();
        if (owner.equals(p)) {
            return true;
        } else if (members.contains(p)) {
            return true;
        } else if (player.hasPermission("district.ignore")) {
            return true;
        } else {
            return false;
        }
    }

    public String getGreeting() {
        return greeting;
    }

    public String getFarewell() {
        return farewell;
    }

    public Location getMin() {
        return new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()));
    }

    public Location getMax() {
        return new Location(l1.getWorld(), Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()), Math.max(l1.getBlockZ(), l2.getBlockZ()));
    }

    public int getSize() {
        Location min = getMin();
        Location max = getMax();
        int start_x = min.getBlockX();
        int start_y = min.getBlockY();
        int start_z = min.getBlockZ();
        int end_x = max.getBlockX();
        int end_y = max.getBlockY();
        int end_z = max.getBlockZ();

        int size = 0;

        for (int x = start_x; x <= end_x; x++) {
            for (int y = start_y; y <= end_y; y++) {
                for (int z = start_z; z <= end_z; z++) {
                    size++;
                }
            }
        }
        return (int) Math.cbrt(size);
    }
}
