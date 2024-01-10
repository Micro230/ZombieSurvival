package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class stats {
	public Plugin plugin;
	private static Map<String, Double> totalpoints = new HashMap<>();
	private static Map<String, Double> totalkills = new HashMap<>();
	private static Map<String, Double> totaldeaths = new HashMap<>();
	private static Map<String, Double> sessionpoints = new HashMap<>();
	private static Map<String, Double> sessionkills = new HashMap<>();
	private static Map<String, Double> sessiondeaths = new HashMap<>();
	private static Map<String, Map<String, Boolean>> playerclasses = new HashMap<>();
	private static FileConfiguration SConfig = null;
	private static File SConfigFile = null;

	public void LoadStats() {
		for (String p : getSConfig().getStringList("Players")) {
			addDeaths(p, getSConfig().getDouble(String.valueOf(p) + ".td"));
			addKills(p, getSConfig().getDouble(String.valueOf(p) + ".tk"));
			addPoints(p, getSConfig().getDouble(String.valueOf(p) + ".tp"));
			loadPlayerClass(p, getSConfig().getString(String.valueOf(p) + ".classesunlocked"));
		}
	}
	
	public static void loadPlayerClass(String player, String list) {
		if(list != null) {
			String[] split = list.split(", ");
			Map<String, Boolean> filterlist = new HashMap<>();
			for(String s: split) {
				String key = s.split("!")[0];
				String val = s.split("!")[1];
				filterlist.put(key, Boolean.valueOf(val));
			}
			playerclasses.put(player, filterlist);
		}
	}

	public static double getTotalPoints(String player) {
		if (totalpoints.get(player) != null) {
			return ((Double) totalpoints.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static double getTotalKills(String player) {
		if (totalkills.get(player) != null) {
			return ((Double) totalkills.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static double getTotalDeaths(String player) {
		if (totaldeaths.get(player) != null) {
			return ((Double) totaldeaths.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static double getSesPoints(String player) {
		if (sessionpoints.get(player) != null) {
			return ((Double) sessionpoints.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static double getSesKills(String player) {
		if (sessionkills.get(player) != null) {
			return ((Double) sessionkills.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static double getSesDeaths(String player) {
		if (sessiondeaths.get(player) != null) {
			return ((Double) sessiondeaths.get(player)).doubleValue();
		}
		return 0.0D;
	}

	public static Set<String> getPlayers() {
		return totalpoints.keySet();
	}

	public static void addPoints(String player, double points) {
		if (totalpoints.get(player) != null) {
			double p1 = ((Double) totalpoints.get(player)).doubleValue();
			double p2 = p1 + points;
			totalpoints.put(player, Double.valueOf(p2));
		}
		else {
			totalpoints.put(player, Double.valueOf(points));
		}
		if (sessionpoints.get(player) != null) {
			double p1 = ((Double) sessionpoints.get(player)).doubleValue();
			double p2 = p1 + points;
			sessionpoints.put(player, Double.valueOf(p2));
		}
		else {
			sessionpoints.put(player, Double.valueOf(points));
		}
	}
	
	public static void removePoints(String player, double points) {
		if (sessionpoints.get(player) != null) {
			double p1 = ((Double) sessionpoints.get(player)).doubleValue();
			double p2 = p1 - points;
			sessionpoints.put(player, Double.valueOf(p2));
		}
		else {
			sessionpoints.put(player, Double.valueOf(points));
		}
	}
	
	public static void removeTotalPoints(String player, double points) {
		if (totalpoints.get(player) != null) {
			double p1 = ((Double) totalpoints.get(player)).doubleValue();
			double p2 = p1 - points;
			totalpoints.put(player, Double.valueOf(p2));
		}
		else {
			totalpoints.put(player, Double.valueOf(points));
		}
	}

	public static void addKills(String player, double points) {
		if (totalkills.get(player) != null) {
			double p1 = ((Double) totalkills.get(player)).doubleValue();
			double p2 = p1 + points;
			totalkills.put(player, Double.valueOf(p2));
		}
		else {
			totalkills.put(player, Double.valueOf(points));
		}
		if (sessionkills.get(player) != null) {
			double p1 = ((Double) sessionkills.get(player)).doubleValue();
			double p2 = p1 + points;
			sessionkills.put(player, Double.valueOf(p2));
		}
		else {
			sessionkills.put(player, Double.valueOf(points));
		}
	}

	public static void addDeaths(String player, double points) {
		if (totaldeaths.get(player) != null) {
			double p1 = ((Double) totaldeaths.get(player)).doubleValue();
			double p2 = p1 + points;
			totaldeaths.put(player, Double.valueOf(p2));
		}
		else {
			totaldeaths.put(player, Double.valueOf(points));
		}
		if (sessiondeaths.get(player) != null) {
			double p1 = ((Double) sessiondeaths.get(player)).doubleValue();
			double p2 = p1 + points;
			sessiondeaths.put(player, Double.valueOf(p2));
		}
		else {
			sessiondeaths.put(player, Double.valueOf(points));
		}
	}

	public static void setPoints(String player, double p) {
		sessionpoints.put(player, Double.valueOf(p));
	}

	public static void setKills(String player, double p) {
		sessionkills.put(player, Double.valueOf(p));
	}

	public static void setDeaths(String player, double p) {
		sessiondeaths.put(player, Double.valueOf(p));
	}
	
	public static boolean isClassUnlocked(String player, String classunlocked) {
		if(playerclasses.get(player) != null) {
			if(playerclasses.get(player).get(classunlocked) != null) {
				if (playerclasses.get(player).get(classunlocked) == true) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void buyClass(String player, String classtobuy) {
		if(playerclasses.get(player) != null) {
			Map<String, Boolean> m = playerclasses.get(player);
			m.put(classtobuy, true);
			playerclasses.put(player, m);
		}
		else {
			Map<String, Boolean> m = new HashMap<>();
			for(String s : classes.getAllClasses()) {
				if(s.equals(classtobuy)) {
					m.put(classtobuy, true);
				}
				else {
					if(classes.isUnlockedByDefault(classes.getUserClass(player))) {
						m.put(s, true);
					}
					m.put(s, false);
				}
			}
			playerclasses.put(player, m);
		}
	}

	public static void clear() {
		sessiondeaths.clear();
		sessionkills.clear();
		sessionpoints.clear();
	}

	public static void removeSplayer(String player) {
		sessiondeaths.remove(player);
		sessionkills.remove(player);
		sessionpoints.remove(player);
	}

	public static void removeTplayer(String player) {
		totaldeaths.remove(player);
		totalkills.remove(player);
		totalpoints.remove(player);
	}

	public static void removeSplayerPoints(String player) {
		sessionpoints.remove(player);
	}

	public static void removeSplayerKills(String player) {
		sessionkills.remove(player);
	}

	public static void removeSplayerDeaths(String player) {
		sessiondeaths.remove(player);
	}

	public static void removeTplayerPoints(String player) {
		totalpoints.remove(player);
	}

	public static void removeTplayerKills(String player) {
		totalkills.remove(player);
	}

	public static void removeTplayerDeaths(String player) {
		totaldeaths.remove(player);
	}
	
	public static String leaderboardPlayer(int num){
		Set<String> pset = sortHashMapByValues(totalkills).keySet();
		ArrayList<String> tval = new ArrayList<>();
		for(String p : pset) {
			tval.add(p);
		}
		if(num >= 0 && num < tval.size()) {
			return tval.get(num);
		}
		else {
			return "Empty";
		}
	}
	
	public static Double leaderboardKill(String player){
		Double val = sortHashMapByValues(totalkills).get(player);
		if(val != null) {
			return sortHashMapByValues(totalkills).get(player);
		}
		else {
			return 0.0D;
		}
	}
	
	public static LinkedHashMap<String, Double> sortHashMapByValues(Map<String, Double> passedMap) {
	    List<String> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Double> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues);
	    Collections.sort(mapKeys);
	    Collections.reverse(mapValues);
        Collections.reverse(mapKeys);

	    LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	    	Double val = valueIt.next();
	        Iterator<String> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	        	String key = keyIt.next();
	            Double comp1 = passedMap.get(key);
	            Double comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}

	public static String topScorePlayer() {
		double initp = 0.0D;
		String user = "ERROR";
		for (String p : totalpoints.keySet()) {
			if (((Double) totalpoints.get(p)).doubleValue() > initp) {
				initp = ((Double) totalpoints.get(p)).doubleValue();
				user = p;
			}
		}
		return user;
	}

	public static String topKillsPlayer() {
		double initp = 0.0D;
		String user = "ERROR";
		for (String p : totalkills.keySet()) {
			if (((Double) totalkills.get(p)).doubleValue() > initp) {
				initp = ((Double) totalkills.get(p)).doubleValue();
				user = p;
			}
		}
		return user;
	}

	public static String topDeathsPlayer() {
		double initp = 0.0D;
		String user = "ERROR";
		for (String p : totaldeaths.keySet()) {
			if (((Double) totaldeaths.get(p)).doubleValue() > initp) {
				initp = ((Double) totaldeaths.get(p)).doubleValue();
				user = p;
			}
		}
		return user;
	}

	public static double topScore() {
		return ((Double) totalpoints.get(topScorePlayer())).doubleValue();
	}

	public static double topKills() {
		return ((Double) totalkills.get(topKillsPlayer())).doubleValue();
	}

	public static double topDeaths() {
		return ((Double) totaldeaths.get(topDeathsPlayer())).doubleValue();
	}

	public void saveSConfig() {
		if (SConfig == null || SConfigFile == null) {
			return;
		}
		try {
			getSConfig().save(SConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + SConfigFile, ex);
		}
	}

	public FileConfiguration getSConfig() {
		if (SConfig == null) {
			reloadSConfig();
		}
		return SConfig;
	}

	public void reloadSConfig() {
		if (SConfigFile == null) {
			SConfigFile = new File(this.plugin.getDataFolder(), "stats.yml");
		}
		SConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(SConfigFile);
		File defConfigStream = new File(this.plugin.getDataFolder(), "stats.yml");
		YamlConfiguration.loadConfiguration(defConfigStream);
	}

	public void SaveStats() {
		List<String> players = new ArrayList<>();
		for (String p : getPlayers()) {
			players.add(p);
			getSConfig().set(String.valueOf(p) + ".td", Double.valueOf(getTotalDeaths(p)));
			getSConfig().set(String.valueOf(p) + ".tk", Double.valueOf(getTotalKills(p)));
			getSConfig().set(String.valueOf(p) + ".tp", Double.valueOf(getTotalPoints(p)));
			String bm = String.valueOf(playerclasses.get(p));
			if(playerclasses.get(p) != null) {
				String mr = bm.substring(1, bm.length()-1);
				String mapreplacement = mr.replace("=", "!");
				getSConfig().set(String.valueOf(p) + ".classesunlocked", mapreplacement);
			}
		}
		getSConfig().set("Players", players);
		saveSConfig();
	}

	public void Destroy() {
		try {
			finalize();
		} catch (Throwable e) {
			this.plugin.getLogger().warning("Failed to destroy class");
		}
	}
}