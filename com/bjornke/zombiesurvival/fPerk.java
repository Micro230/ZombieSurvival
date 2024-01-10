package com.bjornke.zombiesurvival;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

import org.bukkit.boss.*;

public class fPerk implements Listener {
	Random random = new Random();
	LUAMath luaMath = new LUAMath();
	private Map<String, Integer> perkID = new HashMap<>();
	private Map<String, Integer> gameperks = new HashMap<>();
	public static BossBar bb_gm = Bukkit.createBossBar("God Mode", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
	public static BossBar bb_ik = Bukkit.createBossBar("Insta-Kill", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
	public static BossBar bb_fp = Bukkit.createBossBar("Fire Perk", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
	public static BossBar bb_if = Bukkit.createBossBar("Iron Fist", BarColor.RED, BarStyle.SOLID, BarFlag.CREATE_FOG);
	public boolean isDropped = false;

	public void setPerk(String map, int i) {
		this.gameperks.put(map, Integer.valueOf(i));
	}

	public int getPerk(String map) {
		return ((Integer) this.gameperks.get(map)).intValue();
	}

	public Set<String> setPerk() {
		return this.gameperks.keySet();
	}

	public void callPerk(String map, Location loc) {
		if (loc != null && !this.isDropped) {
			ItemStack item = new ItemStack(perkItem(), 1);
			World world = Bukkit.getWorld(main.Maps.get(map));
			Item i = world.dropItem(loc, item);
			i.teleport(loc);
			this.perkID.put(map, Integer.valueOf(i.getEntityId()));
			this.isDropped = true;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPickUp(EntityPickupItemEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) e.getEntity();
			Item im = e.getItem();
			if (pmethods.inGame(player)) {
				String map = pmethods.playerGame(player);
				if (this.perkID.get(map) != null && im.getEntityId() == ((Integer) this.perkID.get(map)).intValue()) {
					e.setCancelled(true);
					Material randomperk = im.getItemStack().getType();
					switch (randomperk) {
					case GOLD_INGOT:
						this.gameperks.put(map, Integer.valueOf(1));
						for (String pl : pmethods.playersInMap(map)) {
							Player p = Bukkit.getPlayer(pl);
							if (p != null) {
								bb_gm.addPlayer(p);
								bb_gm.setVisible(true);
							}
						}
						break;
					case DIAMOND:
						this.gameperks.put(map, Integer.valueOf(2));
						for (String pl : pmethods.playersInMap(map)) {
							Player p = Bukkit.getPlayer(pl);
							if (p != null) {
								bb_ik.addPlayer(p);
								bb_ik.setVisible(true);
							}
						}
						break;
					case LAPIS_LAZULI:
						this.gameperks.put(map, Integer.valueOf(3));
						for (String pl : pmethods.playersInMap(map)) {
							Player p = Bukkit.getPlayer(pl);
							if (p != null) {
								bb_fp.addPlayer(p);
								bb_ik.setVisible(true);
							}
						}
						break;
					case IRON_INGOT:
						this.gameperks.put(map, Integer.valueOf(6));
						for (String pl : pmethods.playersInMap(map)) {
							Player p = Bukkit.getPlayer(pl);
							if (p != null) {
								bb_if.addPlayer(p);
								bb_if.setVisible(true);
							}
						}
						break;

					default:
						break;
					}
					im.remove();
					this.isDropped = false;
				}
			}
		}
		if (e.getEntityType() == EntityType.ZOMBIE) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDespawn(ItemDespawnEvent e) {
		int id = e.getEntity().getEntityId();
		for (Iterator<String> it = this.perkID.keySet().iterator(); it.hasNext();) {
			String m = it.next();
			if (((Integer) this.perkID.get(m)).intValue() == id) {
				it.remove();
				this.isDropped = false;
			}
		}
	}
	
	public void forceDespawn(int ID) {
		for (Iterator<String> it = this.perkID.keySet().iterator(); it.hasNext();) {
			String m = it.next();
			if (((Integer) this.perkID.get(m)).intValue() == ID) {
				it.remove();
				this.isDropped = false;
			}
		}
	}

	public void NewPerk(String map) {
		int randomperk = this.random.nextInt(5) + 1;
		switch (randomperk) {
		case 1:
			this.gameperks.put(map, Integer.valueOf(1));
			for (String pl : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(pl);
				if (p != null) {
					bb_gm.addPlayer(p);
					bb_gm.setVisible(true);
				}
			}
			break;
		case 2:
			this.gameperks.put(map, Integer.valueOf(2));
			for (String pl : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(pl);
				if (p != null) {
					bb_ik.addPlayer(p);
					bb_ik.setVisible(true);
				}
			}
			break;
		case 3:
			this.gameperks.put(map, Integer.valueOf(3));
			for (String pl : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(pl);
				if (p != null) {
					bb_fp.addPlayer(p);
					bb_fp.setVisible(true);
				}
			}
			break;
		case 5:
			this.gameperks.put(map, Integer.valueOf(6));
			for (String pl : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(pl);
				if (p != null) {
					bb_if.addPlayer(p);
					bb_if.setVisible(true);
				}
			}
			break;
		}
	}

	public static void PerkProgress(double PerkBarProgress) {
		if (fPerk.bb_gm.isVisible()) {
			fPerk.bb_gm.setProgress(PerkBarProgress);
		}
		if (fPerk.bb_ik.isVisible()) {
			fPerk.bb_ik.setProgress(PerkBarProgress);
		}
		if (fPerk.bb_fp.isVisible()) {
			fPerk.bb_fp.setProgress(PerkBarProgress);
		}
		if (fPerk.bb_if.isVisible()) {
			fPerk.bb_if.setProgress(PerkBarProgress);
		}
	}

	public static void ClearPerk(Player p) {
		if (bb_gm.isVisible()) {
			bb_gm.removeAll();
			bb_gm.setVisible(false);
		}
		if (bb_ik.isVisible()) {
			bb_ik.removeAll();
			bb_ik.setVisible(false);
		}
		if (bb_fp.isVisible()) {
			bb_fp.removeAll();
			bb_fp.setVisible(false);
		}
		if (bb_if.isVisible()) {
			bb_if.removeAll();
			bb_if.setVisible(false);
		}
	}

	public Material perkItem() {
		int randomperk = luaMath.random(1, 4);
		switch (randomperk) {
		case 1:
			return Material.GOLD_INGOT;
		case 2:
			return Material.DIAMOND;
		case 3:
			return Material.LAPIS_LAZULI;
		case 4:
			return Material.IRON_INGOT;
		}
		return Material.AIR;
	}
}
