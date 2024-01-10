package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class language {
	Plugin plugin;
	private FileConfiguration lConfig;
	private File lConfigFile;
	List<String> strings;

	public language(Plugin instance) {
		this.lConfig = null;
		this.lConfigFile = null;
		this.strings = new ArrayList<>(100);
		this.plugin = instance;
	}

	public void LoadLanguage() {
		boolean load = true;
		for (int i = 1; i < 100; i++) {
			this.strings.add(utilities.processForColors(getlConfig().getString(Integer.toString(i))));
		}
	}

	public void savelConfig() {
		if (this.lConfig == null || this.lConfigFile == null) {
			return;
		}
		try {
			getlConfig().save(this.lConfigFile);
		} catch (IOException ex) {
			this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.lConfigFile, ex);
		}
	}

	public FileConfiguration getlConfig() {
		if (this.lConfig == null) {
			reloadlConfig();
		}
		return this.lConfig;
	}

	public void reloadlConfig() {
		if (this.lConfigFile == null) {
			this.lConfigFile = new File(this.plugin.getDataFolder(), "language.yml");
		}
		this.lConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.lConfigFile);
		File defConfigStream = new File(this.plugin.getDataFolder(), "language.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.lConfig.setDefaults((Configuration) defConfig);
		}
	}
}