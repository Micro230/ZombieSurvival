package com.bjornke.zombiesurvival;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class pmethods {
	public static String playerGame(Player p) {
		if (p == null) {
			return null;
		}
		if (games.PlayerExists(p.getName())) {
			String map = games.getPlayerMap(p.getName());
			return map;
		}
		return null;
	}

	public static boolean inGame(Player p) {
		if (p == null) {
			return false;
		}
		if (games.PlayerExists(p.getName())) {
			return true;
		}
		return false;
	}

	public static List<String> playersInMap(String map) {
		if (map == null) {
			return null;
		}
		List<String> mapplayers = new ArrayList<>();
		for (String p : games.playermapKeySet()) {
			if (games.getPlayerMap(p).equalsIgnoreCase(map)) {
				mapplayers.add(p);
			}
		}
		return mapplayers;
	}

	public static int onlinepcount(String map) {
		int i = 0;
		for (String mp : playersInMap(map)) {
			Player p = Bukkit.getPlayer(mp);
			if (p != null && !main.dead.containsKey(mp)) {
				i++;
			}
		}
		return i;
	}

	public static int numberInMap(String map) {
		int i = 0;
		for (String mp : playersInMap(map)) {
			i++;
		}
		return i;
	}
}