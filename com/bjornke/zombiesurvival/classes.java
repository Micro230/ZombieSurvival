package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class classes {
	public Plugin plugin;
	private static HashMap<String, String> userclass = new HashMap<>();
	private static HashMap<String, List<ItemStack>> classitems = new HashMap<>();
	private static HashMap<HashMap<String, Integer>, List<String>> upgradeline = new HashMap<>();
	private static HashMap<String, String> classes = new HashMap<>();
	private static HashMap<String, String> description = new HashMap<>();
	private static HashMap<String, Boolean> isDefault = new HashMap<>();
	private static HashMap<String, Integer> unlockedbyrank = new HashMap<>();
	private static FileConfiguration cConfig = null;
	private static File cConfigFile = null;
	private static MicroCraft Microcraft = new MicroCraft();

	public static String getUserClass(Player p) {
		return userclass.get(p.getName());
	}
	
	public static String getUserClass(String p) {
		return userclass.get(p);
	}

	public static void setUserClass(Player p, String cl) {
		if (classes.containsKey(cl)) {
			if (userclass.get(p.getName()) != null && ((String) userclass.get(p.getName())).equalsIgnoreCase(cl)) {
				p.sendMessage(ChatColor.DARK_RED + "You're already in this class!");
				return;
			}
			userclass.put(p.getName(), cl);
			p.sendMessage(ChatColor.BLUE + "You joined: " + ChatColor.GRAY + (String) classes.get(cl));
			if(description.get(cl) != null) {
				p.sendMessage(ChatColor.GRAY + (String) description.get(cl));
			}
		} else {
			p.sendMessage(ChatColor.RED + "Invalid class!");
		}
	}

	public static Set<String> getAllUsers() {
		return userclass.keySet();
	}

	public static Set<String> getAllClasses() {
		return classes.keySet();
	}

	public static void removeUser(Player p) {
		userclass.remove(p.getName());
	}

	public static boolean isClassed(Player p) {
		if (userclass.containsKey(p.getName())) {
			return true;
		}
		return false;
	}

	public static void setupPlayer(Player p, CustomPlayerData cpdata) {
		String name = p.getName();
		if (userclass.get(name) != null && isClassed(p)) {
			String cls = userclass.get(name);
			for (ItemStack i : classitems.get(userclass.get(name))) {
				utilities.equipPlayer(p, i, cpdata);
			}
		}
	}

	public static List<String> getUpgrades(Player p, int lvl) {
		String name = p.getName();
		String cls = userclass.get(name);
		Map<String, Integer> caller = new HashMap<>();
		caller.put(cls, lvl);
		System.out.println(upgradeline.get(caller));
		return (upgradeline.get(caller));
	}

	public void savecConfig() {
		if (cConfig == null || cConfigFile == null) {
			return;
		}
		try {
			getcConfig().save(cConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + cConfigFile, ex);
		}
	}

	public FileConfiguration getcConfig() {
		if (cConfig == null) {
			reloadcConfig();
		}
		return cConfig;
	}

	public void reloadcConfig() {
		if (cConfigFile == null) {
			cConfigFile = new File(this.plugin.getDataFolder(), "classes.yml");
		}
		cConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(cConfigFile);
		File defConfigStream = new File(this.plugin.getDataFolder(), "classes.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			cConfig.setDefaults((Configuration) defConfig);
		}
	}
	
	public boolean doesClassExist(String s) {
		for(String cls : classes.keySet()) {
			if(s.equalsIgnoreCase(cls)) {
				return true;
			}
		}
		return false;
	}
	
	public String ApplyUpgrade(Player p, CustomPlayerData cpdata, int level) {//Can return upgrade if necessary
		HashMap<String, Integer> caller = new HashMap<>();
		caller.put(userclass.get(p.getName()), level);
		List<String> levelstring = null;
		for(Entry<HashMap<String, Integer>, List<String>> immutable : upgradeline.entrySet()) {
			if(immutable.getKey().containsKey(userclass.get(p.getName()))){
				if(immutable.getKey().get(userclass.get(p.getName())) == level) {
					levelstring = immutable.getValue();
					break;
				}
			}
		}
		String rstr = "";
		for(String lstring : levelstring) {
			if(lstring.contains("Effect") && lstring.contains(":")) {
				try {
					p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(lstring.substring(7, lstring.length()-3)), 9999 * 20, Integer.parseInt(lstring.substring(lstring.length()-3, lstring.length()-1))));
					if(!rstr.equals("")) {
						rstr = rstr + ":";
					}
					rstr = rstr + "Effect," + lstring.substring(7, lstring.length()-3) + "|" + lstring.substring(lstring.length()-3, lstring.length()-1);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(lstring.contains("Repair")) {
				try {
					cpdata.setAutoRepair(lstring.substring(7, lstring.length()-1));
					if(!rstr.equals("")) {
						rstr = rstr + ":";
					}
					rstr = rstr + "Repair," + lstring.substring(7, lstring.length()-1);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(lstring.contains("Regenerate")) {
				try {
					cpdata.setAutoRegenerate(lstring.substring(11, lstring.length()-1));
					if(!rstr.equals("")) {
						rstr = rstr + ":";
					}
					rstr = rstr + "Regenerate," + lstring.substring(11, lstring.length()-1);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(lstring.equals("RemoveUpgrades")) {
				try {
					cpdata.clearAutoRegenerate();
					cpdata.clearAutoRepair();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		return rstr;
	}

	public String ApplyUpgrades(Player p, Map<String, Integer> playerupgradelevel, CustomPlayerData cpdata) {
		// Check if item, effect, effect upgrade, or nothing
		if (playerupgradelevel.get(p.getName()) == 1) {
			return ApplyUpgrade(p, cpdata, 1);
		}
		if (playerupgradelevel.get(p.getName()) == 2) {
			return ApplyUpgrade(p, cpdata, 2);
		}
		if (playerupgradelevel.get(p.getName()) == 3) {
			return ApplyUpgrade(p, cpdata, 3);
		}
		return null;
	}
	
	public static int getClassUnlockedByRank(String cls) {
		if(unlockedbyrank.get(cls) != null) {
			return unlockedbyrank.get(cls);
		}
		return 0;
	}
	
	public static boolean isUnlockedByDefault(String cls) {
		if(isDefault.get(cls) != null) {
			return isDefault.get(cls);
		}
		return false;
	}

	public void LoadClasses() {
		for (String cls : getcConfig().getStringList("Classes")) {
			classes.put(cls, cls); //May change to cls, true/false depending on payed or not payed class
			description.put(cls, getcConfig().getString(String.valueOf(cls) + ".description"));
			unlockedbyrank.put(cls, getcConfig().getInt(String.valueOf(cls) + ".unlockedbyrank"));
			isDefault.put(cls, getcConfig().getBoolean(String.valueOf(cls) + ".isunlockedbydefault"));
			for (String it : getcConfig().getStringList(String.valueOf(cls) + ".items")) {
				try {
					String[] line = it.split(",");
					Material itemid = Material.getMaterial(line[0]);
					int amount = 1;
					ItemStack item = new ItemStack(itemid, amount);
					
					if (line.length > 1) {
						amount = Integer.parseInt(line[1]);
						item = new ItemStack(itemid, amount);
						if (line.length > 2) {
							String potiontype = line[2];
							int potionduration = Integer.parseInt(line[3]);
							int potionstrength = Integer.parseInt(line[4]);
							String potionname = line[5];
							int r = Integer.parseInt(line[6]);
							int g = Integer.parseInt(line[7]);
							int b = Integer.parseInt(line[8]);
							item = Microcraft.parsePotion(itemid, potiontype, potionduration, potionstrength, potionname, r, g, b, amount);
						}
					}
					
					if (classitems.get(cls) != null) {
						((List<ItemStack>) classitems.get(cls)).add(item);
						continue;
					}
					classitems.put(cls, new ArrayList<>());
					((List<ItemStack>) classitems.get(cls)).add(item);
				} catch (Exception e) {
					this.plugin.getLogger().warning("Could not load class: " + cls);
				}
			}
			List<String> levelslist = new ArrayList<>();
			HashMap<String, Integer> classandlevel = new HashMap<>();
			List<String> Level1List = getcConfig().getStringList(String.valueOf(cls) + ".level1");
			List<String> Level2List = getcConfig().getStringList(String.valueOf(cls) + ".level2");
			List<String> Level3List = getcConfig().getStringList(String.valueOf(cls) + ".level3");
			
			for(String level1 : Level1List) {
				classandlevel.put(cls, 1);
				levelslist.add(level1);
			}
			upgradeline.put(classandlevel, levelslist);
			levelslist = new ArrayList<>();
			classandlevel = new HashMap<>();
			
			for(String level2 : Level2List) {
				classandlevel.put(cls, 2);
				levelslist.add(level2);
			}
			upgradeline.put(classandlevel, levelslist);			levelslist = new ArrayList<>();
			classandlevel = new HashMap<>();;
			
			for(String level3 : Level3List) {
				classandlevel.put(cls, 3);
				levelslist.add(level3);
			}
			upgradeline.put(classandlevel, levelslist);			levelslist = new ArrayList<>();
			classandlevel = new HashMap<>();;
		}
	}
}