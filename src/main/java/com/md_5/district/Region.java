package com.md_5.district;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Region {

    private String name;
    public int start_x, start_y, start_z;
    public int end_x, end_y, end_z;
    private World w;
    private String owner;
    private ArrayList<String> members;

    public Region(final World w, final Location l1, final Location l2, final String owner, final ArrayList<String> members, final String name) {
        this.w = w;
        Location loc1 = Util.getMin(l1, l2);
        Location loc2 = Util.getMax(l1, l2);
        start_x = loc1.getBlockX();
        start_y = loc1.getBlockY();
        start_z = loc1.getBlockZ();
        end_x = loc2.getBlockX();
        end_y = loc2.getBlockY();
        end_z = loc2.getBlockZ();
        this.owner = owner;
        this.members = members;
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

    public String getOwner() {
        return owner;
    }

    public boolean canAdmin(Player p) {
        if (isOwner(p) || p.hasPermission("district.ignore")) {
            return true;
        }
        return false;
    }

    public boolean isOwner(String name) {
        return owner.equals(name);
    }

    public boolean isOwner(Player p) {
        return isOwner(p.getName());
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

    public Vector getSize() {
        return new Vector(end_x - start_x, end_y - start_y, end_z - start_z);
    }

    public boolean isMember(Player player) {
        return isMember(player.getName());
    }

    public int getVolume() {
        Vector size = getSize();
        return (int) (size.getX() * size.getY() * size.getZ());
    }
}
