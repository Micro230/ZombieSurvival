package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.bukkit.entity.Zombie;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftZombie;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class spawns {
	Plugin plugin;
	public List<Spawn> spawns = new ArrayList<>();
	public Map<String, Location> lobbies = new HashMap<>();
	public Map<String, Location> spectate = new HashMap<>();
	public Random rand = new Random();

	public spawns(Plugin instance) {
		this.plugin = instance;
	}

	public void saveSpawn() {
		try {
			File file = new File(this.plugin.getDataFolder(), "spawns.zss");
			FileOutputStream saveFile = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(this.spawns);
			save.close();
			Bukkit.getLogger().info("SUCCESSFULLY SAVED SPAWN FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO SAVE SPAWN FILE: " + exception.getLocalizedMessage());
		}
	}

	public void loadSpawn() {
		try {
			File file = new File(this.plugin.getDataFolder(), "spawns.zss");
			FileInputStream saveFile = new FileInputStream(file);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			this.spawns = (ArrayList) restore.readObject();
			restore.close();
			for (Spawn sp : this.spawns) {
				sp.init();
			}
			Bukkit.getLogger().info("SUCCESSFULLY LOADED SPAWN FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO LOAD SPAWN FILE: " + exception.getLocalizedMessage());
		}
	}

	public void addSpawn(Block b, String m, int w) {
		Spawn newsp = new Spawn(b, m, w);
		this.spawns.add(newsp);
		showSpawn(newsp);
		saveSpawn();
	}

	public void removeSpawn(Spawn d) {
		Block b = d.location.getBlock();
		b.setType(Material.AIR);
		this.spawns.remove(d);
		saveSpawn();
	}

	public void resetSpawn(String m) {
		for (Spawn sp : this.spawns) {
			if (sp.map.matches(m)) {
				sp.activated = false;
			}
		}
	}

	public Spawn findSpawn(double x, double y, double z) {
		for (Spawn sp : this.spawns) {
			if (sp.x == x && sp.y == y && sp.z == z) {//Error here often
				return sp;
			}
		}
		return null;
	}

	public List<Spawn> getAllSpawns(){
		List<Spawn> spawnlist = new ArrayList<>();
		for (Spawn sp : this.spawns) {
			spawnlist.add(sp);
		}
		return spawnlist;
	}
	
	public List<Location> spawnLocs(String map, int wave, boolean buydoors) {
		List<Location> locs = new ArrayList<>();
		for (Spawn sp : this.spawns) {
			if (buydoors) {
				if (sp.map.matches(map) && sp.activated && sp.location != null) {
					locs.add(sp.location);
				}
				else if (sp.map.matches(map) && !sp.activated && sp.location != null) {
					if (sp.wave == 1) {
						sp.activated = true;
						locs.add(sp.location);
					}
				}
			}
			else {
				if (sp.map.matches(map) && sp.wave <= wave && sp.location != null) {
					locs.add(sp.location);
				}
			}
		}

		return locs;
	}

	public Location spawn(String map, int wave, boolean buydoors) {
		Random random = new Random();
		Location loc = this.lobbies.get(map);
		if (games.getWave(map) != 0) {// Prevent zombies spawning after game end
			try {
				loc = spawnLocs(map, wave, buydoors).get(random.nextInt(spawnLocs(map, wave, buydoors).size()));
			} catch (Exception e) {
				System.err.println("[ZombieSurvival] No zombie spawns for: " + map + " using lobby point instead!");
			}
		}
		return loc;
	}

	public void showSpawns(String map) {
		for (Spawn d : this.spawns) {
			if (d.map.matches(map)) {
				showSpawn(d);
			}
		}
	}

	public void hideSpawns(String map) {
		for (Spawn d : this.spawns) {
			if (d.map.matches(map)) {
				hideSpawn(d);
			}
		}
	}

	public void showSpawn(Spawn s) {
		if (s.location != null) {
			Block b = s.location.getBlock();
			b.setType(Material.LIME_WOOL);
		}
	}

	public void hideSpawn(Spawn s) {
		if (s.location != null) {
			Block b = s.location.getBlock();
			b.setType(Material.AIR);
		}
	}

	public void EquipMob(LivingEntity ent) {
		if (ent.getType() == EntityType.ZOMBIE) {

		}
	}

	public void Destroy() {
		try {
			finalize();
		} catch (Throwable e) {
			this.plugin.getLogger().warning("Failed to destroy class");
		}
	}
}