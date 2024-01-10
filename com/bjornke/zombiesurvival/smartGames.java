package com.bjornke.zombiesurvival;

public class smartGames {
	public int smartWaveCount(String map) {
		if (!((Boolean)main.wolfwave.get(map)).booleanValue()) {
			int mz = games.getMaxZombies(map);
			int w = games.getWave(map);
			int pre = 1;
			int fin = 1;
			if (mz < 10) {
				pre = (int)((mz * w) * 0.5D);
			}
			if (mz >= 10 && mz <= 50) {
				pre = (int)((mz * w) * 0.1D);
			}
			if (mz >= 51 && mz <= 100) {
				pre = (int)((mz * w) * 0.08D);
			}
			if (mz >= 101 && mz <= 200) {
				pre = (int)((mz * w) * 0.05D);
			}
			if (mz >= 201) {
				pre = (int)((mz * w) * 0.04D);
			}
			if (pre < 1) {
				pre = 1;
			}
			fin = pre * pmethods.numberInMap(map) / games.getMaxPlayers(map);
			if (fin < pre * 0.6D) {
				fin = (int)(pre * 0.6D);
			}
			if (fin < 1) {
				fin = 1;
			}
			return fin;
		}
		else {
			int mz = games.getMaxZombies(map);
			int w = games.getWave(map);
			int pre = 1;
			int fin = 1;
			if (mz < 10) {
				pre = (int)((mz * w) * 0.25D);
			}
			if (mz >= 10 && mz <= 50) {
				pre = (int)((mz * w) * 0.05D);
			}
			if (mz >= 51 && mz <= 100) {
				pre = (int)((mz * w) * 0.04D);
			}
			if (mz >= 101 && mz <= 200) {
				pre = (int)((mz * w) * 0.025D);
			}
			if (mz >= 201) {
				pre = (int)((mz * w) * 0.02D);
			}
			if (pre < 1) {
				pre = 1;
			}
			fin = pre * pmethods.numberInMap(map) / games.getMaxPlayers(map);
			if (fin < pre * 0.3D) {
				fin = (int)(pre * 0.3D);
			}
			if (fin < 1) {
				fin = 1;
			}
			return fin;
		}
	}
}