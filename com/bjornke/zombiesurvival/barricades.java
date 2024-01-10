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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class barricades {
	Plugin plugin;
	public List<Barricade> bar = new ArrayList<>();
	public Map<Integer, Boolean> timerlist = new HashMap<>();
	public Map<Integer, Barricade> zombiebreakingbarricade = new HashMap<>();

	public void saveBar() {
		try {
			File file = new File(this.plugin.getDataFolder(), "barricades.zss");
			FileOutputStream saveFile = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(this.bar);
			save.close();
			Bukkit.getLogger().info("SUCCESSFULLY SAVED BARRICADE FILE");
		} catch (Exception e) {
			Bukkit.getLogger().severe("FAILED TO SAVE BARRICADE FILE: " + e.getLocalizedMessage());
		}
	}

	public void loadBar(List<Spawn> spawnfix) {
		try {
			File file = new File(this.plugin.getDataFolder(), "barricades.zss");
			FileInputStream saveFile = new FileInputStream(file);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			this.bar = (ArrayList) restore.readObject();
			restore.close();
			for (Barricade b : this.bar) {
				b.init();
				List<Spawn> concurrentfix = new ArrayList<>();
				List<Spawn> concurrentdelfix = new ArrayList<>();
				for(Spawn deserializespawn : b.spawns) {
					deserializespawn.init();
					for(Spawn csf : spawnfix) {
						if(csf.location.equals(deserializespawn.location)) {
							concurrentdelfix.add(deserializespawn);
							concurrentfix.add(csf);
						}
					}
				}
				if(concurrentdelfix != null) {
					for(Spawn cdf : concurrentdelfix) {
						b.spawns.remove(cdf);
					}
				}
				if(concurrentfix != null) {
					for(Spawn ccf : concurrentfix) {
						b.spawns.add(ccf);
					}
				}
			}
			Bukkit.getLogger().info("SUCCESSFULLY LOADED BARRICADE FILE");
		} catch (Exception e) {
			Bukkit.getLogger().severe("FAILED TO LOAD BARRICADE FILE:");
			e.printStackTrace();
		}
	}
	
	public List<Barricade> getAllBarricades(){
		List<Barricade> barlist = new ArrayList<>();
		for (Barricade b : this.bar) {
			barlist.add(b);
		}

		return barlist;
	}

	public void addBar(Block b, String m) {
		Barricade newdr = new Barricade(b, m);
		this.bar.add(newdr);
		showBar(newdr);
		saveBar();
	}

	public void removeBar(Barricade d) {
		Block b = d.location.getBlock();
		b.setType(d.type);
		this.bar.remove(d);
		saveBar();
	}

	public void showBarricades(String map) {
		for (Barricade d : this.bar) {
			if (d.map.matches(map)) {
				Block b = d.location.getBlock();
				b.setType(Material.LIME_WOOL);
			}
		}
	}

	public void hideBarricades(String map) {
		for (Barricade d : this.bar) {
			if (d.map.matches(map)) {
				Block b = d.location.getBlock();
				b.setType(d.type);
				b.setBlockData(Bukkit.createBlockData(d.bdata));
			}
		}
	}

	public void showBar(Barricade d) {
		Block b = d.location.getBlock();
		b.setType(Material.LIME_WOOL);
	}

	public void hideBar(Barricade d) {
		Block b = d.location.getBlock();
		b.setType(d.type);
	} 
	
	public Barricade getBarricadeLinkedToSpawn(Spawn s, List<Spawn> spawnfix) {//Get Closest and Get Linked
		for (Barricade barcade : this.bar) {
			if(barcade.spawns.contains(s)) {
				return barcade;
			}
		}
		return null;
	}


	public Barricade findBar(double x, double y, double z) {
		for (Barricade d : this.bar) {
			if (d.x == x && d.y == y && d.z == z) {
				return d;
			}
		}
		return null;
	}

	public void damageBarricade(Barricade d, boolean damageone) {
		if (damageone) {
			return;
		}
		d.health -= 10;
		if(d.health <= 0) {
			d.health = 0;
			Block b = d.location.getBlock();
			b.setType(Material.AIR);
			b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.6F, 1);
		}
	}

	public boolean healBarricade(Barricade d, boolean repairone) {
		if (d.health >= 30 || repairone) {
			return false;
		}
		d.health = 30;
		Block b = d.location.getBlock();
		b.setType(d.type);
		b.setBlockData(Bukkit.createBlockData(d.bdata));
		b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.6F, 1);
		return true;
	}
	
	public int getBarricadeHealth(Barricade d) {
		return d.health;
	}

	public List<Barricade> checkForBarricades(Location middle, String map) {
		List<Barricade> barshere = new ArrayList<>();
		for (int x = middle.getBlockX() - 1; x <= middle.getBlockX() + 1; x++) {
			for (int y = middle.getBlockY() - 1; y <= middle.getBlockY() + 1; y++) {
				for (int z = middle.getBlockZ() - 1; z <= middle.getBlockZ() + 1; z++) {
					Barricade d = findBar(x, y, z);
					if (d != null && d.map.matches(map)) {
						barshere.add(d);
					}
				}
			}
		}
		return barshere;
	}
	
	public boolean allBarricadesGone(Location middle, String map) {
		List<Barricade> barshere = checkForBarricades(middle, map);
		for(Barricade b: barshere) {
			if(b.health > 0) {
				return false;
			}
		}
		return true;
	}
	
	public void stopzombiebreaking(int id) {
		this.zombiebreakingbarricade.put(id, null);
	}

	public int healBars(Location middle, String map) {
		int pay = 1;
		boolean repairone = false;
		List<Barricade> barlist = checkForBarricades(middle, map);
		for (Barricade d : barlist) {
			for(Map.Entry<Integer,Barricade> dtest : this.zombiebreakingbarricade.entrySet()) {
				if(!dtest.equals(null)) {
					if(d.equals(dtest.getValue())) {
						return 2;
					}
				}
			}
		}
		for (Barricade d : barlist) {
			if (healBarricade(d, repairone) && (pay == 1)) {
				pay = 0;
				repairone = true;
			}
		}
		return pay;
	}

	public void damageBars(Location middle, String map, int id) {
		boolean damageone = false;
		List<Barricade> barlist = checkForBarricades(middle, map);
		if(barlist.isEmpty()) {//If there are no barricades nearby
			this.stopzombiebreaking(id);
			return;
		}
		for (Barricade d : barlist) {
			if(d.health != 0) {
				damageBarricade(d, damageone);
				this.zombiebreakingbarricade.put(id, d);
				damageone = true;
			}
		}
	}

	public void resetBars(String map) {
		for (Barricade d : this.bar) {
			if (d.map.matches(map)) {
				Block b = d.location.getBlock();
				b.setType(d.type);
				b.setBlockData(Bukkit.createBlockData(d.bdata));
				d.health = 30;
			}
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