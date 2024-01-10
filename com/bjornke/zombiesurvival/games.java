package com.bjornke.zombiesurvival;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class games {
	private static Map<String, Integer> pcount = new HashMap<>();
	private static Map<String, Integer> state = new HashMap<>();
	private static Map<String, Integer> zcount = new HashMap<>();
	private static Map<String, Integer> wave = new HashMap<>();
	private static Map<String, Integer> zslayed = new HashMap<>();
	private static Map<String, Integer> maxzombies = new HashMap<>();
	private static Map<String, Integer> maxwaves = new HashMap<>();
	private static Map<String, Integer> teleporteramount = new HashMap<>();
	private static Map<String, Boolean> allfastzombies = new HashMap<>();
	private static Map<String, Integer> maxplayers = new HashMap<>();
	private static Map<String, String> playermap = new HashMap<>();
	private static Map<String, Boolean> mapenabled = new HashMap<>();

	public static void setPcount(String map, int num) {
		pcount.put(map, Integer.valueOf(num));
	}

	public static void setState(String map, int num) {
		state.put(map, Integer.valueOf(num));
	}

	public static void setZcount(String map, int num) {
		zcount.put(map, Integer.valueOf(num));
	}

	public static void setWave(String map, int num) {
		wave.put(map, Integer.valueOf(num));
	}

	public static void setZslayed(String map, int num) {
		zslayed.put(map, Integer.valueOf(num));
	}

	public static void setMaxZombies(String map, int num) {
		maxzombies.put(map, Integer.valueOf(num));
	}

	public static void setMaxPlayers(String map, int num) {
		maxplayers.put(map, Integer.valueOf(num));
	}

	public static void setMaxWaves(String map, int num) {
		maxwaves.put(map, Integer.valueOf(num));
	}
	
	public static void setTeleporterAmount(String map, int num) {
		teleporteramount.put(map, Integer.valueOf(num));
	}
	
	public static void setAllFastZombies(String map, boolean val) {
		allfastzombies.put(map, Boolean.valueOf(val));
	}

	public static void setPlayerMap(String name, String map) {
		playermap.put(name, map);
	}
	
	public static void setMapEnabled(String map, Boolean b) {
		mapenabled.put(map, b);
	}

	public static int getPcount(String map) {
		if (pcount.get(map) != null) {
			return ((Integer) pcount.get(map)).intValue();
		}
		return 0;
	}

	public static int getState(String map) {
		if (state.get(map) != null) {
			return ((Integer) state.get(map)).intValue();
		}
		return 0;
	}

	public static int getZcount(String map) {
		if (zcount.get(map) != null) {
			return ((Integer) zcount.get(map)).intValue();
		}
		return 0;
	}

	public static int getWave(String map) {
		if (wave.get(map) != null) {
			return ((Integer) wave.get(map)).intValue();
		}
		return 0;
	}

	public static int getZslayed(String map) {
		if (zslayed.get(map) != null) {
			return ((Integer) zslayed.get(map)).intValue();
		}
		return 0;
	}

	public static int getMaxZombies(String map) {
		if (maxzombies.get(map) != null) {
			return ((Integer) maxzombies.get(map)).intValue();
		}
		return 0;
	}

	public static int getMaxWaves(String map) {
		if (maxwaves.get(map) != null) {
			return ((Integer) maxwaves.get(map)).intValue();
		}
		return 0;
	}
	
	public static int getTeleporterAmount(String map) {
		if (teleporteramount.get(map) != null) {
			return ((Integer) teleporteramount.get(map)).intValue();
		}
		return 0;
	}
	
	public static boolean getAllFastZombies(String map) {
		if (allfastzombies.get(map) != null) {
			return ((Boolean) allfastzombies.get(map)).booleanValue();
		}
		return false;
	}

	public static int getMaxPlayers(String map) {
		if (maxplayers.get(map) != null) {
			return ((Integer) maxplayers.get(map)).intValue();
		}
		return 0;
	}
	
	public static boolean getMapEnabled(String map) {
		if (maxplayers.get(map) != null) {
			return ((Boolean) mapenabled.get(map)).booleanValue();
		}
		return true;
	}

	public static String getPlayerMap(String name) {
		return playermap.get(name);
	}

	public static void removePlayerMap(String name) {
		playermap.remove(name);
	}

	public static boolean PlayerExists(String name) {
		if (playermap.containsKey(name)) {
			return true;
		}
		return false;
	}

	public static Set<String> playermapKeySet() {
		return playermap.keySet();
	}

	public static boolean exists(String map) {
		if (state.get(map) != null) {
			return true;
		}
		return false;
	}
}
