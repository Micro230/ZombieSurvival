package com.bjornke.zombiesurvival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Barricade implements Serializable {
	public double x;
	public double y;
	public double z;
	public Material type;
	public String bdata;
	public String world;
	public String map;
	public int health;
	public transient Location location;
	public List<Spawn> spawns = new ArrayList<>();

	public Barricade(Block b, String m) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.map = m;
		this.world = b.getWorld().getName();
		this.location = b.getLocation();
		this.bdata = b.getBlockData().getAsString();
		this.type = b.getType();
		this.health = 50;
	}

	public void init() {
		World w = Bukkit.getWorld(this.world);
		this.location = new Location(w, this.x, this.y, this.z);
		Block b = this.location.getBlock();
		b.setType(this.type);
		b.setBlockData(Bukkit.createBlockData(this.bdata));
	}
}
