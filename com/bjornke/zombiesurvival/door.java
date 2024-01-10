package com.bjornke.zombiesurvival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class door implements Serializable {
	public double x;
	public double y;
	public double z;
	public Material type;
	public int wave;
	public String world;
	public String map;
	public List<Spawn> spawns = new ArrayList<>();
	public boolean open;
	public transient Location location;

	public door(Block b, String m, int w) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.type = b.getType();
		this.open = false;
		this.location = b.getLocation();
		this.wave = w;
		this.world = b.getWorld().getName();
		this.map = m;
	}

	public void init() {
		World w = Bukkit.getWorld(this.world);
		this.location = new Location(w, this.x, this.y, this.z);
		this.open = false;
		Block b = this.location.getBlock();
		b.setType(this.type);
	}
}
