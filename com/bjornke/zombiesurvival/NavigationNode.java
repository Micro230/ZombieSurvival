package com.bjornke.zombiesurvival;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class NavigationNode implements Serializable {
	public double x;
	public double y;
	public double z;
	public String world;
	public String map;
	public String subwaycolor;
	public int subwaynumber;
	public transient Location location;
	public Material type;
	public boolean isNeutral;

	public NavigationNode(Block b, String m, String sc, int sn, boolean isn) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.map = m;
		this.subwaycolor = sc;
		this.subwaynumber = sn;
		this.world = b.getWorld().getName();
		this.type = b.getType();
		this.location = b.getLocation();
		this.isNeutral = isn;
	}

	public void init() {
		World w = Bukkit.getWorld(this.world);
		this.location = new Location(w, this.x, this.y, this.z);
	}
}