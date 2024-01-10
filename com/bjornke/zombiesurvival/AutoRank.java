package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AutoRank {
	public Plugin plugin;
	
	private static FileConfiguration ARConfig = null;
	private static File ARConfigFile = null;
	
	public List<String> players = new ArrayList<>();
	public Map<String, Integer> onlineplayers = new HashMap<>();
	public Map<String, Boolean> ignoredplayers = new HashMap<>();
	public static Map<String, String> ranksplayer = new HashMap<>();
	public Map<String, Integer> rankstime = new HashMap<>();
	public static Map<String, Integer> rankslevel = new HashMap<>();
	public Map<String, String> rankscommand = new HashMap<>();
	public String defaultrank;
	
	public void Initalize() {
		defaultrank = getARConfig().getString("Default");
		for (String rks : getARConfig().getStringList("Ranks")) {
			rankslevel.put(rks, getARConfig().getInt(rks + ".ranklevel"));
			rankstime.put(rks, getARConfig().getInt(rks + ".timeneeded"));
			rankscommand.put(rks, getARConfig().getString(rks + ".commandtorun"));
		}
		for (String p : getARConfig().getStringList("Players")) {
			players.add(p);
			ranksplayer.put(p, getARConfig().getString(p + ".rank"));
			ignoredplayers.put(p, getARConfig().getBoolean(p + ".ignored"));
		}
		RunChecker();
	}
	
	public void RunChecker() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				if(onlineplayers.isEmpty() || rankstime.isEmpty()) {
					return;
				}
				for(String p : onlineplayers.keySet()) {
					int ticks = Bukkit.getPlayer(p).getStatistic(Statistic.PLAY_ONE_MINUTE);
					int seconds = ticks/20;
					for(Entry<String, Integer> rs : rankstime.entrySet()) {
						String rank = rs.getKey();
						Integer secondsneed = rs.getValue();
						String currentrank = getPlayerRank(p);
						
						if(currentrank != null && !ignoredplayers.get(p)) {
							if(!rank.equals(currentrank)) {
								if(rankslevel.get(currentrank) < rankslevel.get(rank)) {
									if(seconds >= secondsneed) {
										setPlayerRank(p, rank, ignoredplayers.get(p), rankscommand.get(rank));
									}
								}
							}
						}
					}
				}
			}
		}, 0, 200L);
	}
	
	public void setPlayerRank(String playername, String rank, boolean ignored, String command) {
		ranksplayer.put(playername, rank);
		System.out.println(playername + " has upgraded to rank: " + rank);
		if(command != null) {
			String fixedcommand = command;
			if(command.contains("@p")) {
				fixedcommand = fixedcommand.replace("@p", playername);
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), fixedcommand);
		}
		SaveAutoRank(playername, rank, ignored);
	}
	
	public void setIgnored(String playername, boolean bool) {
		if(players.contains(playername)) {
			ignoredplayers.put(playername, bool);
		}
		else {
			addPlayer(playername);
			ignoredplayers.put(playername, bool);
		}
	}
	
	public String getPlayerRank(String playername) {
		return ranksplayer.get(playername);
	}
	
	public static int getRankLevelByPlayerName(String playername) {
		if(rankslevel.get(ranksplayer.get(playername)) != null) {
			return rankslevel.get(ranksplayer.get(playername));
		}
		return 0;
	}
	
	public void addPlayer(String playername) {
		players.add(playername);
		ranksplayer.put(playername, defaultrank);
		ignoredplayers.put(playername, false);
	}
	
	public void addOnlinePlayer(Player p) {
		onlineplayers.put(p.getName(), p.getStatistic(Statistic.PLAY_ONE_MINUTE));
	}
	
	public void removeOnlinePlayer(Player p) {
		onlineplayers.remove(p.getName());
	}
	
	public void saveARConfig() {
		if (ARConfig == null || ARConfigFile == null) {
			return;
		}
		try {
			getARConfig().save(ARConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + ARConfigFile, ex);
		}
	}

	public FileConfiguration getARConfig() {
		if (ARConfig == null) {
			reloadARConfig();
		}
		return ARConfig;
	}

	public void reloadARConfig() {
		if (ARConfigFile == null) {
			ARConfigFile = new File(this.plugin.getDataFolder(), "autorank.yml");
		}
		ARConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(ARConfigFile);
		File defConfigStream = new File(this.plugin.getDataFolder(), "autorank.yml");
		YamlConfiguration.loadConfiguration(defConfigStream);
	}

	public void SaveAutoRank(String playername, String rank, Boolean ignored) {
		List<String> pls = new ArrayList<>();
		for (String p : players) {
			pls.add(p);
			getARConfig().set(playername + ".rank", rank);
			getARConfig().set(playername + ".ignored", ignored);
		}
		getARConfig().set("Players", pls);
		saveARConfig();
	}
}