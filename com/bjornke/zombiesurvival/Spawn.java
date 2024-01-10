package com.bjornke.zombiesurvival;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Spawn implements Serializable {
	public double x;
	public double y;
	public double z;
	public byte data;
	public Material type;
	public String world;
	public String map;
	public boolean activated;
	public int wave;
	public transient Location location;

	public Spawn(Block b, String m, int w) {
		this.x = b.getX() + 0.5D;
		this.y = b.getY() + 1.0D;
		this.z = b.getZ() + 0.5D;
		this.map = m;
		this.activated = false;
		this.wave = w;
		this.world = b.getWorld().getName();
		this.location = b.getLocation().add(0.5D, 1.0D, 0.5D);
		this.type = b.getRelative(BlockFace.UP).getType();
	}

	public void init() {
		World w = Bukkit.getWorld(this.world);
		this.location = new Location(w, this.x, this.y, this.z);
		Block b = this.location.getBlock();
		b.setType(this.type);
	}
}
