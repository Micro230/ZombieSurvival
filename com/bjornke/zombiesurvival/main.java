package com.bjornke.zombiesurvival;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.UnknownFormatConversionException;

import org.bukkit.entity.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.attribute.*;
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.text.NumberFormat.Field;
import java.util.Random;
import java.util.concurrent.*;

import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.*;
import net.minecraft.world.entity.EntityCreature;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.goal.PathfinderGoalSelector;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.animal.EntityWolf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import org.bukkit.event.Listener;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftWolf;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftZombieHorse;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;

public class main extends JavaPlugin implements Runnable, Listener {
	public Random random = new Random();
	public main instance;
	public navigationnodes navigationnode = new navigationnodes((Plugin) this);
	public zombieregions navigationregion = new zombieregions((Plugin) this);
	public spawns spawn = new spawns((Plugin) this);
	public barricades bar = new barricades();
	public language lang = new language((Plugin) this);
	public fPerk perk = new fPerk();
	public smartGames sg = new smartGames();
	public doors door = new doors((Plugin) this);
	public revive revive = new revive();
	public classes classes = new classes();
	public stats stats = new stats();
	public AutoRank autorank = new AutoRank();
	public Map<String, Integer> secondkills = new HashMap<>();
	public int onlinep = 0;
	public Map<String, Integer> justleftgame = new HashMap<>();
	public static Map<String, Boolean> wolfwave = new HashMap<>();

	public Map<String, Integer> easycreate = new HashMap<>();
	public Map<String, String> ecname = new HashMap<>();
	public Map<String, Integer> ecpcount = new HashMap<>();
	public Map<String, Integer> ecwcount = new HashMap<>();
	public Map<String, Double> eczcount = new HashMap<>();
	public Map<String, door> playerdoorlink = new HashMap<>();
	public Map<String, Barricade> playerbarricadelink = new HashMap<>();

	public List<String> twomintimer = new ArrayList<>();
	public int startpcount;
	
	public Map<String, Location> plynavposition1 = new HashMap<>();
	public Map<String, Location> plynavposition2 = new HashMap<>();
	public Map<String, Integer> commandwave = new HashMap<>();
	public Map<String, String> commandMap = new HashMap<>();
	public Map<String, String> commandName = new HashMap<>();
	public Map<String, String> commandsubwaycolor = new HashMap<>();
	public Map<String, Integer> commandsubwaynumber = new HashMap<>();
	public Map<String, Boolean> addedneutralnode = new HashMap<>();
	public Map<String, NavigationNode> plyneutralnode = new HashMap<>();
	public Map<Integer, Integer> zombiewasat = new HashMap<>();
	
	public Map<String, Integer> perkcount = new HashMap<>();
	public Map<UUID, CustomPlayerData> cpdata = new HashMap<>();
	public String VERSION = "0.7.0";
	public boolean outofdate = false;
	public boolean antigrief = false;
	public boolean itemsatjoin = false;
	public boolean infectmat = false;
	public boolean spectateallow = false;
	public boolean emptyaccount = true;
	public boolean forcespawn = false;
	public boolean respawn = true;
	public boolean allhurt = true;
	public boolean forceclear = false;
	public boolean invsave = true;
	public int fastzombiestartwave = 3;
	public int tankzombiestartwave = 8;
	public int kingzombiestartwave = 10;
	public double seekspeed = 0.5D;
	public double fastseekspeed = 1.25D;
	public Map<String, Double> mapseekspeed = new HashMap<>();
	public Map<String, Double> mapfastseekspeed = new HashMap<>();
	public Map<String, Integer> mapfastchance = new HashMap<>();
	public Map<String, Integer> maptankchance = new HashMap<>();
	public Map<String, Integer> mapkingchance = new HashMap<>();
	public boolean wolfs = true;
	public boolean healnewwave = true;
	public boolean nvchecker = false;
	public boolean resetpointsdeath = true;
	public boolean jm = true;
	public boolean smartw = true;
	public boolean buydoors = true;
	public int scoreboardmenu = 1;
	public int scoreboardingame = 2;
	public List<String> joincommand = new ArrayList<>();
	public List<String> leavecommand = new ArrayList<>();
	public int cooldown = 120;
	public int vspoke = 0;
	public int runnerchance = 10;
	public int tankerchance = 22;
	public int kingchance = 37;
	public int zombiemaxhealth = 20;
	public int skellywavechance = 0;
	public int wait = 20;
	public int doorfindradius = 6;
	public int leavetimer = 120;
	public int maxpoints = 20;
	public double deathloss = 0.0D;
	public double diffmulti = 0.1D;
	public double damagemulti = 1.0D;
	public int upgradeclassincrease = 150;
	public int upgradeclasscost = 150;
	public int maxzombiesatonce = 20;
	public Map<String, Integer> maxzombiesingame = new HashMap<>();
	public LUAMath luaMath = new LUAMath();
	public MicroCraft Microcraft = new MicroCraft();
	public Map<String, Integer> playerupgradelevel = new HashMap<>();
	public Map<String, Integer> playerupgradeint = new HashMap<>();
	public Map<String, Integer> cooldowncount = new HashMap<>();
	public Map<String, Boolean> power = new HashMap<>();
	public List<Sign> powersigns = new ArrayList<>();
	public Map<String, Location> lightloc = new HashMap<>(10);
	public List<ItemStack> joinitems = new ArrayList<>(5);
	public List<ItemStack> joinarmor = new ArrayList<>(5);
	public Map<ItemStack, Double> drops = new HashMap<>(5);
	public List<ItemStack> boxitems = new ArrayList<>(10);
	public Map<Location, String> roundFire = new HashMap<>();
	public Map<String, Map<Block, BlockState>> changedBlocks = new HashMap<>();
	public Map<String, Map<Block, BlockState>> placedBlocks = new HashMap<>();
	public List<Integer> blockbreak = new ArrayList<>();
	public List<Integer> blockplace = new ArrayList<>();
	public Map<String, pData> playerdata = new HashMap<>();
	public Map<Integer, String> zombies = new HashMap<>(200);
	public Map<Integer, String> fastzombies = new HashMap<>(5);
	public Map<Integer, String> tankzombies = new HashMap<>(5);
	public Map<Integer, String> kingzombies = new HashMap<>(5);
	public Map<Integer, Map<String, Double>> zombiescore = new HashMap<>();
	public static Map<String, String> dead = new ConcurrentHashMap<>();
	public Map<String, Location> deathPoints = new HashMap<>();
	public Map<String, Boolean> perkend = new HashMap<>();
	public static Map<String, String> Maps = new HashMap<>(10);
	public static Map<Integer, String> Machines = new HashMap<>();
	public Set<Location> Signs = new HashSet<>();
	public Map<Location, String> MBoxSigns = new HashMap<>();
	public Map<String, Location> MBoxReset = new HashMap<>();
	public Set<String> voted = new HashSet<>();
	public Map<String, Integer> wavemax = new HashMap<>();
	public Map<String, Integer> runnerwavecount = new HashMap<>();
	public boolean teleporttospawn;
	public Map<String, List<Integer>> doorsbought = new HashMap<>();
	public Map<String, teleporters> teleportlist = new HashMap<>();
	public Map<String, Block> powerswitches = new HashMap<>();
	public Map<String, BlockData> powerswitchdata = new HashMap<>();
	public Map<String, Boolean> repairmap = new HashMap<>();
	public boolean menuscoreboardenabled = true;
	public Map<String, Integer> amountofbarricadesrepaired = new HashMap<>();
	
	public double xSeeDistance;
	public double ySeeDistance;
	public double zSeeDistance;
	public double xHearDistance;
	public double yHearDistance;
	public double zHearDistance;
	boolean doFire;
	boolean chargeCreepers;
	public static main mainfile;
	
	public int maxbarricadestorepair = 20;
	
	public Map<Integer, Boolean> justSpawned = new HashMap<>();
	public Map<Integer, Boolean> persuingNeutral = new HashMap<>();
	public Map<Integer, Spawn> zombiespawnedfrom = new HashMap<>();

	public Map<String, Integer> add = new HashMap<>(2);
	public Map<String, Integer> remove = new HashMap<>(2);
	int task;
	int task2;
	int task3;
	int task4;
	private double PerkBarProgress;
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	private static Field targetSelector;
	Locale bLocale = new Locale("en", "US");

	public void onEnable() {
		this.instance = this;
		try {
			saveDefaultConfig();
			saveResource("language.yml", false);
			saveResource("classes.yml", false);
			registerEvents();
			Machines.put(0, "Quick Trigger");
			Machines.put(1, "Extra Health");
			Machines.put(2, "Quick Revive");
			Machines.put(3, "Fast Reload");
			Machines.put(4, "Fast Melee");
			Machines.put(5, "Health Regen");
			this.lang.LoadLanguage();
			this.autorank.plugin = (Plugin) this;
			this.stats.plugin = (Plugin) this;
			this.classes.plugin = (Plugin) this;
			this.door.plugin = (Plugin) this;
			this.navigationnode.plugin = (Plugin) this;
			this.spawn.plugin = (Plugin) this;
			this.revive.plugin = (Plugin) this;
			this.bar.plugin = (Plugin) this;
			this.classes.LoadClasses();
			this.stats.LoadStats();
			this.autorank.Initalize();
			this.task = scheduleTask(this, 20L, 20L);
			new ShootDelayListener(this);
			updateScoreboardMenu();
			reloadPlayers();
			QuickUpdate();
			checkMobs();
			AsynchTasks();
			synchTasks();
			this.revive.counterTask();
			dealDamage();
			LoadConfig();
			getLogger().info(ChatColor.GREEN + "ZombieSurvival Enabled!");

		} catch (Exception e) {
			e.printStackTrace();
			getLogger().severe(ChatColor.RED + "ZombieSurvival failed to start correctly! Disabling!");
			getLogger().severe(e.toString());
			Bukkit.getPluginManager().disablePlugin((Plugin) this);
		}
		
		if(this.nvchecker) {
			new UpdateChecker(this, 12345).getVersion(version -> {
	            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
	            	getLogger().info("You have the current version of ZombieSurvival");
	            } else {
	            	getLogger().info(ChatColor.RED + "New update available!");
	            }
	        });
		}
	}

	public void onDisable() {
		for (String map : Maps.keySet()) {
			GamesOver(map, Boolean.valueOf(true));
		}
		this.autorank.saveARConfig();
		getServer().getScheduler().cancelTasks((Plugin) this);
		this.door.saveDoor();
		this.navigationregion.saveRegions();
		this.navigationnode.saveNodeSpawn();
		this.spawn.saveSpawn();
		this.bar.saveBar();
		saveSigns();
		this.bar.Destroy();
		this.door.Destroy();
		this.navigationregion.Destroy();
		this.navigationnode.Destroy();
		this.spawn.Destroy();
		this.revive.Destroy();
		getLogger().info("ZombieSurvival Disabled!");
	}

	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, (Plugin) this);
		pm.registerEvents(new signs(), (Plugin) this);
		pm.registerEvents(this.perk, (Plugin) this);
		pm.registerEvents(new spectate(), (Plugin) this);
		pm.registerEvents(this.revive, (Plugin) this);
		pm.registerEvents(new donator(), (Plugin) this);
		pm.registerEvents(new LocalThunderHandler(), (Plugin)this);
	}

	public void LoadConfig() {
		this.spawn.loadSpawn();
		this.door.loadDoor(this.spawn.getAllSpawns());
		this.bar.loadBar(this.spawn.getAllSpawns());
		this.navigationregion.loadRegions();
		this.navigationnode.loadNodeSpawn();
		saveDefaultConfig();
		
		this.xSeeDistance = getConfig().getDouble("X-See-Distance");
		this.ySeeDistance = getConfig().getDouble("Y-See-Distance");
		this.zSeeDistance = getConfig().getDouble("Z-See-Distance"); 
		this.xHearDistance = getConfig().getDouble("X-Hear-Distance");
		this.yHearDistance = getConfig().getDouble("Y-Hear-Distance");
		this.zHearDistance = getConfig().getDouble("Z-Hear-Distance");
		this.doFire = getConfig().getBoolean("Do-Fire");
		this.chargeCreepers = getConfig().getBoolean("Charge-Creepers");
		
		mainfile = this;
		
		List<String> worldnames = new ArrayList<>();
		for (World world2 : this.getServer().getWorlds()) {//Bukkit.getWorlds()
			worldnames.add(world2.getName());
		}
		for (String w : worldnames) {
			for (String s : getCustomConfig().getStringList(String.valueOf(w) + ".maps")) {
				Maps.put(s, w);
			}
		}
		for (String str2 : getCustomConfig().getStringList("signs")) {
			if (parseToLoc(str2) != null) {
				this.Signs.add(parseToLoc(str2));
			}
		}
		saveSigns();
		for (String str : Maps.keySet()) {
			for (String str2 : getCustomConfig()
					.getStringList(String.valueOf(Maps.get(str)) + "." + str + ".roundfire")) {
				this.roundFire.put(parseToLoc(str2), str);
			}
			for (String str3 : getCustomConfig().getStringList(String.valueOf(Maps.get(str)) + "." + str + ".mboxsigns")) {
				this.MBoxSigns.put(parseToLoc(str3), str);
			}
			this.deathPoints.put(str, parseToLoc(getCustomConfig().getString(String.valueOf(Maps.get(str)) + "." + str + ".waiting")));
			this.spawn.lobbies.put(str, parseToLoc(getCustomConfig().getString(String.valueOf(Maps.get(str)) + "." + str + ".lobby")));
			this.spawn.spectate.put(str, parseToLoc(getCustomConfig().getString(String.valueOf(Maps.get(str)) + "." + str + ".spectate")));
			this.lightloc.put(str, parseToLoc(getCustomConfig().getString(String.valueOf(Maps.get(str)) + "." + str + ".lightning")));
			games.setMaxZombies(str, getCustomConfig().getInt(String.valueOf(Maps.get(str)) + "." + str + ".maxzombies"));
			games.setMaxPlayers(str, getCustomConfig().getInt(String.valueOf(Maps.get(str)) + "." + str + ".maxplayers"));
			games.setMaxWaves(str, getCustomConfig().getInt(String.valueOf(Maps.get(str)) + "." + str + ".maxwaves"));
			games.setTeleporterAmount(str, getCustomConfig().getInt(String.valueOf(Maps.get(str)) + "." + str + ".teleporteramount"));
			games.setAllFastZombies(str, getCustomConfig().getBoolean(String.valueOf(Maps.get(str)) + "." + str + ".allfastzombies"));
			games.setMapEnabled(str, getCustomConfig().getBoolean(String.valueOf(Maps.get(str)) + "." + str + ".enabled"));
			games.setState(str, 1);
			games.setPcount(str, 0);
			this.perk.setPerk(str, 0);
			this.perkcount.put(str, Integer.valueOf(0));
			this.changedBlocks.put(str, new HashMap<>());
			this.placedBlocks.put(str, new HashMap<>());
			this.perkend.put(str, Boolean.valueOf(true));
			games.setWave(str, 0);
			games.setZcount(str, 0);
		}
		for (String it : getConfig().getStringList("join-items")) {
			try {
				String[] line = it.split(":");
				Material itemid = Material.getMaterial(line[0]);
				int amount = 1;
				if (line.length > 1) {
					amount = Integer.parseInt(line[1]);
				}
				ItemStack item = new ItemStack(itemid, amount);
				this.joinitems.add(item);
			} catch (Exception e) {
				getLogger().warning("Could not load item config (join-items): " + it);
			}
		}
		for (String it : getConfig().getStringList("armor-items")) {
			try {
				if (!it.contentEquals("0")) {
					Material itemid = Material.getMaterial(it);
					ItemStack item = new ItemStack(itemid, 1);
					this.joinarmor.add(item);
				}
			} catch (Exception e) {
				getLogger().warning("Could not load item config (armor-items): " + it);
			}
		}
		for (String it : getConfig().getStringList("mysterybox-items")) {
			try {
				String[] line = it.split(":");
				Material itemid = Material.getMaterial(line[0]);
				ItemStack item = new ItemStack(itemid, 1);
				this.boxitems.add(item);
			} catch (Exception e) {
				getLogger().warning("Could not load item config (mysterybox-items): " + it);
			}
		}
		for (String it : getConfig().getStringList("drop-items")) {
			try {
				if (!it.contentEquals("0")) {
					String[] line = it.split(":");
					Material itemid = Material.getMaterial(line[0]);
					double chance = 0.5D;
					if (line.length > 1) {
						chance = Double.parseDouble(line[1]);
					}
					ItemStack item = new ItemStack(itemid, 1);
					this.drops.put(item, Double.valueOf(chance));
				}
			} catch (Exception e) {
				getLogger().warning("Could not load item config (drop-items): " + it);
			}
		}
		this.blockbreak = getConfig().getIntegerList("allowbreak");
		this.blockplace = getConfig().getIntegerList("allowplace");
		this.startpcount = getConfig().getInt("start-player-count");
		this.antigrief = getConfig().getBoolean("auto-anti-grief");
		this.itemsatjoin = getConfig().getBoolean("items-at-join");
		this.cooldown = getConfig().getInt("auto-cooldown");
		this.deathloss = getConfig().getDouble("death-loss-percent");
		this.diffmulti = getConfig().getDouble("health-multi");
		this.damagemulti = getConfig().getDouble("damage-multi");
		this.infectmat = getConfig().getBoolean("infect-mat");
		this.spectateallow = getConfig().getBoolean("allow-spectate");
		this.wait = getConfig().getInt("wave-wait") * 20;
		this.emptyaccount = getConfig().getBoolean("empty-account");
		this.forcespawn = getConfig().getBoolean("force-spawn");
		this.seekspeed = getConfig().getDouble("seek-speed");
		this.fastseekspeed = getConfig().getDouble("fast-seek-speed");
		this.doorfindradius = getConfig().getInt("buy-door-find-radius");
		this.respawn = getConfig().getBoolean("death-non-human-respawn");
		this.allhurt = getConfig().getBoolean("all-hurt");
		this.leavetimer = getConfig().getInt("leave-timer");
		this.forceclear = getConfig().getBoolean("inventory-clear-join");
		this.invsave = getConfig().getBoolean("inventory-save");
		this.healnewwave = getConfig().getBoolean("heal-player-new-wave");
		this.skellywavechance = getConfig().getInt("wolf-wave-chance");
		this.joincommand = getConfig().getStringList("join-commands");
		this.leavecommand = getConfig().getStringList("leave-commands");
		this.nvchecker = getConfig().getBoolean("new-version-checking");
		this.maxpoints = getConfig().getInt("max-points-per-kill");
		this.resetpointsdeath = getConfig().getBoolean("reset-points-on-death");
		this.jm = getConfig().getBoolean("join-message");
		this.smartw = getConfig().getBoolean("use-smart-waves");
		this.buydoors = getConfig().getBoolean("use-buyable-doors");
		this.upgradeclassincrease = getConfig().getInt("upgrade-class-increase");
		this.upgradeclasscost = getConfig().getInt("upgrade-class-cost");
		this.teleporttospawn = getConfig().getBoolean("teleport-to-spawn");
		this.maxzombiesatonce = getConfig().getInt("max-zombies-at-once");
		this.zombiemaxhealth = getConfig().getInt("zombie-max-health");
		this.menuscoreboardenabled = getConfig().getBoolean("menu-scoreboard-enabled");
		this.maxbarricadestorepair = getConfig().getInt("max-barricades-to-repair");
		this.runnerchance = getConfig().getInt("fast-zombie-spawn-chance");
		this.tankerchance = getConfig().getInt("tank-zombie-spawn-chance");
		this.kingchance = getConfig().getInt("king-zombie-spawn-chance");
		this.fastzombiestartwave = getConfig().getInt("fast-zombie-start-wave");
		this.tankzombiestartwave = getConfig().getInt("tank-zombie-start-wave");
		this.kingzombiestartwave = getConfig().getInt("king-zombie-start-wave");

		saveCustomConfig();
	}

	@EventHandler
	public void CommandListener(PlayerCommandPreprocessEvent e) {
		String message = e.getMessage().substring(1);
		String[] command = message.split(" ");
		if (this.joincommand.contains(command[0])) {
			if (command.length > 1) {
				e.getPlayer().performCommand("bsapj " + command[1]);
			} else {
				e.getPlayer().performCommand("bsapj ");
			}
			e.setCancelled(true);
		} else if (this.leavecommand.contains(command[0])) {
			e.getPlayer().performCommand("bsapl");
			e.setCancelled(true);
		}
	}
	
	static class ShootDelayListener implements Listener {
		
		private final Plugin plugin;
		
		public ShootDelayListener(Plugin plugin) {
			this.plugin = plugin;
			this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		}
	}

	@EventHandler
	public void foodChangeEvent(FoodLevelChangeEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) event.getEntity();
			event.setCancelled(true);
			if (player.getFoodLevel() <= 19.0D) {
				player.setFoodLevel(20);
			}
		}
	}

	public void reload() {
		for (String str : Maps.keySet()) {
			GamesOver(str, Boolean.valueOf(true));
		}
		this.lightloc.clear();
		this.blockbreak.clear();
		this.blockplace.clear();
		this.roundFire.clear();
		this.door.doors.clear();
		this.add.clear();
		this.remove.clear();
		this.changedBlocks.clear();
		this.placedBlocks.clear();
		this.zombies.clear();
		this.perkcount.clear();
		this.MBoxSigns.clear();
		this.MBoxReset.clear();
		
		//These might go in the reloadConfig function but it doesn't exist and I didn't google documentation
		this.xSeeDistance = getConfig().getDouble("X-See-Distance");
		this.ySeeDistance = getConfig().getDouble("Y-See-Distance");
		this.zSeeDistance = getConfig().getDouble("Z-See-Distance");        
		this.xHearDistance = getConfig().getDouble("X-Hear-Distance");
		this.yHearDistance = getConfig().getDouble("Y-Hear-Distance");
		this.zHearDistance = getConfig().getDouble("Z-Hear-Distance");
		this.doFire = getConfig().getBoolean("Do-Fire");
		this.chargeCreepers = getConfig().getBoolean("Charge-Creepers");
		
		dead.clear();
		reloadConfig();
		reloadCustomConfig();
		LoadConfig();
		reloadPlayers();
	}

	public void run() {
		for (String map : Maps.keySet()) {
			World world = Bukkit.getWorld(Maps.get(map));
			if (games.getState(map) == 2 && games.getZcount(map) < ((Integer) this.wavemax.get(map)).intValue()) {
				if(this.maxzombiesingame.get(map) < maxzombiesatonce) {
					Entity ent;
					if (((Boolean) wolfwave.get(map)).booleanValue()) {
						ent = world.spawnEntity(this.spawn.spawn(map, games.getWave(map),this.buydoors), EntityType.WOLF);
						Wolf w = (Wolf) ent;
						w.setAngry(true);
					} else {
						Location spawnloc = this.spawn.spawn(map, games.getWave(map), this.buydoors);
						ent = world.spawnEntity(spawnloc, EntityType.ZOMBIE);// Spawn Zombie
						this.justSpawned.put(ent.getEntityId(), true);
						this.zombiewasat.put(ent.getEntityId(), 0);
						this.persuingNeutral.put(ent.getEntityId(), false);
						this.maxzombiesingame.put(map, this.maxzombiesingame.get(map) + 1);
						this.zombiespawnedfrom.put(ent.getEntityId(), this.spawn.findSpawn(spawnloc.getX(),spawnloc.getY(),spawnloc.getZ()));
						Zombie z = (Zombie) ent;
						z.setAdult();
					}
					LivingEntity lent = (LivingEntity) ent;
					lent.getEquipment().clear();
					if (this.diffmulti != 0.0D) {
						int health = (int) (games.getWave(map) * this.diffmulti);
						AttributeInstance healthAttribute = lent.getAttribute(Attribute.GENERIC_MAX_HEALTH);
						if(health > this.zombiemaxhealth) {
							health = this.zombiemaxhealth;
						}
						healthAttribute.setBaseValue(3 + health);
						lent.setHealth(3 + health);
						lent.setRemoveWhenFarAway(false);
					}
					//FAST ZOMBIES
					if(games.getAllFastZombies(map)) {
						this.fastzombies.put(Integer.valueOf(ent.getEntityId()), map);
					}
					if (this.mapfastchance.get(map) != 0) {
						if(games.getWave(map) >= this.fastzombiestartwave) {
							int go = this.random.nextInt(this.mapfastchance.get(map)) + 1;
							if (go == 1 || go == 2) {
								this.fastzombies.put(Integer.valueOf(ent.getEntityId()), map);
							}
						}
					}
					//TANK ZOMBIES
					if (this.maptankchance.get(map) != 0 && games.getWave(map) >= this.tankzombiestartwave && !this.fastzombies.containsKey(ent.getEntityId())) {
						int go = this.random.nextInt(this.mapfastchance.get(map)) + 1;
						if (go == 1) {
							this.tankzombies.put(Integer.valueOf(ent.getEntityId()), map);
							lent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
							lent.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
							lent.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
							lent.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
							lent.getEquipment().setHelmetDropChance(0.1f);
							lent.getEquipment().setChestplateDropChance(0.1f);
							lent.getEquipment().setLeggingsDropChance(0.1f);
							lent.getEquipment().setBootsDropChance(0.1f);
						}
					}
					//KING ZOMBIES
					if (this.mapkingchance.get(map) != 0 && games.getWave(map) >= this.kingzombiestartwave) {
						int go = this.random.nextInt(this.mapfastchance.get(map)) + 1;
						if (go == 1) {
							this.kingzombies.put(Integer.valueOf(ent.getEntityId()), map);
							lent.getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
							lent.getEquipment().setHelmetDropChance(0);
						}
					}
					this.zombies.put(Integer.valueOf(ent.getEntityId()), map);
					games.setZcount(map, games.getZcount(map) + 1);
				}
			}
			if (this.perk.getPerk(map) > 0 && ((Integer) this.perkcount.get(map)).intValue() < 30) { //Perk Timer
				this.PerkBarProgress = (Math.abs((this.perkcount.get(map).doubleValue() / 30) - 1));
				fPerk.PerkProgress(PerkBarProgress);
				this.perkcount.put(map, Integer.valueOf(((Integer) this.perkcount.get(map)).intValue() + 1));
				continue;
			}
			if (!((Boolean) this.perkend.get(map)).booleanValue() && !this.perk.isDropped) {// Shut Perk Off
				for (String str : pmethods.playersInMap(map)) {
					Player p = Bukkit.getPlayer(str);
					fPerk.ClearPerk(p);
				}
				this.perkend.put(map, Boolean.valueOf(true));
			}
			this.perk.setPerk(map, 0);
			this.perkcount.put(map, Integer.valueOf(0));
		}
	}
	
	public void killUnloadedZombies(String map, World world) {
		List<LivingEntity> entsinworld = world.getLivingEntities();
		
		List<Integer> safeids = new ArrayList<>();
		for(Entry<Integer, String> zentry : this.zombies.entrySet()) {
			for(Entity we : entsinworld) {
				if(we instanceof CraftZombie) {
					if(we.getEntityId() == zentry.getKey()) {
						safeids.add(we.getEntityId());
					}
				}
			}
		}
		for(Entity we : entsinworld) {
			if(!safeids.contains(we.getEntityId())){
				if(we instanceof CraftZombie) {
					we.remove();
					System.out.println("Zombie with broken ID in chunk unload was removed and will respawn elsewhere.");
				}
			}
		}
	}

	public void Games(String map, Boolean force) {//StartGame GamesStart Game Start Games Start
		World world = Bukkit.getWorld(Maps.get(map));
		if (games.getPcount(map) >= this.startpcount || force.booleanValue()) {
			Bukkit.getScheduler().cancelTask(task4);//Stop the rundamagebarricades task if it is running
			RunDamageHealBarricades();
			killUnloadedZombies(map, world);
			games.setState(map, 2);
			games.setWave(map, 1);
			games.setZslayed(map, 0);
			games.setZcount(map, 0);
			maxzombiesingame.put(map, 0);
			doorsbought.put(map, new ArrayList<Integer>());
			mapseekspeed.put(map, this.seekspeed);
			mapfastseekspeed.put(map, this.fastseekspeed);
			mapfastchance.put(map, this.runnerchance);
			maptankchance.put(map, this.tankerchance);
			mapkingchance.put(map, this.kingchance);
			teleportlist.put(map, new teleporters(games.getTeleporterAmount(map)));
			power.put(map, false);
			wolfwave.put(map, Boolean.valueOf(false));
			if (this.smartw) {
				this.wavemax.put(map, Integer.valueOf(this.sg.smartWaveCount(map)));
			} else {
				maxfinder(map);
			}
			String health = Integer.toString(4 + (int) (games.getWave(map) * this.diffmulti));
			String zombmax = Integer.toString(((Integer) this.wavemax.get(map)).intValue());
			if (this.lightloc.get(map) != null) {
				world.strikeLightningEffect(this.lightloc.get(map));
			}
			getLogger().info(String.valueOf(this.lang.strings.get(0)) + ": " + map);
			for (String mp : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(mp);
				if (p != null) {
					p.teleport(this.spawn.lobbies.get(map));
					p.setAllowFlight(false);
					p.setFlying(false);
					p.setGameMode(GameMode.SURVIVAL);
					CreateScoreboard(p);
					p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
					p.setFoodLevel(20);
					p.setLevel(0);
					p.setExp(0);
					p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
					p.getInventory().setHeldItemSlot(0);
					this.playerupgradelevel.put(p.getName(), 1);
					this.playerupgradeint.put(p.getName(), upgradeclasscost);
					p.setPlayerListName("§2[In-Game: " + map + "] §r" + p.getDisplayName());
					for (PotionEffect effect : p.getActivePotionEffects()) {
						p.removePotionEffect(effect.getType());
					}
					dead.remove(p.getName());
					joinItems(p);
					classes.setupPlayer(p, cpdata.get(p.getUniqueId()));
					p.sendMessage(this.lang.strings.get(2));
					p.sendMessage(this.lang.strings.get(3));
					p.sendMessage(ChatColor.DARK_RED + zombmax + " " + (String) this.lang.strings.get(4) + " "
							+ ChatColor.DARK_RED + health + " " + (String) this.lang.strings.get(5));
				}
			}
		} else {
			for (String mp : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(mp);
				if (p != null) {
					p.sendMessage(this.lang.strings.get(6));
				}
			}
		}
	}

	public void placeInGame(Player p, String map, boolean wave) {
		getLogger().info(String.valueOf(map) + ": from PlaceInGame()");
		utilities.unhidePlayer(p);
		p.teleport(this.spawn.lobbies.get(map));
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setGameMode(GameMode.SURVIVAL);
		p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		p.setFoodLevel(20);
		joinItems(p);
		CreateScoreboard(p);
		p.setLevel(0);
		p.setExp(0);
		p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
		p.getInventory().setHeldItemSlot(0);
		p.setPlayerListName("§2[In-Game: " + map + "] " + p.getDisplayName());
		cpdata.get(p.getUniqueId()).resetMachines();
		this.playerupgradelevel.put(p.getName(), 1);
		this.playerupgradeint.put(p.getName(), upgradeclasscost);
		for (PotionEffect effect : p.getActivePotionEffects()) {
			p.removePotionEffect(effect.getType());
		}
		classes.setupPlayer(p, cpdata.get(p.getUniqueId()));
		if (!wave) {
			p.sendMessage(this.lang.strings.get(2));
		}
	}
	
	public void CreateScoreboard(Player p) {
		p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());		
    	ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        if(board.getObjective("showstats") != null) {
        	board.getObjective("showstats").unregister();
        }
        final Objective objective = board.registerNewObjective("showstats", "stats");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    	if(pmethods.inGame(p)) {
            objective.setDisplayName(ChatColor.RED + "§l§n" + pmethods.playerGame(p) + " Stats");
            p.setScoreboard(board);
            updateScoreboardInGame(p, games.getPlayerMap(p.getName()), 0);
    	}
    	else if(menuscoreboardenabled) {
            objective.setDisplayName(ChatColor.RED + "§l§nTop Players");
            p.setScoreboard(board);
            updateScoreboardMenu();
    	}
	}
	
	public void updateScoreboardInGame(Player p, String map, int update) {
		Objective objective = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
		Score scorewaves = objective.getScore("Current Wave: " + "§2" + + (int)games.getWave(map));
        Score scoreremaining = objective.getScore("Zombies Left: " + "§2" + (int)(wavemax.get(map) - games.getZslayed(map)));
        Score scorekills = objective.getScore("Kills: " + "§2" + + (int)stats.getSesKills(p.getName()));
        Score scorepoints = objective.getScore("Points: " + "§2" + + (int)stats.getSesPoints(p.getName()));
		if(pmethods.inGame(p)) {
            if(games.getState(map) != 1) {
				switch(update) {
				case 0:
	                scorewaves = objective.getScore("Current Wave: " + "§2" + + (int)games.getWave(map));
	                scorewaves.setScore(4);
	                scoreremaining = objective.getScore("Zombies Left: " + "§2" + (int)(wavemax.get(map) - games.getZslayed(map)));
	                scoreremaining.setScore(3);
		            scorekills = objective.getScore("Kills: " + "§2" + + (int)stats.getSesKills(p.getName()));
		            scorekills.setScore(2);
		            scorepoints = objective.getScore("Points: " + "§2" + + (int)stats.getSesPoints(p.getName()));
		            scorepoints.setScore(1);
		            break;
				case 1:
					for(String i : p.getScoreboard().getEntries()) {
						if(i.contains("Current Wave")) {
							p.getScoreboard().resetScores(i);
						}
					}
					scorewaves = objective.getScore("Current Wave: " + "§2" + + (int)games.getWave(map));
					scorewaves.setScore(4);
	                break;
				case 2:
					for(String i : p.getScoreboard().getEntries()) {
						if(i.contains("Zombies Left")) {
							p.getScoreboard().resetScores(i);
						}
					}
					scoreremaining = objective.getScore("Zombies Left: " + "§2" + (int)(wavemax.get(map) - games.getZslayed(map)));
	                scoreremaining.setScore(3);
	                break;
				case 3:
					for(String i : p.getScoreboard().getEntries()) {
						if(i.contains("Kills")) {
							p.getScoreboard().resetScores(i);
						}
					}
					scorekills = objective.getScore("Kills: " + "§2" + + (int)stats.getSesKills(p.getName()));
		            scorekills.setScore(2);
		            break;
				case 4:
					for(String i : p.getScoreboard().getEntries()) {
						if(i.contains("Points")) {
							p.getScoreboard().resetScores(i);
						}
					}
					scorepoints = objective.getScore("Points: " + "§2" + + (int)stats.getSesPoints(p.getName()));
		            scorepoints.setScore(1);
		            break;
				}
            }
		}
	}
	
	public void updateScoreboardMenu() {
    	for(Player p : Bukkit.getOnlinePlayers()) {
        	String map = pmethods.playerGame(p);
            Objective objective = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
            try {
            	for(String e : p.getScoreboard().getEntries()) {
        			p.getScoreboard().resetScores(e);
        		}
            	if(!pmethods.inGame(p) && menuscoreboardenabled) {
	                if (stats.leaderboardPlayer(0) != "Empty") {
		                Score player1 = objective.getScore(stats.leaderboardPlayer(0));
		                player1.setScore(stats.leaderboardKill(stats.leaderboardPlayer(0)).intValue());
	                }
	                if (stats.leaderboardPlayer(1) != "Empty") {
	                	Score player2 = objective.getScore(stats.leaderboardPlayer(1));
		                player2.setScore(stats.leaderboardKill(stats.leaderboardPlayer(1)).intValue());
	                }
	                if (stats.leaderboardPlayer(2) != "Empty") {
	                	Score player3 = objective.getScore(stats.leaderboardPlayer(2));
		                player3.setScore(stats.leaderboardKill(stats.leaderboardPlayer(2)).intValue());
	                }
	                if (stats.leaderboardPlayer(3) != "Empty") {
	                	Score player4 = objective.getScore(stats.leaderboardPlayer(3));
		                player4.setScore(stats.leaderboardKill(stats.leaderboardPlayer(3)).intValue());
	                }
	                if (stats.leaderboardPlayer(4) != "Empty") {
	                	Score player5 = objective.getScore(stats.leaderboardPlayer(4));
		                player5.setScore(stats.leaderboardKill(stats.leaderboardPlayer(4)).intValue());
	                }
	                if (stats.leaderboardPlayer(5) != "Empty") {
	                	Score player6 = objective.getScore(stats.leaderboardPlayer(5));
		                player6.setScore(stats.leaderboardKill(stats.leaderboardPlayer(5)).intValue());
	                }
	                if (stats.leaderboardPlayer(6) != "Empty") {
	                	Score player7 = objective.getScore(stats.leaderboardPlayer(6));
		                player7.setScore(stats.leaderboardKill(stats.leaderboardPlayer(6)).intValue());
	                }
	                if (stats.leaderboardPlayer(7) != "Empty") {
	                	Score player8 = objective.getScore(stats.leaderboardPlayer(7));
		                player8.setScore(stats.leaderboardKill(stats.leaderboardPlayer(7)).intValue());
	                }
	                if (stats.leaderboardPlayer(8) != "Empty") {
	                	Score player9 = objective.getScore(stats.leaderboardPlayer(8));
		                player9.setScore(stats.leaderboardKill(stats.leaderboardPlayer(8)).intValue());
	                }
	                if (stats.leaderboardPlayer(9) != "Empty") {
	                	Score player10 = objective.getScore(stats.leaderboardPlayer(9));
		                player10.setScore(stats.leaderboardKill(stats.leaderboardPlayer(9)).intValue());
	                }
            	}
            }
            catch(Exception e) {
            	e.printStackTrace();
            }
    	}
    }

	public void GamesOver(String map, Boolean force) {//GamesEnd Games End Game End
		World world = Bukkit.getWorld(Maps.get(map));
		if (pmethods.onlinepcount(map) < 1 || force.booleanValue()) {
			resetBlocks(map);
			utilities.clearDrops(map);
			resetDoors(map);
			this.stats.SaveStats();
			if(!this.MBoxReset.isEmpty()) {
				Block shouldbesign = world.getBlockAt(this.MBoxReset.get(map));
				if (shouldbesign.getState() instanceof Sign) {
					Sign sign = (Sign) shouldbesign.getState();
					sign.setLine(2, "§2§lACTIVE");
					sign.update();
				}
				for (Map.Entry<Location, String> strloc : this.MBoxSigns.entrySet()) {
					if(!shouldbesign.equals(world.getBlockAt(strloc.getKey()))) {
						if(strloc.getValue().equals(map)) {
							Sign sign2 = (Sign) world.getBlockAt(strloc.getKey()).getState();
							sign2.setLine(2, "§4§lINACTIVE");
							sign2.update();
						}
					}
				}
			}
			this.MBoxReset.remove(map);
			games.setState(map, 1);
			games.setWave(map, 0);
			games.setPcount(map, 0);
			teleportlist.remove(map);
			this.doorsbought.remove(map);
			this.bar.resetBars(map);
			this.vspoke = 0;
			this.wavemax.put(map, null);
			getLogger().info(String.valueOf(this.lang.strings.get(1)) + ": " + map);
			List<LivingEntity> templist = getLivingEnts(world);
			for (LivingEntity went : templist) {
				if (went instanceof Player) {
					
				}
				for (Iterator<Integer> it = this.zombies.keySet().iterator(); it.hasNext();) {
					int id = ((Integer) it.next()).intValue();
					if (((String) this.zombies.get(Integer.valueOf(id))).equalsIgnoreCase(map)
							&& went.getEntityId() == id) {
						if (went.isValid()) {
							went.remove();
							this.bar.stopzombiebreaking(Integer.valueOf(went.getEntityId()));//If barricades are fixed then delete this comment
						}
						it.remove();
					}
				}
			}
			for(Map.Entry<String,Block> powerswitch : this.powerswitches.entrySet()) {
				if(map == powerswitch.getKey()) {
					
					Block block = powerswitch.getValue();//Flip Switch
					Powerable powerdata = (Powerable)this.powerswitchdata.get(map);
					powerdata.setPowered(false);
					block.setBlockData(powerdata);
					block.getState().update(true,true);
					
					Switch bswitch = (Switch) block.getBlockData();//Replace block behind switch
                    BlockFace face = bswitch.getFacing();
                    Block blockbehind = block.getRelative(face.getOppositeFace());
                    Material orig = blockbehind.getType();
            		blockbehind.setType(Material.STONE);
            		blockbehind.setType(orig);
				}
			}
			this.powerswitches.remove(map);
			for (String mp : pmethods.playersInMap(map)) {				
				games.removePlayerMap(mp);
				stats.setPoints(mp, 0.0D);
				stats.setDeaths(mp, 0.0D);
				stats.removeSplayerKills(mp);
				dead.remove(mp);
				spectate.spectators.remove(mp);
				Player p = Bukkit.getPlayer(mp);
				CreateScoreboard(p);
				this.revive.removeSign(mp);
				if (p != null) {
					if(this.teleporttospawn) {
						p.teleport(p.getWorld().getSpawnLocation());
					}
					else {
						p.teleport(((pData) this.playerdata.get(mp)).location);
					}
					p.setFlying(false);
					p.setGameMode(((pData) this.playerdata.get(mp)).gamemode);
					p.setHealth(((pData) this.playerdata.get(mp)).health);
					p.setFoodLevel(((pData) this.playerdata.get(mp)).food);
					if (this.invsave) {
						p.getInventory().setContents(((pData) this.playerdata.get(mp)).inventory);
						p.getInventory().setArmorContents(((pData) this.playerdata.get(mp)).armor);
					}
					this.playerdata.remove(mp);
					this.cpdata.remove(p.getUniqueId());
					p.setPlayerListName("§3[Lobby] §r" + p.getDisplayName());
					utilities.unhidePlayer(p);
					fPerk.ClearPerk(p);
					p.sendMessage(this.lang.strings.get(7));
				}
			}
		}
	}

	public Location parseToLoc(String str) throws NumberFormatException {
		if (str == null) {
			return null;
		}
		String[] strs = str.split(" ");
		double xl = Double.parseDouble(strs[0]);
		double yl = Double.parseDouble(strs[1]);
		double zl = Double.parseDouble(strs[2]);
		World worldl = Bukkit.getServer().getWorld(strs[3]);
		Location parsedLoc = new Location(worldl, xl, yl, zl);
		return parsedLoc;
	}

	public String parseToStr(Location parseloc) {
		return String.format(this.bLocale, "%.2f %.2f %.2f %s", new Object[] { Double.valueOf(parseloc.getX()),
				Double.valueOf(parseloc.getY()), Double.valueOf(parseloc.getZ()), parseloc.getWorld().getName() });
	}

	public int scheduleTask(Runnable runnable, long initial, long delay) {
		return Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this.instance, runnable, initial, delay);
	}
	
	@EventHandler
	public void onCommandBlockDispatch(ServerCommandEvent e) {
		if (e.getSender() instanceof BlockCommandSender) {
			String cmd = e.getCommand();
			if (cmd.startsWith("/")) {
				cmd = cmd.replaceFirst("/", "");
			}
			if (cmd.startsWith("minecraft:")) {
				return;
			}
			String[] args = cmd.split(" ");
			String finalcommand = cmd;
			for (String arg : args) {
				if (arg.startsWith("@")) {
					switch (arg.substring(0, 2)) {
					case "@a":
					case "@r":
					case "@s":
					case "@e":
					case "@p":
						Player considered = null;
						Location cmdblockloc = ((BlockCommandSender)e.getSender()).getBlock().getLocation();						for(Player p : Bukkit.getOnlinePlayers()) {
							if(considered == null) {
								considered = p;
							}
							else if(p.getLocation().distance(cmdblockloc) < considered.getLocation().distance(cmdblockloc)) {
								considered = p;
							}
						}
						finalcommand = finalcommand.replace(arg,considered.getName());
						break;
					default:
						return;
					}
				}
			}
			Bukkit.dispatchCommand(e.getSender(), finalcommand);
			e.setCancelled(true);
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("zsdebug")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				zsDebug(1, player);
				return true;
			}
			zsDebug(0, (Player) null);
			return true;
		}

		if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (cmd.getName().equalsIgnoreCase("zs-jointoggle")) {
				if (args.length != 1) {
					return false;
				}
				String map = args[0];
				if(games.exists(map)) {
					if(games.getMapEnabled(map) == true) {
						player.sendMessage("Map " + map + " disabled!");
						games.setMapEnabled(map, false);
						getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + map + ".enabled",
								false);
					}
					else if(games.getMapEnabled(map) == false) {
						player.sendMessage("Map " + map + " enabled!");
						games.setMapEnabled(map, true);
						getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + map + ".enabled",
								true);
					}
				}
				else {
					player.sendMessage("Map does not exist");
					return false;
				}
			}

			if (cmd.getName().equalsIgnoreCase("zsa-spawn")) {
				if (args.length != 2) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.add.put(player.getName(), Integer.valueOf(3));
				this.commandMap.put(player.getName(), args[0]);
				this.commandwave.put(player.getName(), Integer.valueOf(Integer.parseInt(args[1])));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Zombie Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(10));
				this.spawn.showSpawns(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsr-spawn")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(3));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Zombie Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(10));
				this.spawn.showSpawns(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsa-mysteryboxsign")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.add.put(player.getName(), Integer.valueOf(14));
				this.commandMap.put(player.getName(), args[0]);
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Zombie Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(10));
			} else if (cmd.getName().equalsIgnoreCase("zs-start")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				Bukkit.broadcastMessage(ChatColor.AQUA + "Admin has started the game!");
				Games(args[0], Boolean.valueOf(true));
			} else if (cmd.getName().equalsIgnoreCase("zs-end")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				GamesOver(args[0], Boolean.valueOf(true));
				player.sendMessage(ChatColor.GOLD + "You have ended the games early!");
			} else if (cmd.getName().equalsIgnoreCase("zs-create")) {
				if (args.length != 6) {
					return false;
				}
				try {
					this.commandMap.put(player.getName(), args[0]);
					this.add.put(player.getName(), Integer.valueOf(0));
					this.remove.remove(player.getName());
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".maxzombies",
							Integer.valueOf((int) (Double.parseDouble(args[1]) * 100.0D)));
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".maxplayers",
							Integer.valueOf(Integer.parseInt(args[2])));
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".maxwaves",
							Integer.valueOf(Integer.parseInt(args[3])));
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".teleporteramount",
							Integer.valueOf(Integer.parseInt(args[4])));
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".allfastzombies",
							Boolean.valueOf(Boolean.parseBoolean(args[5])));
					getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + args[0] + ".enabled",
							false);
					games.setMaxZombies(args[0], (int) (Double.parseDouble(args[1]) * 100.0D));
					games.setMaxPlayers(args[0], Integer.parseInt(args[2]));
					games.setMaxWaves(args[0], Integer.parseInt(args[3]));
					games.setTeleporterAmount(args[0], Integer.parseInt(args[4]));
					games.setAllFastZombies(args[0], Boolean.parseBoolean(args[5]));
					games.setPcount(args[0], 0);
					games.setState(args[0], 1);
					games.setMapEnabled(args[0], false);
					Maps.put(args[0], player.getWorld().getName());
					this.perk.setPerk(args[0], 0);
					this.perkcount.put(args[0], Integer.valueOf(0));
					this.perkend.put(args[0], Boolean.valueOf(false));
					games.setWave(args[0], 0);
					this.changedBlocks.put(args[0], new HashMap<>());
					this.placedBlocks.put(args[0], new HashMap<>());
					List<String> gamemaps = new ArrayList<>();
					for (String s : Maps.keySet()) {
						if (((String) Maps.get(s)).equalsIgnoreCase(player.getWorld().getName())) {
							gamemaps.add(s);
						}
					}
					getCustomConfig().set(String.valueOf(Maps.get(args[0])) + ".maps", gamemaps);
					saveCustomConfig();
				} catch (UnknownFormatConversionException ex) {
					player.sendMessage(ChatColor.RED + "Try again, bad arguments!");
					return false;
				}
				player.sendMessage(this.lang.strings.get(12));
			} else if (cmd.getName().equalsIgnoreCase("zs-remove")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				getCustomConfig().set(String.valueOf(Maps.get(args[0])) + ".maps", Maps);
				getCustomConfig().set(String.valueOf(Maps.get(args[0])) + "." + args[0], null);
				Maps.remove(args[0]);
				saveCustomConfig();
				reload();
				player.sendMessage(String.valueOf(this.lang.strings.get(59)) + args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsa-fire")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(4));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Fire Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(13));
			} else if (cmd.getName().equalsIgnoreCase("zsr-fire")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(4));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Fire Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(14));
			} else if (cmd.getName().equalsIgnoreCase("zsa-spectate")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(1));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Spectate Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(15));
			} else if (cmd.getName().equalsIgnoreCase("zsr-spectate")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(1));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Spectate Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(16));
			} else if (cmd.getName().equalsIgnoreCase("zsa-waiting")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(7));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(17));
			} else if (cmd.getName().equalsIgnoreCase("zsr-waiting")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(7));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(18));
			} else if (cmd.getName().equalsIgnoreCase("zsa-lightning")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(2));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Lightning Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(19));
			} else if (cmd.getName().equalsIgnoreCase("zsr-lightning")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(2));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Lightning Spawn Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(20));
			} else if (cmd.getName().equalsIgnoreCase("zsa-door")) {
				if (args.length != 2) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(5));
				this.commandwave.put(player.getName(), Integer.valueOf(Integer.parseInt(args[1])));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Door Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(21));
				this.door.showDoors(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsr-door")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(5));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Door Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(83));
				this.door.showDoors(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsa-nav-door")) {
				if (args.length != 2) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				if(!this.navigationnode.doesExist(args[1])) {
					player.sendMessage("Path does not exist! Define a path first.");
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.commandsubwaycolor.put(player.getName(), args[1]);
				this.add.put(player.getName(), Integer.valueOf(15));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Navigation Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage("Add a navigation door to check");
				this.navigationnode.showNavigationNodes(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsa-nav-room")) {
				if (args.length != 2) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.commandName.put(player.getName(), args[1]);
				this.addedneutralnode.put(player.getName(), false);
				this.add.put(player.getName(), Integer.valueOf(11));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Navigation Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage("First, select a region. Left click the first location.");
				this.navigationnode.showNavigationNodes(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsa-nav-path")) {
				if (args.length != 4) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				if(!this.navigationregion.doesExist(args[1])) {
					player.sendMessage("Region does not exist! Define a region first.");
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.commandName.put(player.getName(), args[1]);
				this.commandsubwaycolor.put(player.getName(), args[2]);
				this.commandsubwaynumber.put(player.getName(), Integer.parseInt(args[3]));
				this.add.put(player.getName(), Integer.valueOf(13));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Navigation Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.remove.remove(player.getName());
				player.sendMessage("Add a navigation path between 2 regions");
				this.navigationnode.showNavigationNodes(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsr-nav")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				//this.commandMap.put(player.getName(), args[0]);
				//this.remove.put(player.getName(), Integer.valueOf(9));
				player.getInventory().setItem(0, BuildTool("[ZombieSurvival] Spawn Navigation Selection Tool"));
				player.getInventory().setHeldItemSlot(0);
				this.add.remove(player.getName());
				player.sendMessage("Removed All Regions");
				for(NavigationNode n : this.navigationnode.getAllNodes()) {
					for(ZombieRegion z : this.navigationregion.regionList()) {
						this.removeNavigationNode(n.location.getBlock(), args[0]);
						this.removeNavigationRegion(z.getName());
					}
				}
			} else if (cmd.getName().equalsIgnoreCase("zs-reload")) {
				reload();
				player.sendMessage("Reloaded ZombieSurvival!");
			} else if (cmd.getName().equalsIgnoreCase("zsa-bar")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(8));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(55));
				this.bar.showBarricades(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsr-bar")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(8));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(56));
				this.bar.showBarricades(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zsr-mysteryboxsign")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.remove.put(player.getName(), Integer.valueOf(14));
				this.add.remove(player.getName());
				player.sendMessage("Select a mystery box sign to remove");
			} else if (cmd.getName().equalsIgnoreCase("zsa-perk")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.commandMap.put(player.getName(), args[0]);
				this.add.put(player.getName(), Integer.valueOf(9));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(57));
			} else if (cmd.getName().equalsIgnoreCase("zstp")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				player.teleport(this.spawn.lobbies.get(args[0]));
			} else if (cmd.getName().equalsIgnoreCase("zs-c")) {
				this.easycreate.put(player.getName(), Integer.valueOf(0));
				player.sendMessage(this.lang.strings.get(60));
			} else if (cmd.getName().equalsIgnoreCase("zs-doorlink")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.add.put(player.getName(), Integer.valueOf(10));
				this.commandMap.put(player.getName(), args[0]);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(70));
				this.door.showDoors(args[0]);
			} else if (cmd.getName().equalsIgnoreCase("zs-barlink")) {
				if (args.length != 1) {
					return false;
				}
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				this.add.put(player.getName(), Integer.valueOf(12));
				this.commandMap.put(player.getName(), args[0]);
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(80));
				this.bar.showBarricades(args[0]);
			}
			if (cmd.getName().equalsIgnoreCase("stats")) {
				if (args.length < 1 && pmethods.inGame(player)) {
					info(player, player);
				}

				if (args.length > 0) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p != null && pmethods.inGame(p)) {
						info(p, player);
					} else {
						player.sendMessage(this.lang.strings.get(61));
					}
				}
				player.sendMessage("Number of players online: " + Integer.toString(this.onlinep));
			} else if (cmd.getName().equalsIgnoreCase("whisper")) {
				Player other = Bukkit.getServer().getPlayer(args[0]);
				if (other == null) {
					return false;
				}
				String send = " MESSAGE:";
				for (int i = 1; i < args.length; i++) {
					send = String.valueOf(send) + " " + args[i];
				}
				other.sendMessage("From: " + player.getName() + send);
				player.sendMessage("Sent To: " + other.getName() + "Message:" + send);
			} else if (cmd.getName().equalsIgnoreCase("zshelp")) {
				player.sendMessage("Commands: stats, whisper, join, leave, zshelp");
				if (player.isOp()) {
					player.performCommand("help ZombieSurvival");
				}
			} else if (cmd.getName().equalsIgnoreCase("zs-rank-ignoreplayer")) {
				if (args.length != 2) {
					return false;
				}
				try {
					this.autorank.setIgnored(args[0], Boolean.valueOf(args[1]));
					player.sendMessage("ZombieSurvival autorank has set " + args[0] + " rank checking to " + args[1]);
				}
				catch(Exception e) {
					return false;
				}
			} else if (cmd.getName().equalsIgnoreCase("zs-signs")) {
				if (player.isOp()) {
					player.sendMessage("Format : 0 = Line 1, 1 = Line 2 2 = Line 3");
					player.sendMessage("Map Stats: 	 Line 1: zombie stats 	Line 2: (MAP)");
					player.sendMessage("My Stats: 	 Line 1: zs 			Line 2: My Stats");
					player.sendMessage("Class Join:  Line 1: zs    	   		Line 2: Join Class     Line 3: (CLASS)");
					player.sendMessage("Class Leave: Line 1:zs   	   		Line 2: Leave Class");
					player.sendMessage("Class Buy: 	 Line 1:zs    			Line 2: Buy Class      Line 3: (Class)    Line 4: (PRICE)");
				} else {
					player.sendMessage("Right click on Zombie signs to interact!");
				}
			} else if (cmd.getName().equalsIgnoreCase("bsapj")) {
				if (args.length < 1) {
					String message = String.valueOf(this.lang.strings.get(64)) + Maps.keySet().toString();
					player.sendMessage(ChatColor.BLUE + message);
					return false;
				}
				ArrayList<Object> noncasesensitive = new ArrayList<Object>(Maps.keySet());
				Boolean noncasebool = false;
				String MapWithCase = "";
				for(int i=0; i<Maps.keySet().size(); i++) {
					String s = (String) noncasesensitive.get(i);
					if(s.toLowerCase().equals(args[0].toLowerCase())) {
						noncasebool = true;
						MapWithCase = s;
						break;
					}
				}
				if(noncasebool != true) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}
				/*Case Sensitive
				if (!Maps.containsKey(args[0])) {
					player.sendMessage(this.lang.strings.get(8));
					return false;
				}*/
				if (games.getMapEnabled(MapWithCase) == false && !sender.isOp()) {
					player.sendMessage(ChatColor.DARK_RED + "This map is disabled. It may be under construction. Contact an administrator");
					return false;
				}
				if (this.justleftgame.containsKey(player.getName())) {
					player.sendMessage(String.valueOf(this.lang.strings.get(62))
							+ Integer.toString(
									this.leavetimer - ((Integer) this.justleftgame.get(player.getName())).intValue())
							+ (String) this.lang.strings.get(63));
					return true;
				}
				if (pmethods.numberInMap(MapWithCase) < games.getMaxPlayers(MapWithCase) && !pmethods.inGame(player)
						&& !dead.containsKey(player.getName()) && stats.getSesPoints(player.getName()) >= 0.0D) {
					if (this.jm) {
						Bukkit.broadcastMessage(ChatColor.GOLD + "[ZombieSurvival] " + ChatColor.GREEN
								+ player.getName() + " just joined " + MapWithCase + "!");
					}
					games.setPlayerMap(player.getName(), MapWithCase);
					stats.setPoints(player.getName(), 0.0D);
					stats.setKills(player.getName(), 0.0D);
					games.setPcount(MapWithCase, games.getPcount(MapWithCase) + 1);
					pData pData = new pData();
					pData.name = player.getName();
					pData.inventory = player.getInventory().getContents();
					pData.armor = player.getInventory().getArmorContents();
					pData.food = player.getFoodLevel();
					pData.gamemode = player.getGameMode();
					pData.health = (int) player.getHealthScale();
					pData.location = player.getLocation();
					this.repairmap.put(player.getName(), false);
					this.playerdata.put(player.getName(), pData);
					this.cpdata.put(player.getUniqueId(), new CustomPlayerData(player.getUniqueId()));
					if (games.getState(MapWithCase) < 2) {
						Games(MapWithCase, Boolean.valueOf(false));
					} else if (games.getState(MapWithCase) > 1) {
						placeInGame(player, MapWithCase, false);
					}
					player.sendMessage(this.lang.strings.get(54));
				} else {
					player.sendMessage(this.lang.strings.get(65));
				}
			} else if (cmd.getName().equalsIgnoreCase("bsapl") && pmethods.inGame(player)) {
				String map = pmethods.playerGame(player);
				this.justleftgame.put(player.getName(), Integer.valueOf(0));
				if (!dead.containsKey(player.getName())) {
					int tempcount = games.getPcount(map);
					games.setPcount(map, tempcount - 1);
				}
				if(this.teleporttospawn) {
					player.teleport(player.getWorld().getSpawnLocation());
				}
				else {
					player.teleport(((pData) this.playerdata.get(player.getName())).location);
				}
				player.setGameMode(((pData) this.playerdata.get(player.getName())).gamemode);
				player.setFlying(false);
				player.setPlayerListName("§3[Lobby] §r" + player.getDisplayName());
				spectate.spectators.remove(player.getName());
				player.sendMessage(this.lang.strings.get(66));
				fPerk.ClearPerk(player);
				games.removePlayerMap(player.getName());
				stats.setPoints(player.getName(), 0.0D);
				CreateScoreboard(player);
				stats.removeSplayerKills(player.getName());
				dead.remove(player.getName());
				utilities.unhidePlayer(player);
				player.setLevel(0);
				player.setExp(0);
				player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
				this.playerupgradelevel.put(player.getName(), 1);
				this.playerupgradeint.put(player.getName(), 0);
				for (PotionEffect effect : player.getActivePotionEffects()) {
					player.removePotionEffect(effect.getType());
				}
				if (this.invsave) {
					player.getInventory().clear();
					player.getInventory().setArmorContents(null);
					player.getInventory().setContents(((pData) this.playerdata.get(player.getName())).inventory);
				}
				this.playerdata.remove(player.getName());
				this.cpdata.remove(player.getUniqueId());
				GamesOver(map, Boolean.valueOf(false));
				String name = player.getName();
			}
			else if (cmd.getName().equalsIgnoreCase("zs-teleporter")) {
				sender.sendMessage("Must be a command block! : /zs-teleporter <map> <player> <teleporter number> <command to execute>");
			}
		} else if (cmd.getName().equalsIgnoreCase("zs-teleporter") && sender instanceof BlockCommandSender) {
			try {
				String maparg = args[0];
				String plyarg = args[1];
				int teleporter = Integer.parseInt(args[2]);
				Player plyr = (Player) this.getServer().getPlayer(plyarg);
				String map = pmethods.playerGame(plyr);
				teleporters teleobj = teleportlist.get(maparg);
				
				if (Maps.containsKey(maparg)) {
					if(pmethods.inGame(plyr) && games.getState(map) != 1) {
						if(this.power.get(map)) {
							if(teleobj.isTeleporterActive(teleporter)) {
								String finalarg = "";
								for(int i = 0; i < args.length; i++) {
									if(i != 0 && i != 1 && i != 2) {
										if(i != args.length-1) {
											finalarg = finalarg + args[i] + " ";
										}
										else {
											finalarg = finalarg + args[i];
										}
									}
								}
								getServer().dispatchCommand(getServer().getConsoleSender(), finalarg);
								plyr.getWorld().strikeLightning(plyr.getLocation());
							}
							if(!teleobj.isTeleporterActive(teleporter)) {
								teleobj.activateTeleporter(teleporter);
								String finalarg = "";
								for(int i = 0; i < args.length; i++) {
									if(i != 0 && i != 1 && i != 2) {
										if(i != args.length-1) {
											finalarg = finalarg + args[i] + " ";
										}
										else {
											finalarg = finalarg + args[i];
										}
									}
								}
								getServer().dispatchCommand(getServer().getConsoleSender(), finalarg);
								plyr.getWorld().strikeLightning(plyr.getLocation());
								if(teleobj.areAllTeleportersActive()) {
									for (String p : pmethods.playersInMap(map)) {
										Player ply = Bukkit.getPlayer(p);
										ply.sendTitle("", ChatColor.GREEN + "ALL TELEPORTERS HAVE BEEN ACTIVATED", 5, 30, 5);
									}
								}
								else {
									for (String p : pmethods.playersInMap(map)) {
										Player ply = Bukkit.getPlayer(p);
										ply.sendTitle("", ChatColor.GREEN + "TELEPORTER " + args[2] + " HAS BEEN ACTIVATED!", 5, 30, 5);
									}
								}
							}
						}
						else {
							plyr.sendMessage("Power needs to be activated first!");
						}
					}
					else {
						System.err.println("ZS-TELEPORTER ERROR: Player " + plyarg + " not found!");
					}
				}
				else {
					System.err.println("ZS-TELEPORTER ERROR: Map " + maparg + " not found!");
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else {
			sender.sendMessage("Must be player!");
			return false;
		}

		return true;
	}

	public void info(Player p, Player s) {
		String str3 = rPlayers(pmethods.playerGame(p));
		s.sendMessage(ChatColor.GREEN + "Current Wave: " + ChatColor.DARK_RED
				+ Integer.toString(games.getWave(pmethods.playerGame(p))));
		s.sendMessage(
				ChatColor.GREEN + "Kills: " + ChatColor.DARK_RED + Double.toString(stats.getSesKills(p.getName())));
		s.sendMessage(ChatColor.GREEN + "All-Time Kills: " + ChatColor.DARK_RED
			+ Double.toString(stats.getTotalKills(p.getName())));
		s.sendMessage(ChatColor.GREEN + "Points: " + ChatColor.DARK_RED
				+ Double.toString(stats.getSesPoints(p.getName())));
		s.sendMessage(ChatColor.GREEN + "All-Time Points: " + ChatColor.DARK_RED
				+ Double.toString(stats.getTotalPoints(p.getName())));
		s.sendMessage(
				ChatColor.GREEN + "Deaths: " + ChatColor.DARK_RED + Double.toString(stats.getSesDeaths(p.getName())));
		s.sendMessage(ChatColor.GREEN + "All-Time Deaths: " + ChatColor.DARK_RED
				+ Double.toString(stats.getTotalDeaths(p.getName())));
		s.sendMessage(ChatColor.GREEN + "Remaining Players: " + str3);
	}

	public String rPlayers(String map) {
		String str = null;
		ChatColor chatColor = ChatColor.DARK_RED;
		for (String p : pmethods.playersInMap(map)) {
			if (!dead.containsKey(p)) {
				str = String.valueOf(chatColor.toString().concat(p)) + ChatColor.GRAY + ", " + ChatColor.DARK_RED;
			}
		}
		return str;
	}

	public void NewWave(final String map) {
		if (games.getPcount(map) != 0) {// Fixes zombies spawning after game end?
			String zombmax;
			if(games.getWave(map) >= this.fastzombiestartwave) {
				if (games.getWave(map) % 2 == 0 && this.mapfastchance.get(map) != 1) {//Runner chance increase
					this.mapfastchance.replace(map, this.mapfastchance.get(map) - 1);
					Bukkit.getConsoleSender().sendMessage("Runner chance has been increased to: " + "1/2/" + this.mapfastchance.get(map) + " on map: " + map);
					for (String player : pmethods.playersInMap(map)) {
						Player p = Bukkit.getPlayer(player);
						if (p != null) {
							p.sendMessage(ChatColor.DARK_RED + "" + ChatColor.ITALIC + "The zombies are getting faster...");
						}
					}
				}
			}
			if(this.mapseekspeed.get(map) < this.fastseekspeed) {//Normal zombie speed increase
				this.mapseekspeed.replace(map, this.mapseekspeed.get(map) + 0.02D);
			}
			games.setState(map, 3);
			games.setWave(map, games.getWave(map) + 1);
			this.bar.resetBars(map);
			if (this.skellywavechance != 0) {
				int go = this.random.nextInt(this.skellywavechance);
				if (go == 1 && games.getWave(map) != 1 && this.wolfs) {
					this.wolfwave.put(map, Boolean.valueOf(true));
				} else {
					this.wolfwave.put(map, Boolean.valueOf(false));
				}
			}
			if (Integer.toString(games.getWave(map)).endsWith("0")) {
				for (Location blg : this.roundFire.keySet()) {
					if (((String) this.roundFire.get(blg)).equalsIgnoreCase(map)) {
						blg.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
					}
				}
			}
			if (games.getWave(map) > games.getMaxWaves(map)) {
				GamesOver(map, Boolean.valueOf(true));
				return;
			}
			String health = "20";
			if (this.diffmulti != 0.0D) {
				int h = (int) (games.getWave(map) * this.diffmulti);
				health = Integer.toString(3 + h);
				if(Integer.parseInt(health) > this.zombiemaxhealth) {
					health = "20";
				}
			}

			if (this.smartw) {
				zombmax = Integer.toString(this.sg.smartWaveCount(map));
			} else {
				zombmax = utilities.annouceMax(map);
			}
			if (games.getWave(map) != 1) {
				Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
					public void run() {
						if (games.getState(map) != 1) {// Maybe this will stop the spawning?
							if (main.this.smartw) {
								main.this.wavemax.put(map, Integer.valueOf(main.this.sg.smartWaveCount(map)));
							} else {
								main.this.maxfinder(map);
							}
							games.setZcount(map, 0);
							games.setZslayed(map, 0);
							games.setState(map, 2);
							for (String player : pmethods.playersInMap(map)) {
								Player p = Bukkit.getPlayer(player);
								updateScoreboardInGame(p, map, 0);
							}
						}
					}
				}, this.wait);
			}
			Boolean doorsopen = Boolean.valueOf(false);
			for (String player : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(player);
				if (p != null) {
					amountofbarricadesrepaired.put(p.getName(), 0);
					p.sendTitle(ChatColor.GREEN + "WAVE: " + ChatColor.DARK_RED + Integer.toString(games.getWave(map)),
							ChatColor.GRAY + " starts in " + ChatColor.DARK_RED + Integer.toString(this.wait / 20)
									+ ChatColor.GRAY + " seconds!",
							5, 50, 5);
					p.sendMessage(ChatColor.GREEN + "WAVE: " + ChatColor.DARK_RED + Integer.toString(games.getWave(map))
							+ ChatColor.GRAY + " starts in " + ChatColor.DARK_RED + Integer.toString(this.wait / 20)
							+ ChatColor.GRAY + " seconds!");
					if (((Boolean) this.wolfwave.get(map)).booleanValue()) {
						p.sendMessage(ChatColor.GRAY + "The zombies have called in their " + ChatColor.DARK_RED
								+ "wolves!" + ChatColor.GRAY + " Prepare for the slaughter!");
						p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " wolves with "
								+ ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
					} else {
						p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " zombies with "
								+ ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
					}
					cpdata.get(p.getUniqueId()).repairItems();
					cpdata.get(p.getUniqueId()).restockItems();
				}
			}
			Iterator<String> it;
			for (it = dead.keySet().iterator(); it.hasNext();) {
				String player = it.next();
				if (((String) dead.get(player)).equalsIgnoreCase(map)) {
					Player p = Bukkit.getPlayer(player);
					if (p != null) {
						placeInGame(p, dead.get(player), true);
						games.setPcount(map, games.getPcount(map) + 1);
						it.remove();
					}
				}
			}
			for (it = spectate.spectators.keySet().iterator(); it.hasNext();) {
				String name = it.next();
				if (((String) spectate.spectators.get(name)).matches(map)) {
					Player p = Bukkit.getPlayer(name);
					utilities.unhidePlayer(p);
					it.remove();
				}
			}
			for (door d : this.door.doorWave(map, games.getWave(map))) {
				if (d.location != null && !this.buydoors) {// Prevent errors from no doors in map & don't open door if buyable doors is on
					Block dblock = d.location.getBlock();
					dblock.setType(Material.AIR);
					doorsopen = Boolean.valueOf(true);
				}
			}
			for (String player : pmethods.playersInMap(map)) {
				Player p = Bukkit.getPlayer(player);
				if (p != null) {
					updateScoreboardInGame(p, map, 1);
					if (this.healnewwave) {
						p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
						p.setFoodLevel(20);
					}
					if (doorsopen.booleanValue()) {
						p.sendMessage(this.lang.strings.get(67));
					}
				}
			}
		}
	}

	public void resetBlocks(String map) {
		List<Location> fire = new ArrayList<>();
		if (!this.roundFire.isEmpty() && this.roundFire != null) {
			for (Location str : this.roundFire.keySet()) {
				if (((String) this.roundFire.get(str)).equalsIgnoreCase(map)) {
					fire.add(str);
				}
			}
			for (Location b : fire) {
				b.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
			}
			fire.clear();
		}
		if (!this.changedBlocks.isEmpty() && this.changedBlocks.get(map) != null) {
			for (Block b : (this.changedBlocks.get(map)).keySet()) {
				BlockState bstate = (BlockState) ((Map) this.changedBlocks.get(map)).get(b);
				b.setType(bstate.getType());
			}
			((Map) this.changedBlocks.get(map)).clear();
		}
		if (!this.placedBlocks.isEmpty() && this.placedBlocks.get(map) != null) {
			for (Block p : (this.placedBlocks.get(map)).keySet()) {
				p.setType(Material.AIR);
			}
			((Map) this.placedBlocks.get(map)).clear();
		}
	}

	public void addLobby(Location lobbyloc, String map) {
		lobbyloc.add(0.5D, 1.0D, 0.5D);
		this.spawn.lobbies.put(map, lobbyloc);
		String savelobbyloc = parseToStr(lobbyloc);
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".lobby", savelobbyloc);
		saveCustomConfig();
	}

	public void removeLobby(Location lobbyloc, String map) {
		lobbyloc.add(0.5D, 1.0D, 0.5D);
		if (this.spawn.lobbies.containsKey(map) && ((Location) this.spawn.lobbies.get(map)).equals(lobbyloc)) {
			this.spawn.lobbies.remove(map);
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".lobby", null);
		saveCustomConfig();
	}

	public void addFire(Location fireblock, String map) {
		this.roundFire.put(fireblock, map);
		List<String> adeath = new ArrayList<>();
		for (Location str : this.roundFire.keySet()) {
			if (((String) this.roundFire.get(str)).equalsIgnoreCase(map)) {
				adeath.add(parseToStr(str));
			}
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".roundfire", adeath);
		saveCustomConfig();
	}

	public void removeFire(Location fireblock, String map) {
		this.roundFire.remove(fireblock);
		List<String> adeath = new ArrayList<>();
		for (Location str : this.roundFire.keySet()) {
			if (((String) this.roundFire.get(str)).equalsIgnoreCase(map)) {
				adeath.add(parseToStr(str));
			}
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".roundfire", adeath);
		saveCustomConfig();
	}

	public void addBar(Block b, String map) {
		this.bar.addBar(b, map);
	}

	public void removeBar(Block b, String map) {
		this.bar.removeBar(this.bar.findBar(b.getX(), b.getY(), b.getZ()));
	}

	public void addNavigationRegion(String name, String map, Location a, Location b, NavigationNode nn) {//Also add regions it is connected to
		this.navigationregion.addNavigationRegion(name, map, a, b, nn);
	}
	
	public void removeNavigationRegion(String name) {
		this.navigationregion.removeNavigationRegion(name);
	}
	
	public void addNavigationNode(Block b, String map, String sc, int sn, boolean isNeutral) {
		this.navigationnode.addNavigationNode(b, map, sc, sn, isNeutral);
	}
	
	public void removeNavigationNode(Block b, String map) {
		this.navigationnode.removeNavigationNode(this.navigationnode.findNavigationNode(b.getX(), b.getY(), b.getZ()));
	}

	public void addSpawn(Block b, int wavenumber, String map) {
		this.spawn.addSpawn(b, map, wavenumber);
	}

	public void removeSpawn(Block b, String map) {
		this.spawn.removeSpawn(this.spawn.findSpawn(b.getX() + 0.5D, b.getY(), b.getZ() + 0.5D));
	}

	public void addDoor(Block b, int wavenumber, String map) {
		this.door.addDoor(b, map, wavenumber);
	}

	public void removeDoor(Location b) {
		this.door.removeDoor(this.door.findDoor(b.getX(), b.getY(), b.getZ()));
	}

	public void addSpec(Location specloc, String map) {
		specloc.add(0.5D, 1.0D, 0.5D);
		this.spawn.spectate.put(map, specloc);
		String savelobbyloc = parseToStr(specloc);
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".spectate", savelobbyloc);
		saveCustomConfig();
	}

	public void removeSpec(Location specloc, String map) {
		specloc.add(0.5D, 1.0D, 0.5D);
		if (this.spawn.spectate.containsKey(map) && ((Location) this.spawn.spectate.get(map)).equals(specloc)) {
			this.spawn.spectate.remove(map);
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".spectate", null);
		saveCustomConfig();
	}

	public void addLight(Location lightloca, String map) {
		lightloca.add(0.5D, 1.0D, 0.5D);
		this.lightloc.put(map, lightloca);
		String savelobbyloc = parseToStr(lightloca);
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".lightning", savelobbyloc);
		saveCustomConfig();
	}

	public void removeLight(Location lightloca, String map) {
		lightloca.add(0.5D, 1.0D, 0.5D);
		if (this.lightloc.containsKey(map) && ((Location) this.lightloc.get(map)).equals(lightloca)) {
			this.lightloc.remove(map);
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".lightning", null);
		saveCustomConfig();
	}

	public void addDeath(Location deathloc, String map) {
		deathloc.add(0.5D, 1.0D, 0.5D);
		this.deathPoints.put(map, deathloc);
		String savelobbyloc = parseToStr(deathloc);
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".waiting", savelobbyloc);
		saveCustomConfig();
	}

	public void removeDeath(Location deathloc, String map) {
		deathloc.add(0.5D, 1.0D, 0.5D);
		if (this.deathPoints.containsKey(map) && ((Location) this.deathPoints.get(map)).equals(deathloc)) {
			this.deathPoints.remove(map);
		}
		getCustomConfig().set(String.valueOf(Maps.get(map)) + "." + map + ".waiting", null);
		saveCustomConfig();
	}

	@EventHandler
	public void onOpAction(PlayerInteractEvent event) {
		int OPadd = -1;
		int OPremove = -1;
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		if (player.isOp() || player.hasPermission("zs.edit")) {

			if (this.add.containsKey(player.getName())) {
				OPadd = ((Integer) this.add.get(player.getName())).intValue();
			}
			if (this.remove.containsKey(player.getName())) {
				OPremove = ((Integer) this.remove.get(player.getName())).intValue();
			}
			switch (OPadd) {
			case 0://Add Lobby
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addLobby(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(25));
					break;
				}
				if (this.easycreate.get(player.getName()) != null
						&& ((Integer) this.easycreate.get(player.getName())).intValue() == 4) {
					this.add.remove(player.getName());
					player.sendMessage(this.lang.strings.get(43));
					player.performCommand("zsa-spawn " + this.commandMap + " 1");
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 1://Add Spectator Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addSpec(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(26));
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 2://Add Lightning Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addLight(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(27));
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 3://Add Zombie Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addSpawn(block, ((Integer) this.commandwave.get(player.getName())).intValue(),
							this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(28));
					break;
				}
				if (this.easycreate.get(player.getName()) != null
						&& ((Integer) this.easycreate.get(player.getName())).intValue() == 4) {
					this.add.remove(player.getName());
					player.sendMessage(this.lang.strings.get(44));
					this.easycreate.remove(player.getName());
					break;
				}
				this.spawn.hideSpawns(this.commandMap.get(player.getName()));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 4://Add Fire Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addFire(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(29));
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 5://Add Door Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addDoor(block, ((Integer) this.commandwave.get(player.getName())).intValue(),
							this.commandMap.get(player.getName()));
					if (block.getType() == Material.OAK_DOOR) {
						smartDoors(block, this.commandMap.get(player.getName()),
								((Integer) this.commandwave.get(player.getName())).intValue());
					}
					player.sendMessage(this.lang.strings.get(30));
					break;
				}
				this.door.hideDoors(this.commandMap.get(player.getName()));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 7://Add Waiting Area??
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addDeath(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(32));
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				break;

			case 8://Add Barricade Block
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					addBar(block, this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(45));
					break;
				}
				this.bar.hideBarricades(this.commandMap.get(player.getName()));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				break;

			case 10://Door Link
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					if (this.playerdoorlink.get(player.getName()) == null) {
						door d = this.door.findDoor(block.getX(), block.getY(), block.getZ());
						if (d != null && d.map.matches(this.commandMap.get(player.getName()))) {
							this.playerdoorlink.put(player.getName(), d);
							this.door.hideDoors(this.commandMap.get(player.getName()));
							this.spawn.showSpawns(this.commandMap.get(player.getName()));
							break;
						}
						player.sendMessage(this.lang.strings.get(71));

						break;
					}
					Spawn s = this.spawn.findSpawn(block.getX() + 0.5D, block.getY(), block.getZ() + 0.5D);
					if (s != null && s.map.matches(this.commandMap.get(player.getName()))) {
						door d = this.playerdoorlink.get(player.getName());
						d.spawns.add(s);
						this.spawn.hideSpawn(s);
						player.sendMessage(this.lang.strings.get(72));
						break;
					}
					player.sendMessage(this.lang.strings.get(73));

					break;
				}

				this.add.remove(player.getName());
				this.playerdoorlink.remove(player.getName());
				this.door.hideDoors(this.commandMap.get(player.getName()));
				this.spawn.hideSpawns(this.commandMap.get(player.getName()));
				this.door.saveDoor();
				this.spawn.saveSpawn();
				player.sendMessage(this.lang.strings.get(41));
				break;
			
			case 11://Add Zombie Navigation Region
				if(event.getItem().getItemMeta().getDisplayName().equals("[ZombieSurvival] Navigation Selection Tool")) {
					if (event.getAction() == Action.LEFT_CLICK_BLOCK && !this.plynavposition1.containsKey(player.getName())) {
						this.plynavposition1.put(player.getName(), event.getClickedBlock().getLocation());
						player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "First Postition Set");
						break;
					}
					else if(event.getAction() == Action.LEFT_CLICK_BLOCK && this.plynavposition1.containsKey(player.getName()) && !this.plynavposition2.containsKey(player.getName())) {
						this.plynavposition2.put(player.getName(), event.getClickedBlock().getLocation());
						player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Second Postition Set");
						if(this.plynavposition1.containsKey(player.getName()) && this.plynavposition2.containsKey(player.getName())){
							player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Positions Set! Add a neutral node to the region");
						}
						break;
					}
					else if(event.getAction() == Action.LEFT_CLICK_BLOCK && this.addedneutralnode.get(player.getName()).booleanValue() == false) {
						player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Added neutral navigation point!");
						this.addedneutralnode.replace(player.getName(), true);
						addNavigationNode(block, this.commandMap.get(player.getName()), "Null", 0, true);
					}
				}
				addNavigationRegion(this.commandName.get(player.getName()), this.commandMap.get(player.getName()), this.plynavposition1.get(player.getName()), this.plynavposition2.get(player.getName()), this.plyneutralnode.get(player.getName()));
				this.plyneutralnode.remove(player.getName());
				this.addedneutralnode.replace(player.getName(), false);
				this.navigationnode.hideNavigationNodes(this.commandMap.get(player.getName()));
				this.add.remove(player.getName());
				this.plynavposition1.remove(player.getName());
				this.plynavposition2.remove(player.getName());
				this.commandName.remove(player.getName());
				this.commandMap.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;
				
			case 12://Bar Link
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					if (this.playerbarricadelink.get(player.getName()) == null) {
						Barricade b = this.bar.findBar(block.getX(), block.getY(), block.getZ());
						if (b != null && b.map.matches(this.commandMap.get(player.getName()))) {
							this.playerbarricadelink.put(player.getName(), b);
							this.bar.hideBarricades(this.commandMap.get(player.getName()));
							this.spawn.showSpawns(this.commandMap.get(player.getName()));
							break;
						}
						player.sendMessage(this.lang.strings.get(81));

						break;
					}
					Spawn s = this.spawn.findSpawn(block.getX() + 0.5D, block.getY(), block.getZ() + 0.5D);
					if (s != null && s.map.matches(this.commandMap.get(player.getName()))) {
						Barricade b = this.playerbarricadelink.get(player.getName());
						b.spawns.add(s);
						this.spawn.hideSpawn(s);
						player.sendMessage(this.lang.strings.get(82));
						break;
					}
					player.sendMessage(this.lang.strings.get(73));
					break;
				}

				this.add.remove(player.getName());
				this.playerbarricadelink.remove(player.getName());
				this.spawn.hideSpawns(this.commandMap.get(player.getName()));
				this.bar.saveBar();
				this.spawn.saveSpawn();
				player.sendMessage(this.lang.strings.get(41));
				break;
			
			case 13: //Add Nav Node
				if(event.getItem() != null) {
					if(event.getItem().getItemMeta().getDisplayName().equals("[ZombieSurvival] Navigation Selection Tool")) {
						if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
							player.sendMessage(ChatColor.BOLD + "" + ChatColor.GREEN + "Added navigation point!");
							addNavigationNode(block, this.commandMap.get(player.getName()), this.commandsubwaycolor.get(player.getName()), this.commandsubwaynumber.get(player.getName()), false);
							Location bloc = block.getLocation();
							this.navigationregion.addNavNode(this.commandName.get(player.getName()), this.navigationnode.findNavigationNode(bloc.getX(), bloc.getY(), bloc.getZ()));
						}
						break;
					}
					break;
				}
				this.navigationnode.hideNavigationNodes(this.commandMap.get(player.getName()));
				this.commandMap.remove(player.getName());
				this.commandsubwaycolor.remove(player.getName());
				this.commandsubwaynumber.remove(player.getName());
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				break;
				
			case 14://Add Mystery Box Sign
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					if (event.getClickedBlock().getState() instanceof Sign) {
						Sign s = (Sign)event.getClickedBlock().getState();
						if(s.getLine(1).equalsIgnoreCase("§lMystery Box")) {
							this.MBoxSigns.put(event.getClickedBlock().getLocation(), this.commandMap.get(player.getName()));
							player.sendMessage("Added a box sign!");
						}
						else {
							player.sendMessage("Need to select a box sign!");
						}
					}
					else {
						player.sendMessage("Need to select a box sign!");
					}
					break;
				}//Write the randomize sequence

				//Save Mystery Box Sign
				List<String> temp = new ArrayList<>();
				for (Map.Entry<Location, String> strloc : this.MBoxSigns.entrySet()) {
					temp.add(parseToStr(strloc.getKey()));
				}
				getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + this.commandMap.get(player.getName()) + ".mboxsigns", temp);
				saveCustomConfig();
				this.add.remove(player.getName());
				this.commandMap.remove(player.getName());				
				player.sendMessage(this.lang.strings.get(41));
				break;
			
			case 15://Add Navigation Door
				player.sendMessage("Hey you should probably program this in instead of leaving a broken command");
				this.add.remove(player.getName());
				this.commandsubwaycolor.remove(player.getName());
				this.commandMap.remove(player.getName());
				break;
			}

			switch (OPremove) {
			case 0://Remove Lobby Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeLobby(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(33));
					break;
				}
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 1://Remove Spectate Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeSpec(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(34));
					break;
				}
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 2://Remove Lightning Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeLight(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(35));
					break;
				}
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 3://Remove Zombie Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeSpawn(block, this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(36));
					break;
				}
				this.spawn.hideSpawns(this.commandMap.get(player.getName()));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 4://Remove Fire Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeFire(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(37));
					break;
				}
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 5://Remove Door Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeDoor(block.getLocation());
					player.sendMessage(this.lang.strings.get(38));
					break;
				}
				this.door.hideDoors(this.commandMap.get(player.getName()));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;

			case 7://Remove Death Spawn??
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeDeath(block.getLocation(), this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(40));
					break;
				}
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				break;

			case 8://Remove Barricade Spawn
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeBar(block, this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(48));
					break;
				}
				this.bar.hideBarricades(this.commandMap.get(player.getName()));
				this.remove.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				break;
			case 9://Remove Zombie Navigation Node
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					removeNavigationNode(block, this.commandMap.get(player.getName()));
					player.sendMessage(this.lang.strings.get(79));
					break;
				}
				this.navigationnode.hideNavigationNodes(this.commandMap.get(player.getName()));
				this.add.remove(player.getName());
				player.sendMessage(this.lang.strings.get(41));
				player.getInventory().clear(0);
				break;
			case 14://Remove Mystery Box Sign
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					if (event.getClickedBlock().getState() instanceof Sign) {
						Sign s = (Sign)event.getClickedBlock().getState();
						if(s.getLine(1).equalsIgnoreCase("§lMystery Box")) {
							this.MBoxSigns.remove(event.getClickedBlock().getLocation());
							player.sendMessage("Removed a box sign!");
						}
						else {
							player.sendMessage("Need to select a box sign!");
						}
					}
					else {
						player.sendMessage("Need to select a box sign!");
					}
					break;
				}//Write the randomize sequence

				//Save Mystery Box Sign
				List<String> temp = new ArrayList<>();
				for (Map.Entry<Location, String> strloc : this.MBoxSigns.entrySet()) {
					temp.add(parseToStr(strloc.getKey()));
				}
				getCustomConfig().set(String.valueOf(player.getWorld().getName()) + "." + this.commandMap.get(player.getName()) + ".mboxsigns", temp);
				saveCustomConfig();
				this.remove.remove(player.getName());
				this.commandMap.remove(player.getName());				
				player.sendMessage(this.lang.strings.get(41));
				break;
			}
		}
	}
	
	public ItemStack BuildTool(String name) {
		ItemStack i = new ItemStack(Material.IRON_SWORD);
		ItemMeta imeta = i.getItemMeta();
		imeta.setDisplayName(name);
		i.setItemMeta(imeta);
		return i;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null && !(event.getClickedBlock() instanceof Sign) && dead.get(player.getName()) == null) {
			if (pmethods.inGame(player) && games.getState(pmethods.playerGame(player)) > 1) {
				CustomPlayerData custompdata = cpdata.get(player.getUniqueId());
				if(event.getItem().getType() == Material.WOODEN_HOE) {
					weapons.weaponUsed(event, custompdata, 0, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.STONE_HOE) {
					weapons.weaponUsed(event, custompdata, 1, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.IRON_HOE) {
					weapons.weaponUsed(event, custompdata, 2, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.GOLDEN_HOE) {
					weapons.weaponUsed(event, custompdata, 3, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.DIAMOND_HOE) {
					weapons.weaponUsed(event, custompdata, 4, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.NETHERITE_HOE) {
					weapons.weaponUsed(event, custompdata, 5, (Plugin) this);
				}
			}
		}
		else if((event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getItem() != null && !(event.getClickedBlock() instanceof Sign) && dead.get(player.getName()) == null) {
			if (pmethods.inGame(player) && games.getState(pmethods.playerGame(player)) > 1) {
				CustomPlayerData custompdata = cpdata.get(player.getUniqueId());
				if(event.getItem().getType() == Material.WOODEN_HOE) {
					weapons.weaponUsed(event, custompdata, 6, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.STONE_HOE) {
					weapons.weaponUsed(event, custompdata, 7, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.IRON_HOE) {
					weapons.weaponUsed(event, custompdata, 8, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.GOLDEN_HOE) {
					weapons.weaponUsed(event, custompdata, 9, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.DIAMOND_HOE) {
					weapons.weaponUsed(event, custompdata, 10, (Plugin) this);
				}
				else if(event.getItem().getType() == Material.NETHERITE_HOE) {
					weapons.weaponUsed(event, custompdata, 11, (Plugin) this);
				}
			}
		}
		else if (event.getAction() == Action.RIGHT_CLICK_BLOCK && player.hasPermission("zs.signs")) {
			Block block = event.getClickedBlock();
			String map = pmethods.playerGame(player);
			if(block.getType() == Material.LEVER){
				if (pmethods.inGame(player) && games.getState(pmethods.playerGame(player)) > 1) {
					int radius = 1;
				    for (int x = radius; x >= -radius; x--) {
				        for (int y = radius; y >= -radius; y--) {
				            for (int z = radius; z >= -radius; z--) {
				            	if(block.getRelative(x,y,z).getState() instanceof Sign) {
									Sign s = (Sign) block.getRelative(x, y, z).getState();
									if (s.getLine(0).equalsIgnoreCase("§4[zombie survival]") && s.getLine(1).equalsIgnoreCase("§lpower") && s.getLine(3).isEmpty()){
										if(this.power.get(map)) {//If Power is Enabled
											event.setCancelled(true);
										}
										else {
											this.power.replace(map, true);
											this.powerswitches.put(map, block);
											this.powerswitchdata.put(map, block.getBlockData());
											for (String p : pmethods.playersInMap(map)) {
												Player ply = Bukkit.getPlayer(p);
												ply.sendTitle("", ChatColor.GREEN + "Power has been activated!", 5, 30, 5);
											}
										}
									}
									else if (s.getLine(0).equalsIgnoreCase("§4[zombie survival]") && s.getLine(1).equalsIgnoreCase("§lpower") && !s.getLine(3).isEmpty()){
										int price = 0;
										try {
											price = Integer.parseInt(s.getLine(3).substring(1));
										}
										catch(Exception e) {
											player.sendMessage(this.lang.strings.get(48));
										}
										if(this.power.get(map)) {//If Power is Enabled
											event.setCancelled(true);
										}
										else if (stats.getSesPoints(player.getName()) >= price) {
											this.power.replace(map, true);
											this.powerswitches.put(map, block);
											this.powerswitchdata.put(map, block.getBlockData());
											stats.removePoints(player.getName(), price);
											updateScoreboardInGame(player, map, 4);
											for (String p : pmethods.playersInMap(map)) {
												Player ply = Bukkit.getPlayer(p);
												ply.sendTitle("", ChatColor.GREEN + "Power has been activated!", 5, 30, 5);
											}
										}
										else if (stats.getSesPoints(player.getName()) < price) {
											event.setCancelled(true);
											player.sendMessage("Not enough money to purchase!");
										}
									}
								}
				            }
				        }
				    }
				}
			}
			else if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				String[] lines = sign.getLines();
				if (pmethods.inGame(player) && games.getState(pmethods.playerGame(player)) > 1) {
					if (lines.length < 1) {
						return;
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[2].equalsIgnoreCase("§2upgrade class")) {
						if (this.playerupgradelevel.get(player.getName()) != null && classes.isClassed(player)) {
							if (this.playerupgradelevel.get(player.getName()) != 4) {
								int cost2open = playerupgradeint.get(player.getName());
								if (stats.getSesPoints(player.getName()) >= cost2open) {
									try {
										stats.removePoints(player.getName(), cost2open);
										updateScoreboardInGame(player, map, 4);
										String formatmessage = classes.ApplyUpgrades(player, playerupgradelevel, cpdata.get(player.getUniqueId()));
										this.playerupgradelevel.replace(player.getName(), this.playerupgradelevel.get(player.getName()) + 1);
										this.playerupgradeint.replace(player.getName(), this.playerupgradeint.get(player.getName()) + upgradeclassincrease);
										player.sendMessage("§m§7================");
										player.sendMessage("§eClass Upgraded!");
										player.sendMessage("§7You now have: ");
										String[] msgs = formatmessage.split(":");
										for(int i = 0; i < msgs.length; i++) {
											String[] msg = msgs[i].split(",");
											if(msg[0].equals("Effect")) {
												player.sendMessage("Effect " + msg[1] + " at power level: " + msg[2]);
											}
											else if(msg[0].equals("Repair")) {
												player.sendMessage("§7- The ability to repair: " + msg[1] + " each round");
											}
											else if(msg[0].equals("Regenerate")) {
												if(msg.length > 3) {//If Potion
													player.sendMessage("§7- The ability to regenerate: " + msg[2] + " " + msg[6] + " each round");
												}
												if(msg.length == 3) {//If Item
													player.sendMessage("§7- The ability to regenerate: " + msg[1] + " " + msg[2]  + " each round");
												}											
											}
										}
										if(this.playerupgradelevel.get(player.getName()) != 4) {
											player.sendMessage("§o§cNext Upgrade will cost: " + Integer.toString(cost2open + upgradeclassincrease));
											player.sendMessage("§m§7================");
										}
										else {
											player.sendMessage("§o§cYou have reached max level!");
											player.sendMessage("§m§7================");
										}
										
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									player.sendMessage("§cUpgrade costs " + cost2open);
								}
							} else {
								player.sendMessage("§cYou are already at max level!");
							}
						} else {
							player.sendMessage("§cYou need to choose a class at spawn in order to upgrade!");
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§ldoor")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								Location signlocation = sign.getLocation();
								if (doorsbought.get(lines[2].split(":")[0]) != null && !doorsbought.get(lines[2].split(":")[0]).contains(Integer.parseInt(lines[2].split(":")[1]))) {
									stats.removePoints(player.getName(), cost2open);
									updateScoreboardInGame(player, map, 4);
									openDoors(signlocation, lines[2].split(":")[0]);
									doorsbought.get(lines[2].split(":")[0]).add(Integer.parseInt(lines[2].split(":")[1]));
									player.sendMessage(String.valueOf(this.lang.strings.get(51)) + Integer.toString(cost2open) + ChatColor.DARK_RED + " dollars!");
								}
								else {
									player.sendMessage("That door has already been unlocked!");
								}
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lunlock")) {
						try {
							if (main.Maps.containsKey(lines[2]) && this.teleportlist.get(lines[2]).areAllTeleportersActive()) {
								Location signlocation = sign.getLocation();
								openDoors(signlocation, lines[2]);
								player.sendMessage("Pack-A-Punch unlocked!");
							}
							else {
								player.sendMessage("You need to link the teleporters first!");
							}									
						} catch (Exception e) {
							e.printStackTrace();
							player.sendMessage(this.lang.strings.get(48));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lbuyable end")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								if(this.Maps.containsKey(lines[2])) {
									for(Player p : Bukkit.getOnlinePlayers()) {
										if(games.getPlayerMap(p.getName()) != null) {
											p.performCommand("bsapl");
											p.sendTitle("", ChatColor.GREEN + "You have escaped the zombies!", 5, 80, 5);
										}
										p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Players have escaped the apocalypse in " + map);
									}
								}
								else {
									player.sendMessage(this.lang.strings.get(49));
								}
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lmachine")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open && this.Machines.containsValue(lines[2])) {
							try {
								if(main.Machines.get(0).equals(lines[2]) && !cpdata.get(player.getUniqueId()).hasMachine(0)) {
									if(this.power.get(map)) {
										player.sendMessage("Bought quick trigger");
										cpdata.get(player.getUniqueId()).activateMachine(0);
										stats.removePoints(player.getName(), cost2open);
										updateScoreboardInGame(player, map, 4);
									}
									else {
										player.sendMessage("Need to enable power first!");
									}
								}
								else if(main.Machines.get(1).equals(lines[2]) && !cpdata.get(player.getUniqueId()).hasMachine(1)) {
									if(this.power.get(map)) {
										player.sendMessage("Bought extra health");
										cpdata.get(player.getUniqueId()).activateMachine(1);
										player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + 10);
										stats.removePoints(player.getName(), cost2open);
										updateScoreboardInGame(player, map, 4);
									}
									else {
										player.sendMessage("Need to enable power first!");
									}
								}
								else if(main.Machines.get(2).equals(lines[2]) && !cpdata.get(player.getUniqueId()).hasMachine(2)) {
									if(this.power.get(map)) {
										player.sendMessage("Bought quick revive");
										player.sendMessage("Quick Revive is currently broken.");
										cpdata.get(player.getUniqueId()).activateMachine(2);
										stats.removePoints(player.getName(), cost2open);
										updateScoreboardInGame(player, map, 4);
									}
									else {
										player.sendMessage("Need to enable power first!");
									}
								}
								else if(main.Machines.get(3).equals(lines[2]) && !cpdata.get(player.getUniqueId()).hasMachine(3)) {
									if(this.power.get(map)) {
										player.sendMessage("Bought fast reload");
										cpdata.get(player.getUniqueId()).activateMachine(3);
										stats.removePoints(player.getName(), cost2open);
										updateScoreboardInGame(player, map, 4);
									}
									else {
										player.sendMessage("Need to enable power first!");
									}
								}
								else {
									player.sendMessage("You alright bought this machine!");
								}
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lshotgun")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.STONE_HOE);
								ItemMeta ibmeta = ibought.getItemMeta();
								ibmeta.setDisplayName("Shotgun");
								ibought.setItemMeta(ibmeta);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought shotgun");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lrifle")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.IRON_HOE);
								ItemMeta ibmeta = ibought.getItemMeta();
								ibmeta.setDisplayName("Rifle");
								ibought.setItemMeta(ibmeta);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought rifle");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lsniper")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.GOLDEN_HOE);
								ItemMeta ibmeta = ibought.getItemMeta();
								ibmeta.setDisplayName("Sniper");
								ibought.setItemMeta(ibmeta);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought sniper");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lhmg")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.DIAMOND_HOE);
								ItemMeta ibmeta = ibought.getItemMeta();
								ibmeta.setDisplayName("Heavy Machine Gun");
								ibought.setItemMeta(ibmeta);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought heavy machine gun");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lammunition")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.ARROW);
								ibought.setAmount(64);
								ItemMeta ibmeta = ibought.getItemMeta();
								ibmeta.setDisplayName("Ammo");
								ibought.setItemMeta(ibmeta);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought ammunition");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lbow")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.BOW);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought bow");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lcrossbow")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.CROSSBOW);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought crossbow");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lstone axe")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.STONE_AXE);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought stone axe");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§liron axe")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.IRON_AXE);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought iron axe");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lgolden axe")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.GOLDEN_AXE);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought golden axe");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§ldiamond axe")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.DIAMOND_AXE);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought diamond axe");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lwooden sword")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.WOODEN_SWORD);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought wooden sword");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lstone sword")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.STONE_SWORD);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought stone sword");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§liron sword")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.IRON_SWORD);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought iron sword");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lgolden sword")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.GOLDEN_SWORD);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought golden sword");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
					if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§ldiamond sword")) {
						int cost2open = 0;
						try {
							cost2open = Integer.parseInt(lines[3].substring(1));
						} catch (Exception e) {
							player.sendMessage(this.lang.strings.get(48));
						}
						if (stats.getSesPoints(player.getName()) >= cost2open) {
							try {
								stats.removePoints(player.getName(), cost2open);
								updateScoreboardInGame(player, map, 4);
								ItemStack ibought = new ItemStack(Material.DIAMOND_SWORD);
								player.getInventory().addItem(ibought);
								player.sendMessage("Bought diamond sword");
							} catch (Exception e) {
								e.printStackTrace();
								player.sendMessage(this.lang.strings.get(48));
							}
						} else {
							player.sendMessage(this.lang.strings.get(49));
						}
					}
				}
				else if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§1buy class")) {
					int cost2open = 0;
					try {
						cost2open = Integer.parseInt(lines[3].substring(1));
					} catch (Exception e) {
						player.sendMessage(this.lang.strings.get(48));
					}
					if (stats.getTotalPoints(player.getName()) >= cost2open && classes.doesClassExist(lines[2])) {
						try {
							stats.removeTotalPoints(player.getName(), cost2open);
							stats.buyClass(player.getName(), lines[2]);
							player.sendMessage("Unlocked class: " + lines[2]);
						} catch (Exception e) {
							e.printStackTrace();
							player.sendMessage(this.lang.strings.get(48));
						}
					} else {
						player.sendMessage(this.lang.strings.get(49));
					}
				}
				else if (lines[0].equalsIgnoreCase("§dzombie stats") && !lines[1].contains("zombies")) {
					if (this.justleftgame.containsKey(player.getName())) {
						player.sendMessage(String.valueOf(this.lang.strings.get(62))
								+ Integer.toString(this.leavetimer
										- ((Integer) this.justleftgame.get(player.getName())).intValue())
								+ (String) this.lang.strings.get(63));
						return;
					}
					player.performCommand("bsapj " + lines[1]);
				}
			}
			if (block.getState() instanceof Chest) {
				Chest chest = (Chest) block.getState();
				if (pmethods.inGame(player) && !mysteryBox(block.getLocation(), chest, player)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
		if (e.getClickedInventory() instanceof EnchantingInventory) {
			if (e.getSlot() == 1) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void openInventoryEvent(InventoryOpenEvent event) {
		if (event.getPlayer() instanceof Player) {
			Player p = Bukkit.getPlayer(event.getPlayer().getName());
			if (pmethods.inGame(p) && games.getState(pmethods.playerGame(p)) > 1) {
				if (event.getInventory() instanceof DoubleChestInventory) {
					event.setCancelled(true);
				}
				if (event.getInventory() instanceof EnchantingInventory) {
					event.setCancelled(true);
					if(this.power.get(pmethods.playerGame(p))) {
						int cost = 0;
						try {
							int radius = 1;
						    for (int x = radius; x >= -radius; x--) {
						        for (int y = radius; y >= -radius; y--) {
						            for (int z = radius; z >= -radius; z--) {
						            	if(event.getInventory().getLocation().getBlock().getRelative(x,y,z).getState() instanceof Sign) {
											Sign s = (Sign) event.getInventory().getLocation().getBlock().getRelative(x,y,z).getState();
											if(s.getLine(1).equals("§lPack-A-Punch")) {
												if (s.getLine(3).contains("$")) {
													cost = Integer.parseInt(s.getLine(3).substring(1));
													if (stats.getSesPoints(p.getName()) >= cost) {
														p.sendMessage(ChatColor.MAGIC + "|||" + ChatColor.RESET + "" + ChatColor.ITALIC + "" + ChatColor.GREEN + "WEAPON UPGRADED!" + ChatColor.RESET + "" + ChatColor.MAGIC + "|||");
														Material clickeditem = event.getPlayer().getItemInHand().getType();													
														String pitem = event.getPlayer().getItemInHand().getType().toString();
														ItemStack handitem = event.getPlayer().getItemInHand();
														CustomPlayerData plydata = this.cpdata.get(p.getName());
														switch(pitem) {
														case "WOODEN_SWORD":
															handitem.addEnchantment(Enchantment.FIRE_ASPECT, 2);
															handitem.addEnchantment(Enchantment.SWEEPING_EDGE, 3);
															handitem.addEnchantment(Enchantment.DURABILITY, 3);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "STONE_SWORD":
															handitem.addEnchantment(Enchantment.FIRE_ASPECT, 2);
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 2);
															handitem.addEnchantment(Enchantment.SWEEPING_EDGE, 2);
															handitem.addEnchantment(Enchantment.DURABILITY, 2);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "IRON_SWORD":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 2);
															handitem.addEnchantment(Enchantment.SWEEPING_EDGE, 3);
															handitem.addEnchantment(Enchantment.DURABILITY, 2);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "GOLDEN_SWORD":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 5);
															handitem.addEnchantment(Enchantment.MENDING, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "DIAMOND_SWORD":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 4);
															handitem.addEnchantment(Enchantment.DURABILITY, 2);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "NETHERITE_SWORD":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 2);
															handitem.addEnchantment(Enchantment.KNOCKBACK, 2);
															handitem.addEnchantment(Enchantment.MENDING, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "STONE_AXE":
															handitem.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
															handitem.addEnchantment(Enchantment.DURABILITY, 2);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "IRON_AXE":
															handitem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 2);
															handitem.addEnchantment(Enchantment.DURABILITY, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "GOLDEN_AXE":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 3);
															handitem.addUnsafeEnchantment(Enchantment.MENDING, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "DIAMOND_AXE":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 1);
															handitem.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
															handitem.addEnchantment(Enchantment.DURABILITY, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															break;
														case "NETHERITE_AXE":
															handitem.addEnchantment(Enchantment.DAMAGE_ALL, 3);
															handitem.addEnchantment(Enchantment.DURABILITY, 1);
															p.playSound(p.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 1);
															stats.removePoints(p.getName(), cost);
															updateScoreboardInGame(p, pmethods.playerGame(p), 4);
															break;
														case "WOODEN_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Pistol");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														case "STONE_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Shotgun");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														case "IRON_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Rifle");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														case "GOLDEN_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Sniper");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														case "DIAMOND_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Heavy Machine Gun");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														case "NETHERITE_HOE":
															if(!plydata.papIsActivated(pitem)) {
																plydata.activatepap(pitem);
																p.sendMessage("Ray Gun");
																break;
															}
															else {
																p.sendMessage("Upgrade limit reached on this weapon!");
																break;
															}
														default:
															p.sendMessage(pitem);
															p.sendMessage("Upgrade limit reached on this weapon!");
															break;
														}
														/*
														ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI);
														lapis.setAmount(3);
														event.getInventory().setItem(1, lapis);
														econ.withdrawPlayer(p.getName(), cost);
														*/
													}
													else {
														p.sendMessage("Not enough money to purchase!");
														event.setCancelled(true);
													}
												}
											}
											else {
												event.setCancelled(true);
											}
						            	}
						            }
						        }
						    }
						}
						catch(Exception e){
							p.sendMessage(this.lang.strings.get(48));
						}
					}
					else {
						p.sendMessage("Power needs to be activated first!");
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void closeInventoryEvent(InventoryCloseEvent event) {
		if (event.getInventory() instanceof EnchantingInventory) {
			event.getInventory().clear();
		}
	}
	
	@EventHandler
	public void onPlayerChangeSelected(PlayerItemHeldEvent event) {
		Player ply = event.getPlayer();
		if (pmethods.inGame(ply) && games.getState(pmethods.playerGame(ply)) > 1) {
			int previousslot = event.getPreviousSlot();
			int newslot = event.getNewSlot();
			PlayerInventory inv = ply.getInventory();
			ItemStack previousitem = inv.getItem(previousslot);
			ItemStack newitem = inv.getItem(newslot);
			CustomPlayerData custompdata = cpdata.get(ply.getUniqueId());
			
			weapons.cancelEvents(custompdata);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (pmethods.inGame(player)) {
			String map = pmethods.playerGame(player);
			if (games.getState(map) > 1) {
				if (this.blockbreak.contains(String.valueOf(block.getType()))
						&& !((Map) this.changedBlocks.get(map)).containsKey(block)) {
					((Map<Block, BlockState>) this.changedBlocks.get(map)).put(block, block.getState());
				} else {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Not allowed!");
				}
			}
			if (games.getState(map) == 1 && this.antigrief && !player.isOp()) {
				event.setCancelled(true);
			}
		}
		if (this.Signs.contains(block.getLocation()) && !pmethods.inGame(player) && !event.isCancelled()
				&& player.hasPermission("zs.edit")) {
			this.Signs.remove(block.getLocation());
			saveSigns();
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		Projectile p = e.getEntity();
		if(p instanceof Arrow) {
			if (((Arrow) p).doesBounce()){
				p.getWorld().createExplosion(p.getLocation(), 2.0F, false, false);
			}
			p.remove();
		}
		else {
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (pmethods.inGame(player)) {
			String map = pmethods.playerGame(player);
			if (games.getState(map) > 1) {
				if (this.blockplace.contains(String.valueOf(block.getType()))) {
					((Map<Block, BlockState>) this.placedBlocks.get(map)).put(block, block.getState());
				} else {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Not allowed!");
				}
			}
			if (games.getState(map) == 1 && this.antigrief && !player.isOp()) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		if (pmethods.inGame(player)) {
			String map = pmethods.playerGame(player);
			if (games.getState(map) > 1) {
				if (this.blockbreak.contains(String.valueOf(block.getType()))
						&& !((Map) this.changedBlocks.get(map)).containsKey(block)) {
					((Map<Block, BlockState>) this.changedBlocks.get(map)).put(block, block.getState());
				} else {
					event.setCancelled(true);
				}
			}
			if (games.getState(map) == 1 && this.antigrief && !player.isOp()) {
				event.setCancelled(true);
			}
		}
	}
	
	public static ItemStack createInfoBook() {
		ItemStack ib = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta wb = (BookMeta) ib.getItemMeta();
		wb.setTitle("Info Book");
		wb.setAuthor("Micro230");
		wb.setDisplayName("Info Book");
		List<String> pages = new ArrayList<String>();
		pages.add("How To Play 1/4 \n It is recommended that if you are new, you start a new game."
				+ " Joining a game in progress will be much more difficult.\n"
				+ " The goal is either to win, or get to the highest round you can. Some maps are"
				+ " able to be won while others end at wave 100.");
		pages.add("How To Play 2/4 \n Zombies will start by breaking down the barriers and you"
				+ " can repair them by standing in front of them and crouching. You will be"
				+ " rewarded 5 points per barricade.");
		pages.add("How To Play 3/4 \n You can buy upgrades and weapons throughout the game to"
				+ " consistently progress. You can buy doors to unlock new areas, link teleporters"
				+ " to unlock special rooms, activate the power to activate machines, and more.");
		pages.add("How To Play 4/4 \n When you die, you have 30 seconds for another player in the"
				+ " game to revive you. If the player doesn't revive you in time, you will lose"
				+ " all of your money and be given the starting items. If you are revived, you"
				+ " will lose a bit of money, but you will not lose any items.");
		pages.add("Mystery Box 1/1 \n The mystery box can be opened for a price, and will give you"
				+ " a random utility. Sometimes, the box will teleport to another location and not"
				+ " give you your item. You will not lose any money if this happens. Look for the"
				+ " box that says ACTIVE to open.");
		pages.add("Using Guns 1/1 \n Left click with your weapon to reload, and right"
				+ " click to shoot. To resupply with ammunition, find an ammo sign around the map.");
		pages.add("Unlock Classes 1/2 \n While you play, each kill will reward you with"
				+ " points. When you buy things ingame, you lose money. This does not make you"
				+ " lose money outside the game though, and you can use that money after the game"
				+ " to unlock new classes.");
		pages.add("Unlock Classes 2/2 \n Another way to get classes is to earn them. Some"
				+ " classes cannot be bought. Instead you earn them through the Autorank system."
				+ " The longer you play, the more classes you can unlock.");
		pages.add("How to something \n\n I need to add more here oopsie.");
		wb.setPages(pages);
		ib.setItemMeta(wb);
		return ib;
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if (e.getItemDrop() != null) {
			if (e.getItemDrop().getItemStack().getType().equals(Material.WRITTEN_BOOK) ) {
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamagePvP(EntityDamageByEntityEvent event) {
		Entity ent = event.getEntity();
		Entity dEnt = event.getDamager();
		int entid = ent.getEntityId();
		int dEntid = dEnt.getEntityId();
		Player player = null;

		if (ent instanceof Player && dEnt instanceof Player) {//If Attacking Player
			player = (Player) ent;
			Player damager = (Player) dEnt;
			String map = pmethods.playerGame(player);
			if (pmethods.inGame(player)) {
				if (games.getState(map) > 1
						&& pmethods.playerGame(player).equalsIgnoreCase(pmethods.playerGame(damager))) {
					if (dead.containsKey(damager.getName())) {
						event.setCancelled(true);
					}
					stats.removePoints(damager.getName(), event.getDamage());
					updateScoreboardInGame(player, map, 4);
					damager.sendMessage(ChatColor.RED + "You have been deducted "
							+ Integer.toString((int) event.getDamage()) + " for attacking another survivor!");
				}

				if (games.getState(map) == 1 && this.antigrief) {
					event.setCancelled(true);
				}
			}
		}
		if (this.zombies.containsKey(Integer.valueOf(entid)) && !(dEnt instanceof Player) && !(dEnt instanceof org.bukkit.entity.Projectile) && !(dEnt instanceof org.bukkit.entity.Explosive)) {//If Zombie In-Game & Damager is Not Player
			event.setCancelled(true);
		}
		if (this.zombies.containsKey(Integer.valueOf(entid)) && (dEnt instanceof Player || dEnt instanceof Arrow)) {//If zombie in-game is damaged by player or arrow
			if (dEnt instanceof Arrow) {
				Arrow arrow = (Arrow) dEnt;
				((LivingEntity)ent).setNoDamageTicks(0);
				((LivingEntity)ent).setMaximumNoDamageTicks(0);
				if(arrow.getShooter() instanceof Player) {
					CalcMoneyDamage(event.getDamage(),event.getEntity(),(Player)arrow.getShooter(),ent,dEnt,entid,dEntid);
				}
			} else {
				player = (Player) dEnt;
			}
			if (pmethods.inGame(player) && this.zombies.containsKey(Integer.valueOf(entid))) {
				CalcMoneyDamage(event.getDamage(),event.getEntity(),player,ent,dEnt,entid,dEntid);
				((LivingEntity)ent).setNoDamageTicks(10);
				((LivingEntity)ent).setMaximumNoDamageTicks(10);
			}
		}
		if (ent instanceof Player && this.zombies.containsKey(Integer.valueOf(dEntid))) {
			player = (Player) ent;
			if (pmethods.inGame(player)) {
				String map = pmethods.playerGame(player);
				if (this.perk.getPerk(map) == 1) {
					event.setCancelled(true);
				}
				if (this.damagemulti != 0.0D) {
					event.setDamage(2 + (int) (games.getWave(map) * this.damagemulti));
				}
			}
		}
	}
	
	public void CalcMoneyDamage(double eventdamage, Entity evententity, Player player, Entity ent, Entity dEnt, int entid, int dEntid) {
		LivingEntity lent;
		String map = pmethods.playerGame(player);
		if (((String) this.zombies.get(Integer.valueOf(entid))).equalsIgnoreCase(map) && !dead.containsKey(player.getName())) {//If zombie is in the correct map and the player isn't dead
			double zbiemaxhealth = ((LivingEntity)ent).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
			double zombiecurrenthealth = luaMath.clamp((((LivingEntity)ent).getHealth() - eventdamage), 0.0D, zbiemaxhealth);
			double healthtaken = luaMath.clamp((((LivingEntity)ent).getHealth() - zombiecurrenthealth), 0.0D, zbiemaxhealth);
			double damageamount = luaMath.ConvertRange(healthtaken, 0, zbiemaxhealth, 0, this.maxpoints);
			if (this.zombiescore.get(Integer.valueOf(entid)) == null) {//If the score the zombie holds is null, create damage
				Map<String, Double> temp = new HashMap<>();
				temp.put(player.getName(), damageamount);
				this.zombiescore.put(Integer.valueOf(entid), temp);//Add the player along with the damage he did
			}
			else if (((Map) this.zombiescore.get(Integer.valueOf(entid))).get(player.getName()) == null) {//If the zombie holding score doesn't have the player that did damage
				((Map<String, Double>) this.zombiescore.get(Integer.valueOf(entid))).put(player.getName(), damageamount);//Add the player along with the damage he did
			}
			else {//If the player already did damage and exists in the Java Map
				double olddamg = ((Double) ((Map) this.zombiescore.get(Integer.valueOf(entid))).get(player.getName())).doubleValue();
				double damg = olddamg + damageamount;
				((Map<String, Double>) this.zombiescore.get(Integer.valueOf(entid))).put(player.getName(), damg);
			}
		}
		switch (this.perk.getPerk(map)) {
		case 2:
			if(this.dead.get(player) == null) {
				lent = (LivingEntity) ent;
				EntityDamageEvent dmgevent = new EntityDamageEvent(lent, DamageCause.CUSTOM, lent.getHealth());
				ent.setLastDamageCause(dmgevent);
				((Map<String, Double>) this.zombiescore.get(Integer.valueOf(entid))).put(player.getName(), (double)this.maxpoints);
				if (lent.isValid()) {
					lent.setHealth(0);// Kill him again cuz why not
				}
			}
			break;
		case 3:
			if(this.dead.get(player) == null) {
				evententity.setFireTicks(100);
				break;
			}
		case 5:
			if(this.dead.get(player) == null) {
				ent.setVelocity(player.getLocation().getDirection().multiply(3));
				break;
			}
		}
	}
	
	@EventHandler
	public void onZombieDamage(EntityDamageEvent event) {
		Entity ent = event.getEntity();
		if (!this.zombies.isEmpty() && this.zombies.get(Integer.valueOf(ent.getEntityId())) != null
				&& this.zombies.containsKey(Integer.valueOf(ent.getEntityId()))
				&& games.getState((String) this.zombies.get(Integer.valueOf(ent.getEntityId()))) > 1
				&& !(event instanceof EntityDamageByEntityEvent) && !this.allhurt) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onZombieDeath(EntityDeathEvent event) {
		LivingEntity livingEntity = event.getEntity();
		if (!this.zombies.isEmpty() && this.zombies.get(Integer.valueOf(livingEntity.getEntityId())) != null
				&& games.getState((String) this.zombies.get(Integer.valueOf(livingEntity.getEntityId()))) > 1) {

			String map = this.zombies.get(Integer.valueOf(livingEntity.getEntityId()));
			this.bar.stopzombiebreaking(Integer.valueOf(livingEntity.getEntityId()));
			PayPlayers(livingEntity.getEntityId(), livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
			EntityDamageEvent.DamageCause deathreason = livingEntity.getLastDamageCause().getCause();
			if (!this.drops.isEmpty()) {
				for (ItemStack item : randomItem()) {
					event.getDrops().add(item);//Add dropped item
				}
			}
			else {
				event.getDrops().clear();//Clear all dropped items
			}
			event.setDroppedExp(0);
			if (this.fastzombies.containsKey(Integer.valueOf(livingEntity.getEntityId()))) {
				this.fastzombies.remove(Integer.valueOf(livingEntity.getEntityId()));
			}
			if (this.tankzombies.containsKey(Integer.valueOf(livingEntity.getEntityId()))) {
				this.tankzombies.remove(Integer.valueOf(livingEntity.getEntityId()));
			}
			if (this.kingzombies.containsKey(Integer.valueOf(livingEntity.getEntityId()))) {
				this.kingzombies.remove(Integer.valueOf(livingEntity.getEntityId()));
				int kingbabies = luaMath.random(1, 2);
				if(kingbabies == 1) {
					for(int i=0; i < 4; i++) {
						World world = Bukkit.getWorld(Maps.get(map));
						Entity ent = world.spawnEntity(livingEntity.getLocation(), EntityType.ZOMBIE);
						this.justSpawned.put(ent.getEntityId(), false);
						this.zombiewasat.put(ent.getEntityId(), 0);
						this.persuingNeutral.put(ent.getEntityId(), false);
						Zombie z = (Zombie) ent;
						z.setBaby();
						LivingEntity lent = (LivingEntity) ent;
						lent.getEquipment().clear();
						AttributeInstance healthAttribute = lent.getAttribute(Attribute.GENERIC_MAX_HEALTH);
						healthAttribute.setBaseValue(8);
						lent.setHealth(8);
						lent.setRemoveWhenFarAway(false);
						this.zombies.put(Integer.valueOf(ent.getEntityId()), map);
						this.fastzombies.put(Integer.valueOf(ent.getEntityId()), map);
						games.setZcount(map, games.getZcount(map) + 1);
						games.setZslayed(map, games.getZslayed(map) - 1);
						this.maxzombiesingame.put(map, this.maxzombiesingame.get(map) + 1);
					}
				}
			}
			if (this.respawn && deathreason != EntityDamageEvent.DamageCause.ENTITY_ATTACK && deathreason != EntityDamageEvent.DamageCause.PROJECTILE) {
				games.setZcount(map, games.getZcount(map) - 1);
			} else {
				games.setZslayed(map, games.getZslayed(map) + 1);
				this.maxzombiesingame.put(map, this.maxzombiesingame.get(map) - 1);
			}
			this.zombies.remove(Integer.valueOf(livingEntity.getEntityId()));
			
			int dropPerkItem = luaMath.random(0, 80);
			if(dropPerkItem == 0) {
				if (games.getState(map) == 2 && this.perk.getPerk(map) == 0) {
					this.perk.callPerk(map, event.getEntity().getLocation());
					this.perkend.put(map, Boolean.valueOf(false));
				}	
			}
			
			if (games.getZslayed(map) >= ((Integer) this.wavemax.get(map)).intValue()) {
				getLogger().info(String.valueOf(map) + " slayed: " + Integer.toString(games.getZslayed(map))
						+ " wavemax: " + Integer.toString(((Integer) this.wavemax.get(map)).intValue()));
				NewWave(map);
			}
			for(String p : pmethods.playersInMap(map)) {
				Player ply = Bukkit.getPlayer(p);
				updateScoreboardInGame(ply, map, 2);
			}
			if (livingEntity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent entitykiller = (EntityDamageByEntityEvent) livingEntity.getLastDamageCause();
				if (entitykiller.getDamager() instanceof Player || entitykiller.getDamager() instanceof Arrow) {
					Player player = null;
					if (entitykiller.getDamager() instanceof Arrow) {
						Arrow arrow = (Arrow) entitykiller.getDamager();
						if (arrow.getShooter() instanceof Player) {
							player = (Player) arrow.getShooter();
						}
					} else {
						player = (Player) entitykiller.getDamager();
					}
					try {
						if (pmethods.playerGame(player).equalsIgnoreCase(map)) {
							stats.addKills(player.getName(), 1.0D);
							updateScoreboardInGame(player, map, 3);
							updateScoreboardInGame(player, map, 4);
						}
					} catch (Exception e) {
						if (player != null) {
							player.sendMessage("Naughty boy, back to spawn!");
							player.teleport(player.getWorld().getSpawnLocation());
							games.removePlayerMap(player.getName());
							stats.removeSplayerKills(player.getName());
							dead.remove(player.getName());
							stats.setPoints(player.getName(), 0);
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onSignBreak(BlockBreakEvent e) {
		Player ply = e.getPlayer();
		if (pmethods.inGame(ply) && games.getState(pmethods.playerGame(ply)) > 1) {
			if (e.getBlock().getState() instanceof Sign) {
				Sign s = (Sign) e.getBlock().getState();
				Player revivedplayer = Bukkit.getPlayer(s.getLine(1).split("1")[1]);
				ItemStack[] inventory = cpdata.get(revivedplayer.getUniqueId()).getDeathInventory();
				ItemStack[] armor = cpdata.get(revivedplayer.getUniqueId()).getDeathArmor();
				this.revive.onSignBreak(e, inventory, armor);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player killed = event.getEntity();
		if (pmethods.inGame(killed)) {
			String map = pmethods.playerGame(killed);
			this.cpdata.get(killed.getUniqueId()).setDeathInventory(killed.getInventory().getContents());
			this.cpdata.get(killed.getUniqueId()).setDeathArmor(killed.getInventory().getArmorContents());
			getLogger().info(String.valueOf(killed.getName()) + " has died! Was in game: " + map);
			event.getDrops().clear();
			games.setPcount(map, games.getPcount(map) - 1);
			dead.put(killed.getName(), map);
			this.revive.createRevive(killed);
			stats.addDeaths(killed.getName(), 1.0D);
			if (this.deathloss > 0.0D) {
				double original = stats.getSesPoints(killed.getName());
				double withdraw = original * this.deathloss;
				stats.removePoints(killed.getName(), withdraw);
				updateScoreboardInGame(killed, map, 4);
			}
			if (killed.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent killer = (EntityDamageByEntityEvent) killed.getLastDamageCause();
				if (killer.getDamager() instanceof org.bukkit.entity.Zombie) {
					killed.sendMessage(ChatColor.DARK_PURPLE + "You Died Nobly!");
				}
				if (killer.getDamager() instanceof Player) {
					killed.sendMessage(ChatColor.DARK_PURPLE + "You Died By Treason!");
				}
			} else {
				killed.sendMessage(ChatColor.DARK_PURPLE + "You Died!");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		final String name = player.getName();
		Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
			public void run() {
				if (pmethods.inGame(player)) {
					String map = pmethods.playerGame(player);
					main.this.GamesOver(map, Boolean.valueOf(false));
					if (games.getState(map) > 1 && main.dead.containsKey(name)) {
						if (main.this.spawn.spectate.get(map) != null && main.this.spectateallow) {
							player.teleport(main.this.spawn.spectate.get(map));
							player.setAllowFlight(true);
							player.setFlying(true);
							spectate.spectators.put(name, map);
							utilities.hidePlayer(player);
							player.getInventory().clear();
							player.getInventory().setArmorContents(null);
						} else if (main.this.deathPoints.get(map) != null && !main.this.spectateallow) {
							player.teleport(main.this.deathPoints.get(map));
						} else {
							player.teleport(player.getWorld().getSpawnLocation());
							player.sendMessage("No waiting lobby defined and spectating disabled. Back to spawn!");
						}
					}
				}
			}
		});
	}
	
	public boolean isZombieSurvivalSign(String s) {
		if((s.equalsIgnoreCase("zombiesurvival") || s.equalsIgnoreCase("zs") || s.equalsIgnoreCase("zombie"))) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		String[] lines = event.getLines();
		if (lines[0].equalsIgnoreCase("zombie stats") && (Maps.containsKey(lines[1]) || Maps.containsKey(lines[2]))) {
			Player player = event.getPlayer();
			event.setLine(0, "§4Zombie Stats");
			if (player.hasPermission("zs.edit")) {
				Location sloc = event.getBlock().getLocation();
				this.Signs.add(sloc);
				saveSigns();
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (lines[0].equalsIgnoreCase("zombie stats")) {
			event.setLine(0, "§4BAD SIGN");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("door") && Maps.containsKey(lines[2].split(":")[0]) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lDoor");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("door")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("machine") && Machines.containsValue(lines[2]) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lMachine");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("machine")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("unlock") && Maps.containsKey(lines[2])) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lUnlock");
			if (!lines[3].isEmpty() && !lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("unlock")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("power") && lines[2].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lPower");
			if (!lines[3].isEmpty() && !lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("power")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ending") || lines[1].equalsIgnoreCase("end") || lines[1].equalsIgnoreCase("buyable end")) && Maps.containsKey(lines[2]) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lBuyable End");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ending") || lines[1].equalsIgnoreCase("end") || lines[1].equalsIgnoreCase("buyable end"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("shotgun") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lShotgun");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("shotgun")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("rifle") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lRifle");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("rifle")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("sniper") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lSniper");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("sniper")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("hmg") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lHMG");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("hmg")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("packapunch") || lines[1].equalsIgnoreCase("pack") || lines[1].equalsIgnoreCase("punch")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lPack-A-Punch");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("packapunch") || lines[1].equalsIgnoreCase("pack") || lines[1].equalsIgnoreCase("punch"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ammo") || lines[1].equalsIgnoreCase("ammunition")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lAmmunition");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ammo") || lines[1].equalsIgnoreCase("ammunition"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("bow") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lBow");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("bow")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("bow") && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lCrossbow");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].equalsIgnoreCase("crossbow")) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("stoneaxe") || lines[1].equalsIgnoreCase("stone axe") || lines[1].equalsIgnoreCase("stone_axe")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lStone Axe");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("stoneaxe") || lines[1].equalsIgnoreCase("stone axe") || lines[1].equalsIgnoreCase("stone_axe"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ironaxe") || lines[1].equalsIgnoreCase("iron axe") || lines[1].equalsIgnoreCase("iron_axe")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lIron Axe");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ironaxe") || lines[1].equalsIgnoreCase("iron axe") || lines[1].equalsIgnoreCase("iron_axe"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("goldenaxe") || lines[1].equalsIgnoreCase("golden axe") || lines[1].equalsIgnoreCase("golden_axe")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lGolden Axe");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("goldenaxe") || lines[1].equalsIgnoreCase("golden axe") || lines[1].equalsIgnoreCase("golden_axe"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("diamondaxe") || lines[1].equalsIgnoreCase("diamond axe") || lines[1].equalsIgnoreCase("diamond_axe")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lDiamond Axe");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("diamondaxe") || lines[1].equalsIgnoreCase("diamond axe") || lines[1].equalsIgnoreCase("diamond_axe"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("woodensword") || lines[1].equalsIgnoreCase("wooden sword") || lines[1].equalsIgnoreCase("wooden_sword")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lWooden Sword");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("woodensword") || lines[1].equalsIgnoreCase("wooden sword") || lines[1].equalsIgnoreCase("wooden_sword"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("stonesword") || lines[1].equalsIgnoreCase("stone sword") || lines[1].equalsIgnoreCase("stone_sword")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lStone Sword");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("stonesword") || lines[1].equalsIgnoreCase("stone sword") || lines[1].equalsIgnoreCase("stone_sword"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ironsword") || lines[1].equalsIgnoreCase("iron sword") || lines[1].equalsIgnoreCase("iron_sword")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lIron Sword");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("ironsword") || lines[1].equalsIgnoreCase("iron sword") || lines[1].equalsIgnoreCase("iron_sword"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("goldensword") || lines[1].equalsIgnoreCase("golden sword") || lines[1].equalsIgnoreCase("golden_sword")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lGolden Sword");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("goldensword") || lines[1].equalsIgnoreCase("golden sword") || lines[1].equalsIgnoreCase("golden_sword"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("diamondsword") || lines[1].equalsIgnoreCase("diamond sword") || lines[1].equalsIgnoreCase("diamond_sword")) && !lines[3].isEmpty()) {
			Player player = event.getPlayer();
			event.setLine(0, "§4[Zombie Survival]");
			event.setLine(1, "§lDiamond Sword");
			if (!lines[3].contains("$")) {
				event.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				event.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("diamondsword") || lines[1].equalsIgnoreCase("diamond sword") || lines[1].equalsIgnoreCase("diamond_sword"))) {
			event.setLine(0, "§4BAD FORMAT");
		}
	}
	
	

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.onlinep++;
		Player player = event.getPlayer();
		String name = player.getName();
		
		this.autorank.addOnlinePlayer(player);
		if(!this.autorank.players.contains(name)) {
			this.autorank.addPlayer(name);
		}
		
		CreateScoreboard(player);
		
		if (!pmethods.inGame(player)) {
			if (this.invsave) {
				try {
					player.getInventory().setContents(((pData) this.playerdata.get(name)).inventory);
					player.getInventory().setArmorContents(((pData) this.playerdata.get(name)).armor);
				} catch (Exception exception) {
				}
			}

			stats.setPoints(player.getName(), 0.0D);
			player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
			player.setFoodLevel(20);
			player.setLevel(0);
			player.setExp(0);
			player.setPlayerListName("§3[Lobby] §r" + player.getDisplayName());
			player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
			this.playerupgradelevel.put(player.getName(), 1);
			this.playerupgradeint.put(player.getName(), upgradeclasscost);
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			utilities.unhidePlayer(player);
			if (this.forcespawn) {
				player.teleport(player.getWorld().getSpawnLocation());
			}
		}
		if (this.forceclear && !this.twomintimer.contains(name)) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
		}
		if (pmethods.inGame(player)) {
			String map = pmethods.playerGame(player);
			if (games.getState(map) < 2) {
				player.getInventory().clear();
				player.getInventory().setArmorContents(null);
				player.teleport(player.getWorld().getSpawnLocation());
				games.removePlayerMap(name);
				stats.setPoints(name, 0.0D);
				stats.removeSplayerKills(name);
				player.setPlayerListName("§3[Lobby] §r" + player.getDisplayName());
				dead.remove(name);
				stats.setPoints(player.getName(), 0.0D);
			}
		}

		if (this.antigrief) {
			player.sendMessage(ChatColor.GREEN + "Right click a sign or type /join to join a ZombieSurvival game!");
		}
		player.getInventory().setItem(4, main.createInfoBook());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		this.onlinep--;
		final Player player = event.getPlayer();
		final String name = player.getName();
		this.autorank.removeOnlinePlayer(player);
		this.revive.removeSign(name);
		if (pmethods.inGame(player)) {
			this.twomintimer.add(name);
			final String map = pmethods.playerGame(player);
			if (pmethods.onlinepcount(map) > 1) {
				Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
					public void run() {
						if (!player.isOnline()) {
							if (!main.dead.containsKey(name)) {
								int tempcount = games.getPcount(map);
								games.setPcount(map, tempcount - 1);
							}
							games.removePlayerMap(name);
							stats.removeSplayerPoints(name);
							main.this.twomintimer.remove(name);
							main.dead.remove(name);
							main.this.GamesOver(map, Boolean.valueOf(false));
						}

					}
				}, 1200L);
			} else {
				if (!dead.containsKey(name)) {
					int tempcount = games.getPcount(map);
					games.setPcount(map, tempcount - 1);
				}
				games.removePlayerMap(name);
				stats.removeSplayerPoints(name);
				this.twomintimer.remove(name);
				dead.remove(player.getName());
				GamesOver(map, Boolean.valueOf(false));
			}
		}
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		this.onlinep--;
		final Player player = event.getPlayer();
		final String name = player.getName();
		this.revive.removeSign(name);
		if (pmethods.inGame(player)) {
			this.twomintimer.add(name);
			final String map = pmethods.playerGame(player);
			if (pmethods.onlinepcount(map) > 1) {
				Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {
					public void run() {
						if (!player.isOnline()) {
							if (!main.dead.containsKey(name)) {
								int tempcount = games.getPcount(map);
								games.setPcount(map, tempcount - 1);
							}
							games.removePlayerMap(name);
							stats.removeSplayerPoints(name);
							main.this.twomintimer.remove(name);
							main.dead.remove(name);
							main.this.GamesOver(map, Boolean.valueOf(false));
						}

					}
				}, 200L);
			} else {
				if (!dead.containsKey(name)) {
					int tempcount = games.getPcount(map);
					games.setPcount(map, tempcount - 1);
				}
				games.removePlayerMap(name);
				stats.removeSplayerPoints(name);
				this.twomintimer.remove(name);
				dead.remove(player.getName());
				GamesOver(map, Boolean.valueOf(false));
			}
		}
	}

	@EventHandler
	public void onPlayerPickup(EntityPickupItemEvent event) {
		if(event.getEntity() instanceof Player) {			
			if (dead.containsKey(event.getEntity().getName())) {
				event.setCancelled(true);
			}
			weapons.setNameReturn(event.getItem().getItemStack(), cpdata.get(event.getEntity().getUniqueId()));
		}
	}

	public void joinItems(Player player) {
		if (!player.hasPermission("zs.donator") && this.invsave) {
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
		}
		if (!this.joinitems.isEmpty() && this.itemsatjoin && !classes.isClassed(player)) {
			for (ItemStack item : this.joinarmor) {
				utilities.equipPlayer(player, item, cpdata.get(player.getUniqueId()));
				player.updateInventory();
			}
			for (ItemStack item : this.joinitems) {
				utilities.equipPlayer(player, item, cpdata.get(player.getUniqueId()));
				player.updateInventory();
			}
		}
	}

	public List<ItemStack> randomItem() {
		List<ItemStack> items = new ArrayList<>();
		for (ItemStack i : this.drops.keySet()) {
			double chance = ((Double) this.drops.get(i)).doubleValue();
			double chan = this.random.nextDouble();
			if (chan <= chance) {
				items.add(i);
			}
		}
		return items;
	}

	public void maxfinder(String map) {
		int mz = games.getMaxZombies(map);
		int w = games.getWave(map);
		if (mz < 10) {
			this.wavemax.put(map, Integer.valueOf((int) ((mz * w) * 0.5D)));
		}
		if (mz >= 10 && mz <= 50) {
			this.wavemax.put(map, Integer.valueOf((int) ((mz * w) * 0.1D)));
		}
		if (mz >= 51 && mz <= 100) {
			this.wavemax.put(map, Integer.valueOf((int) ((mz * w) * 0.08D)));
		}
		if (mz >= 101 && mz <= 200) {
			this.wavemax.put(map, Integer.valueOf((int) ((mz * w) * 0.05D)));
		}
		if (mz >= 201) {
			this.wavemax.put(map, Integer.valueOf((int) ((mz * w) * 0.04D)));
		}
		if (((Integer) this.wavemax.get(map)).intValue() < 1) {
			this.wavemax.put(map, Integer.valueOf(1));
		}
	}

	public void SignUpdater() {
		for (Iterator<Location> it = this.Signs.iterator(); it.hasNext();) {
			Location sloc = it.next();
			Block block = sloc.getBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				String[] lines = sign.getLines();
				if (lines[1].contains("zombies")) {
					String map = lines[2];
					if (Maps.containsKey(map) && games.getState(map) > 1) {
						sign.setLine(1,
								"§4" + Integer.toString(games.getZcount(map) - games.getZslayed(map)) + " §0zombies");
						sign.setLine(3, "Wave: " + Integer.toString(games.getWave(map)));
						sign.update();
					} else {
						sign.setLine(1, "no zombies in");
						sign.setLine(3, "not started");
						sign.update();
					}
					if (!Maps.containsKey(map)) {
						block.setType(Material.AIR);
						it.remove();
						saveSigns();
					}
					continue;
				}
				if (Maps.containsKey(lines[1])) {
					sign.setLine(2, "Players: " + Integer.toString(pmethods.numberInMap(lines[1])) + "/"
							+ Integer.toString(games.getMaxPlayers(lines[1])));
					sign.setLine(3, "Wave: " + Integer.toString(games.getWave(lines[1])));
					sign.update();
					continue;
				}
				block.setType(Material.AIR);
				it.remove();
				saveSigns();
			}
		}
	}

	public void saveSigns() {
		List<String> temp = new ArrayList<>();
		for (Location strloc : this.Signs) {
			temp.add(parseToStr(strloc));
		}
		getCustomConfig().set("signs", temp);
		saveCustomConfig();
	}

	public void reloadPlayers() {

		Player[] list = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
		byte b;
		int i;
		Player[] arrayOfPlayer1;
		for (i = (arrayOfPlayer1 = list).length, b = 0; b < i;) {
			Player p = arrayOfPlayer1[b];
			stats.setPoints(p.getName(), 0.0D);
			stats.setKills(p.getName(), 0.0D);
			this.onlinep++;
			b++;
		}

	}

	public void openDoors(Location middle, String map) {
		World world = Bukkit.getWorld(Maps.get(map));
		for (int x = middle.getBlockX() - this.doorfindradius; x <= middle.getBlockX() + this.doorfindradius; x++) {
			for (int y = middle.getBlockY() - this.doorfindradius; y <= middle.getBlockY() + this.doorfindradius; y++) {
				for (int z = middle.getBlockZ() - this.doorfindradius; z <= middle.getBlockZ() + this.doorfindradius; z++) {
					this.door.openDoor(this.door.findDoor(x, y, z));
				}
			}
		}
	}
	
	public static Player getPlayerAroundPosition(Location TravelToLocation) {
		Location l1 = TravelToLocation.add(2,0,2);
		Location l2 = TravelToLocation.add(-2,0,-2);
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
	    int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
	    int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
	    int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
	    int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
	    int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		for(Player p : Bukkit.getOnlinePlayers()) {
		    int x = p.getLocation().getBlockX();
		    int y = p.getLocation().getBlockY();
		    int z = p.getLocation().getBlockZ();
			if ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2) && (z >= z1) && (z <= z2)) {
				return p;
			}
		}
		return null;
	}
	
	public List<Location> NavGetLivingPlayers(String map){
		List<Location> plist = new ArrayList<>();
		for (String pl : pmethods.playersInMap(map)) {
			Player p = Bukkit.getPlayer(pl);
			if (!main.dead.containsKey(pl) && p != null && !p.isDead() && p.getWorld().getName().equalsIgnoreCase(main.Maps.get(map))) {
				plist.add(p.getLocation());
			}
		}
		return plist;
	}
	
	public Location NavGetClosestPlayer(Location zloc, String map) {
		List<Location> plist = NavGetLivingPlayers(map);
		Location closest = main.this.spawn.lobbies.get(map);
		if (plist.size() > 0) {
			closest = plist.get(0);
			double closestDist = 9.99999999999999E14D;
			for (Location locs : plist) {
				double dist = locs.distanceSquared(zloc);
				if (dist < closestDist) {
					closestDist = dist;
					closest = locs;
					if(plist.size() > 1) {
						Location recom = null;
						for(Location playerloc : plist) {
							if(recom == null) {
								recom = playerloc;
							}
							if(playerloc.distanceSquared(zloc) < recom.distanceSquared(zloc)) {
								recom = playerloc;
							}
						}
						return recom;
					}
					return closest;
				}
			}
		}
		return null;
	}
	
	public ZombieRegion NavGetEntityRegion(Location entloc) {
		for(ZombieRegion zr : this.navigationregion.regionList()) {
			if(zr.isIn(entloc)) {
				return zr;
			}
		}
		return null;
	}
	
	public void NavSetZombieTarget(LivingEntity went, Location TravelTo) {
		Zombie obj = (Zombie)went;
		Player ap = getPlayerAroundPosition(TravelTo);
		if(ap != null) {
			obj.setTarget(ap);
		}
	}
	
	public Location NavNodeCode(ZombieRegion zombregion, LivingEntity went, Location closest) {
		NavigationNode neutnode = zombregion.getNeutralNode();//Get Neutral Node
		NavigationNode closestnode = zombregion.getClosestNavNode(went.getLocation());
		Location TravelTo = utilities.getSegment(went, closest);//Chase Player instead of null
		if(neutnode != null) {
			closest = neutnode.location;//Get location of neutral node if it's not null
		}
		TravelTo = utilities.getSegment(went, closest);//Chase neutral node instead
		if(closest.distanceSquared(went.getLocation()) < 1 && persuingNeutral.get(went.getEntityId()) == true) {//Check if distance to the neutral node is small and chasing neutral
			persuingNeutral.replace(went.getEntityId(), false);
			TravelTo = utilities.getSegment(went, closestnode.location);
			return TravelTo;
		}
		NavigationNode pathnode = navigationnode.findNavigationNode(closestnode.subwaycolor, closestnode.subwaynumber);
		if(pathnode != null) {
			if(persuingNeutral.get(went.getEntityId()) == false) {
				TravelTo = utilities.getSegment(went, pathnode.location);
				return TravelTo;
			}
			if(pathnode.location.distanceSquared(went.getLocation()) < 2) {
				if(this.zombiewasat.get(went.getEntityId()) == 0 && closestnode.subwaynumber == 1) {
					pathnode = navigationnode.findNavigationNode(closestnode.subwaycolor, closestnode.subwaynumber+1);
					this.zombiewasat.put(went.getEntityId(), 1);
				}
				else if(this.zombiewasat.get(went.getEntityId()) == 1 && closestnode.subwaynumber == 2) {
					pathnode = navigationnode.findNavigationNode(closestnode.subwaycolor, closestnode.subwaynumber+1);
					this.zombiewasat.put(went.getEntityId(), 2);
				}
				else if(this.zombiewasat.get(went.getEntityId()) == 2 && closestnode.subwaynumber == 3) {
					this.zombiewasat.put(went.getEntityId(), 0);
				}
				else if(this.zombiewasat.get(went.getEntityId()) == 0 && closestnode.subwaynumber == 3) {
					pathnode = navigationnode.findNavigationNode(closestnode.subwaycolor, closestnode.subwaynumber-1);
					this.zombiewasat.put(went.getEntityId(), 3);
				}
				else if(this.zombiewasat.get(went.getEntityId()) == 3 && closestnode.subwaynumber == 2) {
					pathnode = navigationnode.findNavigationNode(closestnode.subwaycolor, closestnode.subwaynumber-1);
					this.zombiewasat.put(went.getEntityId(), 2);
				}
				else if(this.zombiewasat.get(went.getEntityId()) == 2 && closestnode.subwaynumber == 1) {
					this.zombiewasat.put(went.getEntityId(), 0);
				}
				TravelTo = utilities.getSegment(went, pathnode.location);
				return TravelTo;
			}
		}
		return utilities.getSegment(went, closest);
	}
	
	public void RunDamageHealBarricades() {
		this.task4 = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(((Plugin)this), new Runnable() {
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					if (pmethods.inGame(p) && games.getState(pmethods.playerGame(p)) > 1) {
						if(p.isSneaking() && !p.isDead() && !dead.containsKey(p.getName())) {
							String map = pmethods.playerGame(p);
							Material tb = p.getLocation().subtract(0, 1, 0).getBlock().getType();
							if(tb == Material.CRIMSON_PLANKS || tb == Material.CRIMSON_SLAB || tb == Material.CRIMSON_STAIRS) {
								int healreturn = main.this.bar.healBars(p.getLocation(), map);
								if (healreturn == 0 && amountofbarricadesrepaired.get(p.getName()) == null) {
									stats.addPoints(p.getName(), 5);
									amountofbarricadesrepaired.put(p.getName(), 1);
									p.sendMessage(ChatColor.GREEN + "GAINED: " + ChatColor.DARK_RED + 5 + ChatColor.GREEN + " points for repairing barricade.");
									updateScoreboardInGame(p, map, 4);
								}
								else if (healreturn == 0 && amountofbarricadesrepaired.get(p.getName()) < maxbarricadestorepair) {
									stats.addPoints(p.getName(), 5);
									amountofbarricadesrepaired.put(p.getName(), amountofbarricadesrepaired.get(p.getName()) + 1);
									p.sendMessage(ChatColor.GREEN + "GAINED: " + ChatColor.DARK_RED + 5 + ChatColor.GREEN + " points for repairing barricade.");
									updateScoreboardInGame(p, map, 4);
								}
								else if (healreturn == 0) {
									p.sendMessage(ChatColor.GREEN + "GAINED: " + ChatColor.DARK_RED + 0 + ChatColor.GREEN + " points for repairing barricade.");
								}
								else if(healreturn == 1) {
									p.sendMessage(ChatColor.DARK_RED + "Barricade is already repaired!");
									p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6F, 1);
								}
								else if(healreturn == 2) {
									p.sendMessage(ChatColor.DARK_RED + "Zombie is breaking barricade!");
									p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6F, 0.2F);
								}
							}
						}
					}
				}
				for(String map : main.Maps.keySet()) {
					World world = Bukkit.getWorld(main.Maps.get(map));
					List<LivingEntity> templist = main.getLivingEnts(world);
					for (LivingEntity ent : templist) {
						if (ent.isValid()) {
							int id = ent.getEntityId();
							if (main.this.zombies.containsKey(Integer.valueOf(id)) && ((String) main.this.zombies.get(Integer.valueOf(id))).equalsIgnoreCase(map)) {
								Location zombieLocation = ent.getLocation();
								bar.damageBars(zombieLocation, map, id);
							}
						}
					}
				}
			}
		}, 20, 20);
	}

	public void QuickUpdate() {
		this.task2 = getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				for (String map : main.Maps.keySet()) {
					World world = Bukkit.getWorld(main.Maps.get(map));
					if (games.getState(map) > 1 && world != null && games.getPcount(map) > 0) {
						
						List<LivingEntity> templist = main.getLivingEnts(world);
						for (LivingEntity went : templist) {
							if (went.isValid()) {
								int id = went.getEntityId();
								if (main.this.zombies.containsKey(Integer.valueOf(id)) && ((String) main.this.zombies.get(Integer.valueOf(id))).equalsIgnoreCase(map)) {
									Location zombieLocation = went.getLocation();
									//main.this.bar.damageBars(id, zombieLocation, map);
									Location closest = NavGetClosestPlayer(zombieLocation, map);
									if (went.getType() == EntityType.WOLF) {
										Wolf w = (Wolf) went;
										main.this.updateTarget(map, w);
									}
									if (went.getType() == EntityType.ZOMBIE) {
										if(justSpawned.get(went.getEntityId()).booleanValue()) {//If just spawned, go to barricade
											Barricade testbar = bar.getBarricadeLinkedToSpawn((zombiespawnedfrom.get(went.getEntityId())), spawn.getAllSpawns());
											if(testbar == null) {//If barricade is null, return and chase player instead
												justSpawned.replace(went.getEntityId(), false);
												return;
											}
											Location TravelTo = testbar.location;
											if(went.getLocation().distanceSquared(TravelTo) < 8 && bar.allBarricadesGone(testbar.location, map)) {
												justSpawned.replace(went.getEntityId(), false);
											}
											Zombie obj = (Zombie)went;
											obj.setTarget(null);
											
											if (main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId()))&& ((String) main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)) {
												utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) main.this.fastseekspeed);
												continue;
											}
											utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), main.this.mapseekspeed.get(map).floatValue());
										}
										else {
											ZombieRegion zombregion = NavGetEntityRegion(went.getLocation());
											ZombieRegion plyregion = NavGetEntityRegion(closest);
											if(zombregion != null) {
												if(plyregion != null) {
													if(zombregion.getName().equals(plyregion.getName())) {
														Location TravelTo = utilities.getSegment(went, closest);
														NavSetZombieTarget(went, TravelTo);
														if (main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId())) && ((String) main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)) {
															utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) main.this.fastseekspeed);
															continue;
														}
														utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), main.this.mapseekspeed.get(map).floatValue());
													}
													else {
														Location TravelTo = NavNodeCode(zombregion, went, closest);			
														zombiewasat.put(went.getEntityId(), 0);
														if (main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId())) && ((String) main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)) {
															utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) main.this.fastseekspeed);
															continue;
														}
														NavSetZombieTarget(went, TravelTo);
														utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), main.this.mapseekspeed.get(map).floatValue());
													}
												}
												else {//If ply region is null, try chasing player anyway
													Location TravelTo = utilities.getSegment(went, closest);		
													zombiewasat.put(went.getEntityId(), 0);
													if (main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId())) && ((String) main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)) {
														utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) main.this.fastseekspeed);
														continue;
													}
													NavSetZombieTarget(went, TravelTo);
													utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), main.this.mapseekspeed.get(map).floatValue());
												}
											}
											else {//If zombie region is null, try chasing player anyway
												Location TravelTo = utilities.getSegment(went, closest);		
												zombiewasat.put(went.getEntityId(), 0);
												if (main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId())) && ((String) main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)) {
													utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) main.this.fastseekspeed);
													continue;
												}
												utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), main.this.mapseekspeed.get(map).floatValue());
												NavSetZombieTarget(went, TravelTo);
											}
										}
									}
									if (went.getType() == EntityType.WOLF) {
										Location TravelTo = utilities.getSegment(went, closest);
										utilities.livingEntityMoveTo(went, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float) (main.this.fastseekspeed + 0.1));
									}
								}
							}
						}
					}
				}
			}
		}, 5L, 5L);
	}

	public void updateTarget(String map, Wolf w) {
		if (targetSelector != null && w instanceof CraftWolf) {
			CraftWolf cw = (CraftWolf) w;
			EntityWolf ew = cw.getHandle();
			PathfinderGoalSelector s = null;
			try {
				s.a(4, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) ew, EntityHuman.class, true));
				// s = (PathfinderGoalSelector)targetSelector.get(ew);
			} catch (Exception exception) {
			}

			if (s != null) {
				s.a(4, (PathfinderGoal) new PathfinderGoalNearestAttackableTarget((EntityCreature) ew,
						EntityHuman.class, true));
			}
		}
		w.setAngry(true);
	}

	public void PayPlayers(int zombid, double maxhealth) {
		try {
			for (String player : (this.zombiescore.get(Integer.valueOf(zombid))).keySet()) {
				Player p = Bukkit.getPlayer(player);
				if (p != null) {
					int newscore = 0;
					int gained = 0;
					gained = Math.round((this.zombiescore.get(Integer.valueOf(zombid))).get(player).floatValue());
					gained = luaMath.random(gained-3, gained);//Random number between max of 15 and min of 12
					if(gained <= 0) {
						gained = luaMath.random(1, 3);//Random number between 1 and 3
					}
					newscore = gained + (int) stats.getSesPoints(player);
					stats.addPoints(player, gained);
					if (p != null) {
						String score = String.format("%.1f", new Object[] { Double.valueOf(stats.getSesPoints(player)) });
						p.sendMessage(ChatColor.GREEN + "You now have " + ChatColor.DARK_RED + score + ChatColor.GREEN + " dollars! GAINED: " + ChatColor.DARK_RED + Integer.toString(gained));
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public void saveCustomConfig() {
		if (this.customConfig == null || this.customConfigFile == null) {
			return;
		}
		try {
			getCustomConfig().save(this.customConfigFile);
		} catch (IOException ex) {
			getLogger().warning("Could not save config to " + this.customConfigFile);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (this.customConfig == null) {
			reloadCustomConfig();
		}
		return this.customConfig;
	}

	public void reloadCustomConfig() {
		if (this.customConfigFile == null) {
			this.customConfigFile = new File(getDataFolder(), "games.yml");
		}
		this.customConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.customConfigFile);

		File defConfigStream = new File(getDataFolder(), "games.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			this.customConfig.setDefaults((Configuration) defConfig);
		}
	}
	
	public boolean runMysteryBox(String[] lines, Chest chest, Player p) {
		int cost = Integer.parseInt(lines[3].substring(1));
		String active = lines[2];
		
		if(active.equals("§4§lINACTIVE")) {
			return false;
		}
		List<HumanEntity> viewers = chest.getInventory().getViewers();
		if (viewers.size() > 0) {
			p.sendMessage("Try again later. Only one person at a time!");
			return false;
		}
		if (stats.getSesPoints(p.getName()) >= cost) {
			stats.removePoints(p.getName(), cost);
			updateScoreboardInGame(p, pmethods.playerGame(p), 4);
			chest.getSnapshotInventory().clear();
			chest.update();
			int ritem = this.random.nextInt(this.boxitems.size() - 1);
			ItemStack item = this.boxitems.get(ritem);
			int slot = this.random.nextInt(27);
			chest.getSnapshotInventory().setItem(slot, item);
			instance.getLogger().info("Set: " + slot + "," + item.toString());
			chest.update();
			p.sendMessage(ChatColor.GREEN + "You have purchased this " + ChatColor.DARK_RED
					+ "Mysterybox " + ChatColor.GREEN + "for " + Integer.toString(cost)
					+ " dollars!");
			return true;
		}
		p.sendMessage("Not enough money to purchase!");
		return false;
	}

	public boolean mysteryBox(Location loc, Chest chest, Player p) {
		World world = loc.getWorld();
		for (int x = loc.getBlockX() - 2; x <= loc.getBlockX() + 2; x++) {
			for (int y = loc.getBlockY() - 2; y <= loc.getBlockY() + 2; y++) {
				for (int z = loc.getBlockZ() - 2; z <= loc.getBlockZ() + 2; z++) {
					Location temp = new Location(world, x, y, z);
					Block sign = temp.getBlock();
					if (sign.getState() instanceof Sign) {
						Sign actual = (Sign) sign.getState();
						String[] lines = actual.getLines();
						if (lines[1].equalsIgnoreCase("§lMystery Box")) {
							if(this.power.get(pmethods.playerGame(p))) {
								int movebox = luaMath.random(1, 9);
								if(movebox != 1) {
									try {
										return runMysteryBox(lines, chest, p);
									}
									catch (Exception e) {
										return true;
									}
								}
								else {
									try {
										if(lines[2].equals("§4§lINACTIVE")) {
											return false;
										}
										int cost = Integer.parseInt(lines[3].substring(1));
										if (stats.getSesPoints(p.getName()) < cost) {
											p.sendMessage("Not enough money to purchase!");
											return false;
										}
										//Clear current box
										String map = pmethods.playerGame(p);
										if(this.MBoxReset.get(map) == null) {
											this.MBoxReset.put(map, sign.getLocation());
										}
										world.playSound(actual.getLocation(), Sound.ITEM_TOTEM_USE, 0.6F, 1);
										world.playSound(actual.getLocation(), Sound.ENTITY_GHAST_SCREAM, 0.6F, 1);
										p.sendMessage(ChatColor.ITALIC + "" + ChatColor.RED + "Mystery Box has been moved!");
										actual.setLine(2, "§4§lINACTIVE");
										actual.update();
										//Choose a new box
										int fuckthisjavabug = MBoxSigns.size();
										for(Map.Entry<Location, String> entries : this.MBoxSigns.entrySet()) {
											if(entries.getValue().equals(map)) {
												int rand = luaMath.random(0, 1);
												if(rand == 1 || fuckthisjavabug == 0) {
													Location blocklocation = entries.getKey();
													Block possiblesign = world.getBlockAt(blocklocation);
													if (possiblesign.getState() instanceof Sign && !blocklocation.equals(actual.getLocation())) {
														Sign actualsign = (Sign) possiblesign.getState();
														world.strikeLightning(possiblesign.getLocation());
														System.out.println("Moved box in map " + map + " to " + entries.getValue());
														actualsign.setLine(2, "§2§lACTIVE");
														actualsign.update();
														return false;
													}
												}
											}
											fuckthisjavabug--;
										}
										if(this.MBoxSigns.equals(null)) {
											return false;
										}
									}
									catch (Exception e) {
										return false;
									}
								}
							}
							else {
								p.sendMessage("Power needs to be activated first!");
								return false;
							}
						}
						else {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public void checkMobs() {//Check if zombies are killed in map & who knows what else
		getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				if (!main.this.zombies.isEmpty()) {
					for (String map : main.Maps.keySet()) {
						if (games.getState(map) == 2) {
							World world = Bukkit.getWorld(main.Maps.get(map));
							List<Entity> templis = main.getLivingEnts(world);
							killUnloadedZombies(map, world);
							List<Integer> tempID = new ArrayList<>();
							for (Entity id : templis) {
								tempID.add(id.getEntityId());
							}
							List<Integer> IDs = new ArrayList<>();
							for (Integer i : main.this.zombies.keySet()) {
								if (main.this.zombies.get(i).equalsIgnoreCase(map)) {
									IDs.add(i);
								}
							}
							for (Integer i2 : IDs) {
								if (!tempID.contains(i2)) {
									games.setZslayed(map, games.getZslayed(map) + 1);
									main.this.zombies.remove(i2);
								}
							}
							if (games.getZslayed(map) >= ((Integer) main.this.wavemax.get(map)).intValue()) {
								main.this.NewWave(map);
							}
						}

					}
				}
			}
		}, 200L, 200L);
	}

	public void AsynchTasks() {
		getServer().getScheduler().scheduleAsyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				for (Iterator<String> it = main.this.justleftgame.keySet().iterator(); it.hasNext();) {
					String string = it.next();
					int orig = ((Integer) main.this.justleftgame.get(string)).intValue();
					if (orig < main.this.leavetimer) {
						orig++;
						main.this.justleftgame.put(string, Integer.valueOf(orig));
						continue;
					}
					it.remove();

				}

			}
		}, 20L, 20L);
	}

	public void synchTasks() {
		getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				main.this.SignUpdater();
			}
		}, 40L, 40L);
	}

	public void dealDamage() {
		getServer().getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				Player[] list = Bukkit.getOnlinePlayers().toArray(new Player[Bukkit.getOnlinePlayers().size()]);
				byte b;
				int i;
				Player[] arrayOfPlayer1;
				for (i = (arrayOfPlayer1 = list).length, b = 0; b < i;) {
					Player p = arrayOfPlayer1[b];
					if ((p.getLocation().getBlock().getType() == Material.WATER
							|| p.getLocation().getBlock().getType() == Material.COBWEB) && pmethods.inGame(p)
							&& main.this.infectmat) {
						p.damage(2);
					}

					b++;
				}

			}
		}, 20L, 20L);
	}

	public void zsDebug(int i, Player p) {
		if (i == 0) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "-----------Diagnostics Started-----------");
			for (String map : Maps.keySet()) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " state: " + Integer.toString(games.getState(map)));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " onlinepcount: " + Integer.toString(pmethods.onlinepcount(map)));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " pcount: " + Integer.toString(games.getPcount(map)));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " numberinmap: " + Integer.toString(pmethods.numberInMap(map)));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " wave: " + Integer.toString(games.getWave(map)));
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: "
						+ map + " zcount: " + Integer.toString(games.getZcount(map)));
			}
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "Number of Signs: " + Integer.toString(this.Signs.size()));
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "Number of Maps: " + Integer.toString(Maps.size()));
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "Number of Zombies: " + Integer.toString(this.zombies.size()));
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "------------Diagnostics Ended------------");
		}
		if (i == 1 && p != null) {
			p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "-----------Diagnostics Started-----------");
			for (String map : Maps.keySet()) {
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " state: "
						+ Integer.toString(games.getState(map)));
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map
						+ " onlinepcount: " + Integer.toString(pmethods.onlinepcount(map)));
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " pcount: "
						+ Integer.toString(games.getPcount(map)));
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map
						+ " numberinmap: " + Integer.toString(pmethods.numberInMap(map)));
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " wave: "
						+ Integer.toString(games.getWave(map)));
				p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " zcount: "
						+ Integer.toString(games.getZcount(map)));
			}
			p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Signs: "
					+ Integer.toString(this.Signs.size()));
			p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Maps: "
					+ Integer.toString(Maps.size()));
			p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Zombies: "
					+ Integer.toString(this.zombies.size()));
			p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE
					+ "------------Diagnostics Ended------------");
		}
	}

	public static synchronized List getEnts(World world) {
		return world.getEntities();
	}

	public static synchronized List getLivingEnts(World world) {
		return world.getLivingEntities();
	}

	public void resetDoors(String m) {
		try {
			this.door.resetDoors(m, this.spawn.getAllSpawns());
		} catch (Exception e) {
			getLogger().info(String.valueOf(m) + " Did not reset doors correctly!");
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String p = e.getPlayer().getName();
		String m = e.getMessage();
		Player pp = e.getPlayer();
		if (this.easycreate.containsKey(p)) {
			int step = ((Integer) this.easycreate.get(p)).intValue();
			switch (step) {
			case 0:
				this.ecname.put(p, m);
				pp.sendMessage(ChatColor.GREEN + "Please type the max players for " + (String) this.ecname.get(p));
				this.easycreate.put(p, Integer.valueOf(1));
				e.setCancelled(true);
				break;
			case 1:
				try {
					this.ecpcount.put(p, Integer.valueOf(Integer.parseInt(m)));
					this.eczcount.put(p, Double.valueOf(utilities.calcMaxZ(Integer.parseInt(m))));
					pp.sendMessage(ChatColor.GREEN + "Please type the max waves for " + (String) this.ecname.get(p));
					this.easycreate.put(p, Integer.valueOf(2));
				} catch (Exception ec) {
					pp.sendMessage(ChatColor.RED + "Try again, could not parse number!");
				}
				e.setCancelled(true);
				break;
			case 2:
				try {
					this.ecwcount.put(p, Integer.valueOf(Integer.parseInt(m)));
					this.easycreate.put(p, Integer.valueOf(3));
					pp.sendMessage(ChatColor.GOLD + "Creating game for " + (String) this.ecname.get(p) + "!"
							+ ChatColor.DARK_RED + " Is this correct??" + ChatColor.GOLD + " players: "
							+ Integer.toString(((Integer) this.ecpcount.get(p)).intValue()) + " waves: "
							+ Integer.toString(((Integer) this.ecwcount.get(p)).intValue()));
					pp.sendMessage(ChatColor.GREEN + "Please type y or n (yes or no)");
				} catch (Exception ex) {
					pp.sendMessage(ChatColor.RED + "Try again, could not parse number!");
				}
				e.setCancelled(true);
				break;
			case 3:
				if (m.contains("y")) {
					pp.performCommand("zs-create " + (String) this.ecname.get(p) + " "
							+ Double.toString(((Double) this.eczcount.get(p)).doubleValue()) + " "
							+ Integer.toString(((Integer) this.ecpcount.get(p)).intValue()) + " "
							+ Integer.toString(((Integer) this.ecwcount.get(p)).intValue()));
					this.easycreate.put(p, Integer.valueOf(4));
				} else {
					pp.sendMessage(ChatColor.GREEN + "Cancelling!");
					this.easycreate.remove(p);
					this.ecpcount.remove(p);
					this.eczcount.remove(p);
					this.ecwcount.remove(p);
					this.ecname.remove(p);
				}
				e.setCancelled(true);
				break;
			}
		}
	}

	public void smartDoors(Block b, String m, int w) {
		if (b.getData() >= 8) {
			Block a = b.getRelative(BlockFace.DOWN);
			this.door.addDoor(b, m, w);
		} else {
			Block a = b.getRelative(BlockFace.UP);
			this.door.addDoor(b, m, w);
		}
	}
}