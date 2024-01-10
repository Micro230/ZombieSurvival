package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

public class zombieregions {
	Plugin plugin;
	public List<ZombieRegion> zombieregions = new ArrayList<>();

	public zombieregions(Plugin instance) {
		this.plugin = instance;
	}

	public void saveRegions() {
		try {
			File file = new File(this.plugin.getDataFolder(), "navregion.zss");
			FileOutputStream saveFile = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(this.zombieregions);
			save.close();
			Bukkit.getLogger().info("SUCCESSFULLY SAVED NAVIGATION REGION FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO SAVE NAVIGATION REGION FILE");
			exception.printStackTrace();
		}
	}

	public void loadRegions() {
		try {
			File file = new File(this.plugin.getDataFolder(), "navregion.zss");
			FileInputStream saveFile = new FileInputStream(file);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			this.zombieregions = (ArrayList) restore.readObject();
			restore.close();
			for (ZombieRegion sp : this.zombieregions) {
				sp.init();
			}
			Bukkit.getLogger().info("SUCCESSFULLY LOADED NAVIGATION REGION FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO LOAD NAVIGATION REGION FILE");
			exception.printStackTrace();
		}
	}
	
	public boolean doesExist(String name) {
		for (ZombieRegion sp : this.zombieregions) {
			if(sp.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void addNavNode(String name, NavigationNode node) {
		for (ZombieRegion sp : this.zombieregions) {
			if(sp.getName().equals(name)) {
				sp.addNode(node);
			}
		}
		saveRegions();
	}

	public void addNavigationRegion(String name, String map, Location a, Location b, NavigationNode neutralnode) {
		ZombieRegion newregion = new ZombieRegion(name, map, a, b, neutralnode);
		this.zombieregions.add(newregion);
		saveRegions();
	}

	public void removeNavigationRegion(String name) {
		for (ZombieRegion sp : this.zombieregions) {
			if(sp.getName() == name) {
				this.zombieregions.remove(sp);
			}
		}
		loadRegions();
	}
	
	public List<ZombieRegion> regionList() {
		return this.zombieregions;
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