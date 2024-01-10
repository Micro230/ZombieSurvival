package com.bjornke.zombiesurvival;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;

public class weapons {
	
	public static Map<UUID, Integer> shootdelaylist = new HashMap<>();
	public static Map<UUID, Integer> reloaddelaylist = new HashMap<>();
	
	static ItemStack woodhoe = new ItemStack(Material.WOODEN_HOE);
	static ItemStack stonehoe = new ItemStack(Material.STONE_HOE);
	static ItemStack ironhoe = new ItemStack(Material.IRON_HOE);
	static ItemStack goldhoe = new ItemStack(Material.GOLDEN_HOE);
	static ItemStack diamondhoe = new ItemStack(Material.DIAMOND_HOE);
	static ItemStack netherhoe = new ItemStack(Material.NETHERITE_HOE);
	
	static double wooddamage = 0.4;
	static double stonedamage = 0.1;
	static double irondamage = 0.7;
	static double golddamage = 4;
	static double diamonddamage = 0.6;
	static double netherdamage = 2;
	
	public static void weaponUsed(PlayerInteractEvent event, CustomPlayerData cpdata, int st, Plugin plugin) {
		switch(st) {
			case 0:
				shootWood(event, cpdata, plugin);
				break;
			case 1:
				shootStone(event, cpdata, plugin);
				break;
			case 2:
				shootIron(event, cpdata, plugin);
				break;
			case 3:
				shootGold(event, cpdata, plugin);
				break;
			case 4:
				shootDiamond(event, cpdata, plugin);
				break;
			case 5:
				shootNether(event, cpdata, plugin);
				break;
			case 6:
				reloadWood(event, cpdata, plugin);
				break;
			case 7:
				reloadStone(event, cpdata, plugin);
				break;
			case 8:
				reloadIron(event, cpdata, plugin);
				break;
			case 9:
				reloadGold(event, cpdata, plugin);
				break;
			case 10:
				reloadDiamond(event, cpdata, plugin);
				break;
			case 11:
				reloadNether(event, cpdata, plugin);
				break;				
		}		
	}
	
	public static boolean isGun(Material item) {
		
		if(item.equals(woodhoe.getType())) {
			return true;
		}
		else if(item.equals(stonehoe.getType())) {
			return true;
		}
		else if(item.equals(ironhoe.getType())) {
			return true;
		}
		else if(item.equals(goldhoe.getType())) {
			return true;
		}
		else if(item.equals(diamondhoe.getType())) {
			return true;
		}
		else if(item.equals(netherhoe.getType())) {
			return true;
		}
		return false;
	}
	
	public static ItemStack setNameReturn(ItemStack item, CustomPlayerData cpdata) {
		if(item.getType().equals(woodhoe.getType())) {
			ItemMeta meta = woodhoe.getItemMeta();
			meta.setDisplayName("Pistol Ammo: " + Integer.toString(cpdata.getAmmoClip(0)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(0)));
			woodhoe.setItemMeta(meta);
			return woodhoe;
		}
		else if(item.getType().equals(stonehoe.getType())) {
			ItemMeta meta = stonehoe.getItemMeta();
			meta.setDisplayName("Shotgun Ammo: " + Integer.toString(cpdata.getAmmoClip(1)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(1)));
			stonehoe.setItemMeta(meta);
			return stonehoe;
		}
		else if(item.getType().equals(ironhoe.getType())) {
			ItemMeta meta = ironhoe.getItemMeta();
			meta.setDisplayName("Rifle Ammo: " + Integer.toString(cpdata.getAmmoClip(2)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(2)));
			ironhoe.setItemMeta(meta);
			return ironhoe;
		}
		else if(item.getType().equals(goldhoe.getType())) {
			ItemMeta meta = goldhoe.getItemMeta();
			meta.setDisplayName("Sniper Ammo: " + Integer.toString(cpdata.getAmmoClip(3)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(3)));
			goldhoe.setItemMeta(meta);
			return goldhoe;
		}
		else if(item.getType().equals(diamondhoe.getType())) {
			ItemMeta meta = diamondhoe.getItemMeta();
			meta.setDisplayName("Heavy Machine Gun Ammo: " + Integer.toString(cpdata.getAmmoClip(4)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(4)));
			diamondhoe.setItemMeta(meta);
			return diamondhoe;
		}
		else if(item.getType().equals(netherhoe.getType())) {
			ItemMeta meta = netherhoe.getItemMeta();
			meta.setDisplayName("Ray Gun Ammo: " + Integer.toString(cpdata.getAmmoClip(5)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(5)));
			netherhoe.setItemMeta(meta);
			return netherhoe;
		}
		return null;
	}
	
	public static void setName(ItemStack item, CustomPlayerData cpdata, boolean empty, boolean reloading) {
		if(empty) {
			ItemMeta meta = item.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("OUT OF AMMO");
			item.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), item);
			return;
		}
		else if(reloading) {
			ItemMeta meta = item.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Reloading...");
			item.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), item);
			return;
		}
		else if(item.getType().equals(woodhoe.getType())) {
			ItemMeta meta = woodhoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Pistol Ammo: " + Integer.toString(cpdata.getAmmoClip(0)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(0)));
			woodhoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), woodhoe);
		}
		else if(item.getType().equals(stonehoe.getType())) {
			ItemMeta meta = stonehoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Shotgun Ammo: " + Integer.toString(cpdata.getAmmoClip(1)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(1)));
			stonehoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), stonehoe);
		}
		else if(item.getType().equals(ironhoe.getType())) {
			ItemMeta meta = ironhoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Rifle Ammo: " + Integer.toString(cpdata.getAmmoClip(2)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(2)));
			ironhoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), ironhoe);
		}
		else if(item.getType().equals(goldhoe.getType())) {
			ItemMeta meta = goldhoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Sniper Ammo: " + Integer.toString(cpdata.getAmmoClip(3)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(3)));
			goldhoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), goldhoe);
		}
		else if(item.getType().equals(diamondhoe.getType())) {
			ItemMeta meta = diamondhoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Heavy Machine Gun Ammo: " + Integer.toString(cpdata.getAmmoClip(4)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(4)));
			diamondhoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), diamondhoe);
		}
		else if(item.getType().equals(netherhoe.getType())) {
			ItemMeta meta = netherhoe.getItemMeta();
			PlayerInventory inv = cpdata.getPlayer().getInventory();
			meta.setDisplayName("Ray Gun Ammo: " + Integer.toString(cpdata.getAmmoClip(5)) + " | " + Integer.toString(cpdata.getMaxAmmoClip(5)));
			netherhoe.setItemMeta(meta);
			inv.setItem(inv.getHeldItemSlot(), netherhoe);
		}
	}
	
	public static Arrow getBullet(double damage, Arrow oldarrow, Player p, boolean explosion, boolean shotgun) {
		Arrow newarr = oldarrow;
		for(Player i : Bukkit.getServer().getOnlinePlayers()) {
		    PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(newarr.getEntityId());
		    ((CraftPlayer) i).getHandle().b.a(packet);
		    //((CraftPlayer) i).getHandle().playerConnection.sendPacket(packet); 1.16
		}
		newarr.setSilent(true);
		newarr.setGravity(false);
		newarr.setCritical(false);
		newarr.setDamage(damage);
		if(explosion) {
			newarr.setBounce(true);
		}
		else {
			newarr.setBounce(false);
		}
		newarr.setShooter(p.getPlayer());
		newarr.setVelocity(p.getPlayer().getEyeLocation().getDirection().multiply(6));
		if(shotgun) {
			Random rand = new Random();
			newarr.setVelocity(p.getPlayer().getEyeLocation().getDirection().multiply(6).add(new Vector(rand.nextDouble(), 0, rand.nextDouble()).subtract(new Vector(rand.nextDouble(), 0, rand.nextDouble()))));
		}
		return newarr;
	}
	
	public static void cancelEvents(CustomPlayerData cpdata) {
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		if(scheduler.isCurrentlyRunning(cpdata.eventrun1)) {
			scheduler.cancelTask(cpdata.eventrun1);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun2)) {
			scheduler.cancelTask(cpdata.eventrun2);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun3)) {
			scheduler.cancelTask(cpdata.eventrun3);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun4)) {
			scheduler.cancelTask(cpdata.eventrun4);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun5)) {
			scheduler.cancelTask(cpdata.eventrun5);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun6)) {
			scheduler.cancelTask(cpdata.eventrun6);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun7)) {
			scheduler.cancelTask(cpdata.eventrun7);
		}
		if(scheduler.isCurrentlyRunning(cpdata.eventrun8)) {
			scheduler.cancelTask(cpdata.eventrun8);
		}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun9)) {
			scheduler.cancelTask(cpdata.eventrun9);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun10)) {
			scheduler.cancelTask(cpdata.eventrun10);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun11)) {
			scheduler.cancelTask(cpdata.eventrun11);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun12)) {
			scheduler.cancelTask(cpdata.eventrun12);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun13)) {
			scheduler.cancelTask(cpdata.eventrun13);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun14)) {
			scheduler.cancelTask(cpdata.eventrun14);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun15)) {
			scheduler.cancelTask(cpdata.eventrun15);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun16)) {
			scheduler.cancelTask(cpdata.eventrun16);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun17)) {
			scheduler.cancelTask(cpdata.eventrun17);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun18)) {
			scheduler.cancelTask(cpdata.eventrun18);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun19)) {
			scheduler.cancelTask(cpdata.eventrun19);
		//}
		//if(scheduler.isCurrentlyRunning(cpdata.eventrun20)) {
			scheduler.cancelTask(cpdata.eventrun20);
		//}
		reloaddelaylist.remove(cpdata.getUUID());
	}
	
	public static void shootWood(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 0;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			a = getBullet(wooddamage, a, event.getPlayer(), false, false);
			a.getWorld().playEffect(event.getPlayer().getEyeLocation(), Effect.SMOKE, 50);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
			cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-1);
			cpdata.canFire(false);
			setName(event.getItem(), cpdata, false, false);
			
			if(shootdelaylist.get(event.getPlayer().getUniqueId()) == null) {
				shootdelaylist.put(cpdata.getUUID(),0);
				cpdata.eventrun1 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if(!cpdata.canFire()){
							shootdelaylist.remove(cpdata.getUUID());
		                	cpdata.canFire(true);
		                }
					}
				},(20 / cpdata.shootperkmultiplier));
			}
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void shootStone(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 1;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			Arrow b = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			Arrow c = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			Arrow d = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			Arrow e = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			Arrow f = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			a = getBullet(stonedamage, a, event.getPlayer(), false, true);
			getBullet(stonedamage, b, event.getPlayer(), false, true);
			getBullet(stonedamage, c, event.getPlayer(), false, true);
			getBullet(stonedamage, d, event.getPlayer(), false, true);
			getBullet(stonedamage, e, event.getPlayer(), false, true);
			getBullet(stonedamage, f, event.getPlayer(), false, true);
			a.getWorld().playEffect(event.getPlayer().getEyeLocation(), Effect.SMOKE, 50);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_PISTON_CONTRACT, 1, 1);
			cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-2);
			cpdata.canFire(false);
			setName(event.getItem(), cpdata, false, false);
			
			if(shootdelaylist.get(event.getPlayer().getUniqueId()) == null) {
				shootdelaylist.put(cpdata.getUUID(),0);
				cpdata.eventrun2 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if(!cpdata.canFire()){
							shootdelaylist.remove(cpdata.getUUID());
		                	cpdata.canFire(true);
		                }
					}
				},(20 / cpdata.shootperkmultiplier));
			}
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void shootIron(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 2;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			shootdelaylist.put(cpdata.getUUID(),0);
			cpdata.eventrun3 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
					a = getBullet(irondamage, a, event.getPlayer(), false, false);
					a.getWorld().playEffect(event.getPlayer().getEyeLocation(), Effect.SMOKE, 50);
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_SNOW_GOLEM_SHOOT, 1, 1);
					cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-1);
					cpdata.canFire(false);
					setName(event.getItem(), cpdata, false, false);
				}
			},(5 / cpdata.shootperkmultiplier));
			cpdata.eventrun4 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if(!cpdata.canFire()){
						shootdelaylist.remove(cpdata.getUUID());
	                	cpdata.canFire(true);
	                }
				}
			},(5 / cpdata.shootperkmultiplier));
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void shootGold(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 3;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			a = getBullet(golddamage, a, event.getPlayer(), false, false);
			a.getWorld().playEffect(event.getPlayer().getEyeLocation(), Effect.SMOKE, 50);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 1, 3);
			cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-1);
			cpdata.canFire(false);
			setName(event.getItem(), cpdata, false, false);
			
			if(shootdelaylist.get(event.getPlayer().getUniqueId()) == null) {
				shootdelaylist.put(cpdata.getUUID(),0);
				cpdata.eventrun5 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if(!cpdata.canFire()){
							shootdelaylist.remove(cpdata.getUUID());
		                	cpdata.canFire(true);
		                }
					}
				},(40 / cpdata.shootperkmultiplier));
			}
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void shootDiamond(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 4;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			shootdelaylist.put(cpdata.getUUID(),0);
			cpdata.eventrun6 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
					a = getBullet(diamonddamage, a, event.getPlayer(), false, false);
					a.getWorld().playEffect(event.getPlayer().getEyeLocation(), Effect.SMOKE, 50);
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_TRIDENT_HIT, 1, 0.25f);
					cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-1);
					cpdata.canFire(false);
					setName(event.getItem(), cpdata, false, false);
				}
			},(7 / cpdata.shootperkmultiplier));
			cpdata.eventrun7 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
				@Override
				public void run() {
					if(!cpdata.canFire()){
						shootdelaylist.remove(cpdata.getUUID());
	                	cpdata.canFire(true);
	                }
				}
			},(7 / cpdata.shootperkmultiplier));
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void shootNether(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 5;
		if(cpdata.canFire() && cpdata.getAmmoClip(weaponnum) > 0 && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
			Arrow a = (Arrow) event.getPlayer().getWorld().spawnEntity(event.getPlayer().getEyeLocation(), EntityType.ARROW);
			a = getBullet(netherdamage, a, event.getPlayer(), true, false);
			a.getWorld().playEffect(event.getPlayer().getLocation(), Effect.SMOKE, 50);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_FISHING_BOBBER_THROW, 1, 1);
			cpdata.setAmmoClip(weaponnum, cpdata.getAmmoClip(weaponnum)-1);
			cpdata.canFire(false);
			setName(event.getItem(), cpdata, false, false);
			
			if(shootdelaylist.get(event.getPlayer().getUniqueId()) == null) {
				shootdelaylist.put(cpdata.getUUID(),0);
				cpdata.eventrun8 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						if(!cpdata.canFire()){
							shootdelaylist.remove(cpdata.getUUID());
		                	cpdata.canFire(true);
		                }
					}
				},(20 / cpdata.shootperkmultiplier));
			}
		}
		else if(cpdata.getAmmoClip(weaponnum) <= 0 && shootdelaylist.get(event.getPlayer().getUniqueId()) == null && reloaddelaylist.get(event.getPlayer().getUniqueId()) == null){
			setName(event.getItem(), cpdata, true, false);
			cpdata.canFire(false);
			event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_DISPENSER_FAIL, 1, 1);
		}
	}
	
	public static void reloadWood(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 0;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun9 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(20 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun10 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(20 / cpdata.reloadperkmultiplier));
			}
		}
	}
	
	public static void reloadStone(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 1;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun11 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(30 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun12 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(30 / cpdata.reloadperkmultiplier));
			}
		}
	}
	
	public static void reloadIron(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 2;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun13 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(50 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun14 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(50 / cpdata.reloadperkmultiplier));
			}
		}
	}
	
	public static void reloadGold(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 3;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun15 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(40 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun16 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(40 / cpdata.reloadperkmultiplier));
			}
		}
	}
	
	public static void reloadDiamond(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 4;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun17 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(80 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun18 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
					}
				},(80 / cpdata.reloadperkmultiplier));
			}
		}
	}
	
	public static void reloadNether(PlayerInteractEvent event, CustomPlayerData cpdata, Plugin plugin) {
		int weaponnum = 5;
		int spareammo = cpdata.getAmmoSpare();
		int clipmax = cpdata.getMaxAmmoClip(weaponnum);
		int ammoclip = cpdata.getAmmoClip(weaponnum);
		
		if(clipmax == ammoclip) {//If weapon is already full
			return;
		}
		
		if (spareammo == 0 && ammoclip == 0) {//If weapon has no spare ammo or no ammo in clip
			setName(event.getItem(), cpdata, true, false);
			return;
		}
		
		if (spareammo == 0 && ammoclip > 0) {//If weapon has no extra ammo but still has ammo in the clip
			return;
		}
		
		if(spareammo > (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun19 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, clipmax);
						cpdata.setAmmoSpare(spareammo - (clipmax - ammoclip));
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
					}
				},(40 / cpdata.reloadperkmultiplier));
			}
		}
		else if(spareammo <= (clipmax - ammoclip)) {
			if(reloaddelaylist.get(event.getPlayer().getUniqueId()) == null) {
				reloaddelaylist.put(cpdata.getUUID(),0);
				setName(event.getItem(), cpdata, false, true);
				cpdata.canFire(false);
				cpdata.eventrun20 = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
					@Override
					public void run() {
						reloaddelaylist.remove(cpdata.getUUID());
						cpdata.setAmmoClip(weaponnum, (ammoclip + spareammo));
						cpdata.setAmmoSpare(0);
						cpdata.canFire(true);
						setName(event.getItem(), cpdata, false, false);
						event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1, 1);
					}
				},(40 / cpdata.reloadperkmultiplier));
			}
		}
	}
}