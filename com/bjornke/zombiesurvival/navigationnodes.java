package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class navigationnodes {
	Plugin plugin;
	public List<NavigationNode> navnodes = new ArrayList<>();

	public navigationnodes(Plugin instance) {
		this.plugin = instance;
	}

	public void saveNodeSpawn() {
		try {
			File file = new File(this.plugin.getDataFolder(), "navigationnodes.zss");
			FileOutputStream saveFile = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(this.navnodes);
			save.close();
			Bukkit.getLogger().info("SUCCESSFULLY SAVED NAVIGATION NODES FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO SAVE NAVIGATION NODES FILE: " + exception.getMessage());
		}
	}

	public void loadNodeSpawn() {
		try {
			File file = new File(this.plugin.getDataFolder(), "navigationnodes.zss");
			FileInputStream saveFile = new FileInputStream(file);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			this.navnodes = (ArrayList) restore.readObject();
			restore.close();
			for (NavigationNode sp : this.navnodes) {
				sp.init();
			}
			Bukkit.getLogger().info("SUCCESSFULLY LOADED NAVIGATION NODES FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO LOAD NAVIGATION NODES FILE: " + exception.getMessage());
		}
	}

	public void addNavigationNode(Block b, String m, String sc, int sn, boolean isn) {
		NavigationNode newsp = new NavigationNode(b, m, sc, sn, isn);
		this.navnodes.add(newsp);
		showNavigationNode(newsp);
		saveNodeSpawn();
	}

	public void removeNavigationNode(NavigationNode d) {
		Block b = d.location.getBlock();
		b.setType(Material.AIR);
		this.navnodes.remove(d);
		saveNodeSpawn();
	}
	
	public boolean doesExist(String sc) {//Can be removed if nav door ends up using subway numbers. Use findNavigationNode
		for (NavigationNode sp : this.navnodes) {
			if(sp.subwaycolor.equals(sc)) {
				return true;
			}
		}
		return false;
	}
	
	public NavigationNode findNavigationNode(String sc, int sn) {
		for (NavigationNode sp : this.navnodes) {
			if(sp.subwaycolor.equals(sc) && sp.subwaynumber == sn) {
				return sp;
			}
		}
		return null;
	}
	
	public NavigationNode findNavigationNode(double x, double y, double z) {
		for (NavigationNode sp : this.navnodes) {
			if (sp.x == x && sp.y == y && sp.z == z) {//Error here often
				return sp;
			}
		}
		return null;
	}

	public List<NavigationNode> getAllNodes(){
		List<NavigationNode> spawnlist = new ArrayList<>();
		for (NavigationNode sp : this.navnodes) {
			spawnlist.add(sp);
		}
		return spawnlist;
	}

	public void showNavigationNodes(String map) {
		for (NavigationNode d : this.navnodes) {
			if (d.map.matches(map)) {
				showNavigationNode(d);
			}
		}
	}

	public void hideNavigationNodes(String map) {
		for (NavigationNode d : this.navnodes) {
			if (d.map.matches(map)) {
				hideNavigationNode(d);
			}
		}
	}

	public void showNavigationNode(NavigationNode s) {
		if (s.location != null) {
			Block b = s.location.getBlock();
			b.setType(Material.RED_WOOL);
		}
	}

	public void hideNavigationNode(NavigationNode s) {
		if (s.location != null) {
			Block b = s.location.getBlock();
			b.setType(s.type);
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