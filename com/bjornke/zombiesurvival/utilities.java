package com.bjornke.zombiesurvival;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPigZombie;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftSpider;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class utilities {
	public static boolean isArmor(ItemStack i) {
		if (isHelm(i) || isChestplate(i) || isLeggings(i) || isBoots(i)) {
			return true;
		}
		return false;
	}

	public static boolean isHelm(ItemStack i) {
		Material id = i.getType();
		if (id == Material.LEATHER_HELMET || id == Material.CHAINMAIL_HELMET || id == Material.IRON_HELMET
				|| id == Material.DIAMOND_HELMET) {
			return true;
		}
		return false;
	}

	public static boolean isChestplate(ItemStack i) {
		Material id = i.getType();
		if (id == Material.LEATHER_CHESTPLATE || id == Material.CHAINMAIL_CHESTPLATE || id == Material.IRON_CHESTPLATE
				|| id == Material.DIAMOND_CHESTPLATE) {
			return true;
		}
		return false;
	}

	public static boolean isLeggings(ItemStack i) {
		Material id = i.getType();
		if (id == Material.LEATHER_LEGGINGS || id == Material.CHAINMAIL_LEGGINGS || id == Material.IRON_LEGGINGS
				|| id == Material.DIAMOND_LEGGINGS) {
			return true;
		}
		return false;
	}

	public static boolean isBoots(ItemStack i) {
		Material id = i.getType();
		if (id == Material.LEATHER_BOOTS || id == Material.CHAINMAIL_BOOTS || id == Material.IRON_BOOTS
				|| id == Material.DIAMOND_BOOTS) {
			return true;
		}
		return false;
	}

	public static void equipPlayer(Player p, ItemStack i, CustomPlayerData cpdata) {
		if (isArmor(i)) {
			if (isHelm(i)) {
				p.getInventory().setHelmet(i);
			} else if (isChestplate(i)) {
				p.getInventory().setChestplate(i);
			} else if (isLeggings(i)) {
				p.getInventory().setLeggings(i);
			} else if (isBoots(i)) {
				p.getInventory().setBoots(i);
			}
		}
		else if(weapons.isGun(i.getType())) {
			p.getInventory().addItem(new ItemStack[] { weapons.setNameReturn(i, cpdata) });
		}
		else {
			p.getInventory().addItem(new ItemStack[] { i });
		}
	}

	public static String processForColors(String str) {
		String parsed;
		if (str != null && str.contains("&")) {
			parsed = str.replaceAll("&", "§");
		} else if (str != null) {
			parsed = str;
		} else {
			parsed = "";
		}
		return parsed;
	}

	public static String annouceMax(String map) {
		int max = 0;
		int mz = games.getMaxZombies(map);
		int w = games.getWave(map);
		if (mz < 10) {
			max = (int) ((mz * w) * 0.5D);
		}
		if (mz >= 10 && mz <= 50) {
			max = (int) ((mz * w) * 0.1D);
		}
		if (mz >= 51 && mz <= 100) {
			max = (int) ((mz * w) * 0.08D);
		}
		if (mz >= 101 && mz <= 200) {
			max = (int) ((mz * w) * 0.05D);
		}
		if (mz >= 201) {
			max = (int) ((mz * w) * 0.04D);
		}
		String string = Integer.toString(max);
		return string;
	}

	public static void clearDrops(String map) {
		World world = Bukkit.getWorld(main.Maps.get(map));
		List<Entity> templist = main.getEnts(world);
		for (Entity e : templist) {
			if (e instanceof org.bukkit.entity.Item && e.isValid()) {
				e.remove();
			}
		}
	}

	public static void hidePlayer(Player player) {
		if (player != null && player.isValid()) {
			Player[] list = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
			byte b;
			int i;
			Player[] arrayOfPlayer1;
			for (i = (arrayOfPlayer1 = list).length, b = 0; b < i;) {
				Player p = arrayOfPlayer1[b];
				if (p != null && p.isValid()) {
					p.hidePlayer(player);
					if (!pmethods.inGame(player))
						player.hidePlayer(p);
				}
				b++;
			}

		}
	}

	public static void unhidePlayer(Player player) {
		if (player != null && player.isValid()) {
			Player[] list = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
			byte b;
			int i;
			Player[] arrayOfPlayer1;
			for (i = (arrayOfPlayer1 = list).length, b = 0; b < i;) {
				Player p = arrayOfPlayer1[b];
				if (p != null && p.isValid())
					p.showPlayer(player);
				b++;
			}

		}
	}

	public static boolean livingEntityMoveTo(LivingEntity livingEntity, double x, double y, double z, float speed) {
		if (livingEntity instanceof org.bukkit.entity.Zombie) {
			return ((CraftZombie) livingEntity).getHandle().D().a(x, y, z, speed);
			//return ((CraftZombie) livingEntity).getHandle().getNavigation().a(x, y, z, speed);
		}
		if (livingEntity instanceof org.bukkit.entity.Wolf) {
			return ((CraftWolf) livingEntity).getHandle().D().a(x, y, z, speed);
		}
		return false;
	}

	public static Location getSegment(LivingEntity mob, Location tar) {
		if (Math.abs(tar.getX() - mob.getLocation().getX()) < 10.0D
				&& Math.abs(tar.getZ() - mob.getLocation().getZ()) < 10.0D) {
			return tar;
		}
		return trigdis(mob.getLocation(), tar);
	}

	private static Location trigdis(Location o, Location t) {
		double xdis = 9.0D;
		double zdis = 9.0D;
		if (t.getX() < o.getX()) {
			xdis = -9.0D;
		}
		if (Math.abs(o.getX() - t.getX()) < 9.0D) {
			xdis = -(o.getX() - t.getX());
		}
		if (t.getZ() < o.getZ()) {
			zdis = -9.0D;
		}
		if (Math.abs(o.getZ() - t.getZ()) < 9.0D) {
			zdis = -(o.getZ() - t.getZ());
		}
		return new Location(o.getWorld(), o.getX() + xdis, o.getY(), o.getZ() + zdis);
	}

	public static double calcMaxZ(int pmax) {
		if (pmax <= 5)
			return pmax * 0.12D;
		if (pmax > 5 && pmax <= 10)
			return pmax * 0.1D;
		if (pmax > 10 && pmax <= 15) {
			return pmax * 0.09D;
		}
		return pmax * 0.1D;
	}
}