package com.bjornke.zombiesurvival;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import net.minecraft.world.entity.EntityLightning;
import net.minecraft.world.entity.EntityTypes;
//import net.minecraft.server.v1_16_R3.EntityLightning; 1.16
//import net.minecraft.server.v1_16_R3.EntityTypes;

public class LocalThunderHandler implements Listener {
	private static final String version = "net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private static final String cbVersion = "org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	private static Class<?> entityClass;
	private static Class<?> entityTypesClass;
	private static Class<?> entityLightningClass;
	private static Class<?> packetSpawnWeatherClass;
	private static Class<?> worldClass;
	private static Class<?> entityPlayerClass;
	private static Class<?> craftWorldClass;
	private static Class<?> playerConnectionClass;
	private static Class<?> craftPlayerClass;
	private static Class<?> packetClass;

	static {
		try {
			entityClass = Class.forName("net.minecraft.world.entity" + ".Entity");//version
			entityTypesClass = Class.forName("net.minecraft.world.entity" + ".EntityTypes");//version
			entityLightningClass = Class.forName("net.minecraft.world.entity" + ".EntityLightning");//version
			packetSpawnWeatherClass = Class.forName("net.minecraft.network.protocol.game" + ".PacketPlayOutSpawnEntity");//version
			worldClass = Class.forName("net.minecraft.world.level" + ".World");//version
			entityPlayerClass = Class.forName("net.minecraft.server.level" + ".EntityPlayer");//version
			craftWorldClass = Class.forName(cbVersion + ".CraftWorld");//cbversion
			playerConnectionClass = Class.forName("net.minecraft.server.network" + ".PlayerConnection");//version
			craftPlayerClass = Class.forName(cbVersion + ".entity.CraftPlayer");//cbversion
			packetClass = Class.forName("net.minecraft.network.protocol" + ".Packet");//version
		} catch (Exception ex) {
			ex.printStackTrace();
			Bukkit.getPluginManager().disablePlugin((Plugin) main.mainfile);
		}
	}

	public static Object makePacket(Location l) {
		try {
			if (!l.getChunk().isLoaded()) {
				return null;
			}

			Object cWorld = craftWorldClass.cast(l.getWorld());
			Method m = craftWorldClass.getMethod("getHandle", new Class[0]);
			m.setAccessible(true);
			Object cWorldHandle = m.invoke(cWorld, new Object[0]);
			m.setAccessible(false);
			Constructor<?> elinit = entityLightningClass.getConstructor(new Class[] { entityTypesClass, worldClass });
			Object el = elinit.newInstance(new Object[] { EntityTypes.U, cWorldHandle });
			Location loc = new Location(l.getWorld(), l.getX(), l.getY(), l.getZ());
			((EntityLightning) el).getBukkitEntity().teleport(loc);
			((EntityLightning) el).b(l.getX(), l.getY(), l.getZ(), l.getPitch(), l.getYaw());
			Constructor<?> packetinit = packetSpawnWeatherClass.getConstructor(new Class[] { entityClass });
			return packetinit.newInstance(new Object[] { el });
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void sendPacket(Object packet, Player p) {
		try {
			Object cp = craftPlayerClass.cast(p);
			Method m = craftPlayerClass.getMethod("getHandle", new Class[0]);
			m.setAccessible(true);
			Object handle = m.invoke(cp, new Object[0]);
			m.setAccessible(false);
			//Field connectionField = entityPlayerClass.getField("playerConnection"); 1.16
			Field connectionField = entityPlayerClass.getField("b");
			connectionField.setAccessible(true);
			Object connection = connectionField.get(handle);
			connectionField.setAccessible(false);

			//Method sendPacket = playerConnectionClass.getMethod("sendPacket", new Class[] { packetClass }); 1.16
			Method sendPacket = playerConnectionClass.getMethod("a", new Class[] { packetClass });
			sendPacket.setAccessible(true);
			sendPacket.invoke(connection, new Object[] { packet });
			sendPacket.setAccessible(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void strike(Location l) {
		Object packet = makePacket(l);

		Item item = l.getWorld().dropItem(l, new ItemStack(Material.GLOWSTONE_DUST, 1));
		Collection<Entity> see = null;
		Collection<Entity> hear = null;
		if (main.mainfile.xSeeDistance == -1 && main.mainfile.ySeeDistance == -1 && main.mainfile.zSeeDistance == -1) {
			see = item.getNearbyEntities(Bukkit.getServer().getViewDistance() * 16,
					Bukkit.getServer().getViewDistance() * 16, Bukkit.getServer().getViewDistance() * 16);
		} else {
			see = item.getNearbyEntities(main.mainfile.xSeeDistance, main.mainfile.ySeeDistance, main.mainfile.zSeeDistance);
		}
		if (main.mainfile.xHearDistance == -1 && main.mainfile.yHearDistance == -1 && main.mainfile.zHearDistance == -1) {
			hear = item.getNearbyEntities(Bukkit.getServer().getViewDistance() * 16,
					Bukkit.getServer().getViewDistance() * 16, Bukkit.getServer().getViewDistance() * 16);
		} else {
			hear = item.getNearbyEntities(main.mainfile.xHearDistance, main.mainfile.yHearDistance, main.mainfile.zHearDistance);
		}
		item.remove();

		for (Entity ent : see) {
			if (ent instanceof Player) {
				Player p = (Player) ent;
				if (packet != null) {
					sendPacket(packet, p);
				}
			}
		}
		for (Entity ent : hear) {
			if (ent instanceof Player) {
				Sound ls = lightningSound();
				if (ls != null) {
					((Player) ent).playSound(l, ls, 10000.0F, 0.8F + (float) Math.round(Math.random() * 0.2D));
				}

				Sound es = explosionSound();
				if (es != null) {
					((Player) ent).playSound(l, es, 2.0F, 0.5F + (float) Math.round(Math.random() * 0.2D));
				}
			}
		}
	}

	private static Sound lightningSound() {
		return getSound(new String[] { "ENTITY_LIGHTNING_BOLT_THUNDER", "ENTITY_LIGHTNING_THUNDER" });
	}

	private static Sound explosionSound() {
		return getSound(new String[] { "ENTITY_GENERIC_EXPLODE", "EXPLODE" });
	}

	private static Sound getSound(String... names) {
		for (String s : names) {
			try {
				return Sound.valueOf(s);
			} catch (Exception exception) {
			}
		}
		return null;
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void lightningStrike(LightningStrikeEvent e) {
		if (!e.isCancelled()) {
			e.setCancelled(true);

			strike(e.getLightning().getLocation());

			if (main.mainfile.doFire) {
				Block b = e.getLightning().getLocation().getBlock();
				Block s = b.getRelative(BlockFace.UP);
				if (!s.getType().isSolid() && !s.getType().equals(Material.WATER)
						&& !s.getType().equals(Material.LAVA)) {
					s.setType(Material.FIRE);
				}
			}

			if (main.mainfile.chargeCreepers) {
				for (Entity ent : e.getLightning().getNearbyEntities(3.333D, 3.333D, 3.333D)) {
					if (ent != null && ent.getType().equals(EntityType.CREEPER)) {
						Creeper c = (Creeper) ent;
						c.setPowered(true);
					}
				}
			}
		}
	}
}
