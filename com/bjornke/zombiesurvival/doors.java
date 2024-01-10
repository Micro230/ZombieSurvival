package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

public class doors {
	Plugin plugin;
	public List<door> doors = new ArrayList<>();

	public doors(Plugin instance) {
		this.plugin = instance;
	}

	public void saveDoor() {
		try {
			File file = new File(this.plugin.getDataFolder(), "doors.zss");
			FileOutputStream saveFile = new FileOutputStream(file);
			ObjectOutputStream save = new ObjectOutputStream(saveFile);
			save.writeObject(this.doors);
			save.close();
			Bukkit.getLogger().info("SUCCESSFULLY SAVED DOOR FILE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadDoor(List<Spawn> spawnfix) {
		try {
			File file = new File(this.plugin.getDataFolder(), "doors.zss");
			FileInputStream saveFile = new FileInputStream(file);
			ObjectInputStream restore = new ObjectInputStream(saveFile);
			this.doors = (ArrayList) restore.readObject();
			restore.close();
			for (door d : this.doors) {
				d.init();
				List<Spawn> concurrentfix = new ArrayList<>();
				List<Spawn> concurrentdelfix = new ArrayList<>();
				for(Spawn deserializespawn : d.spawns) {
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
						d.spawns.remove(cdf);
					}
				}
				if(concurrentfix != null) {
					for(Spawn ccf : concurrentfix) {
						d.spawns.add(ccf);
					}
				}
			}
			Bukkit.getLogger().info("SUCCESSFULLY LOADED DOOR FILE");
		} catch (Exception exception) {
			Bukkit.getLogger().severe("FAILED TO LOAD DOOR FILE");
			exception.printStackTrace();
		}
	}

	public void addDoor(Block b, String m, int w) {
		door newdr = new door(b, m, w);
		this.doors.add(newdr);
		showDoor(newdr);
		saveDoor();
	}

	public void removeDoor(door d) {
		Block b = d.location.getBlock();
		b.setType(d.type);
		this.doors.remove(d);
		saveDoor();
	}

	public void resetDoors(String m, List<Spawn> spawnfix) {
		for (door d : this.doors) {
			if (d.map.matches(m)) {
				if (d.location != null) {
					Block b = d.location.getBlock();
					b.setType(d.type);
					d.open = false;
					deActivateSpawns(d, spawnfix);
				}
			}
		}
	}

	public door findDoor(double x, double y, double z) {
		for (door d : this.doors) {
			if (d.x == x && d.y == y && d.z == z) {
				return d;
			}
		}
		return null;
	}

	public List<Location> doorLocs(String map) {
		List<Location> locs = new ArrayList<>();
		for (door d : this.doors) {
			if (d.map.matches(map) && d.location != null) {
				locs.add(d.location);
			}
		}

		return locs;
	}

	public List<door> doorWave(String map, int wave) {
		List<door> dwave = new ArrayList<>();
		for (door d : this.doors) {
			if (d.map.matches(map) && d.wave == wave) {
				dwave.add(d);
			}
		}
		return dwave;
	}

	public void openDoors(String map, int wave) {
		for (door d : doorWave(map, wave)) {
			Block b = d.location.getBlock();
			b.setType(Material.AIR);
			d.open = true;
			activateSpawns(d);
		}
	}

	public void openDoor(door d) {
		if (d == null) {
			return;
		}
		Block b = d.location.getBlock();
		b.setType(Material.AIR);
		d.open = true;
		activateSpawns(d);
	}

	public void showDoors(String map) {
		for (door d : this.doors) {
			if (d.map.matches(map)) {
				if (d.location != null) {
					Block b = d.location.getBlock();
					b.setType(Material.LIME_WOOL);
				}
			}
		}
	}

	public void hideDoors(String map) {
		for (door d : this.doors) {
			if (d.map.matches(map)) {
				if (d.location != null) {
					Block b = d.location.getBlock();
					b.setType(d.type);
				}
			}
		}
	}

	public void showDoor(door d) {
		Block b = d.location.getBlock();
		b.setType(Material.LIME_WOOL);
	}

	public void hideDoor(door d) {
		if (d.location != null) {
			Block b = d.location.getBlock();
			b.setType(d.type);
		}
	}

	public List<door> getAllDoors(){
		List<door> doorlist = new ArrayList<>();
		for (door d : this.doors) {
			doorlist.add(d);
		}

		return doorlist;
	}

	public void activateSpawns(door d) {
		for (Spawn s : d.spawns) {
			s.activated = true;
		}
	}

	public void deActivateSpawns(door d, List<Spawn> spawnfix) {
		for (Spawn s : d.spawns) {
			s.activated = false;
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