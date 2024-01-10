package com.bjornke.zombiesurvival;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class signs implements Listener {
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && player.hasPermission("zs.signs")) {
			Block block = e.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				String[] lines = sign.getLines();
				if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§lMy Stats")) {
					player.sendMessage(ChatColor.GREEN + "Cash: " + ChatColor.DARK_RED + Double.toString(stats.getTotalPoints(player.getName())));
					player.sendMessage(ChatColor.GREEN + "Total Kills: " + ChatColor.DARK_RED + Double.toString(stats.getTotalKills(player.getName())));
					player.sendMessage(ChatColor.GREEN + "Total Deaths: " + ChatColor.DARK_RED + Double.toString(stats.getTotalDeaths(player.getName())));
				}
				else if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§2join class")) {
					if (!pmethods.inGame(player)) {
						if(classes.getClassUnlockedByRank(lines[2]) != -1 && classes.getClassUnlockedByRank(lines[2]) != 0) {
							if(AutoRank.getRankLevelByPlayerName(player.getName()) >= classes.getClassUnlockedByRank(lines[2])) {
								classes.setUserClass(player, lines[2]);
							}
							else {
								player.sendMessage("§4Class is not unlocked yet!");
							}
						}
						else if (classes.getClassUnlockedByRank(lines[2]) != -1 && classes.getClassUnlockedByRank(lines[2]) == 0) {
							if((stats.isClassUnlocked(player.getName(), lines[2]) || classes.isUnlockedByDefault(lines[2]))) {
								classes.setUserClass(player, lines[2]);
							}
							else {
								player.sendMessage("§4Class is not unlocked yet!");
							}
						}
						else {
							player.sendMessage("ARL:" + AutoRank.getRankLevelByPlayerName(player.getName()));
							player.sendMessage("CUBR:" + classes.getClassUnlockedByRank(lines[2]));
							player.sendMessage("§4Something is wrong with config. Tell Administrator.");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You cannot change classes while in-game.");
					}					
				}
				else if (lines[0].equalsIgnoreCase("§4[zombie survival]") && lines[1].equalsIgnoreCase("§1leave class")) {
					classes.removeUser(player);
					player.sendMessage(ChatColor.GRAY + "You have left your class");
				}
			}
		}
		else if (e.getAction() == Action.LEFT_CLICK_BLOCK && player.hasPermission("zs.signs")) {
			Block block = e.getClickedBlock();
			if (block.getState() instanceof Sign) {
				Sign sign = (Sign) block.getState();
				String[] lines = sign.getLines();
				if (lines[0].equals("§dZombie Stats")) {
					player.sendMessage(ChatColor.GRAY + "Right click this sign to join a game!");
					if (games.exists(lines[1])) {
						player.sendMessage(ChatColor.GREEN + "Players: " + ChatColor.DARK_RED
								+ Integer.toString(pmethods.numberInMap(lines[1])) + ChatColor.GRAY + "/"
								+ ChatColor.DARK_GREEN + Integer.toString(games.getMaxPlayers(lines[1])));
						player.sendMessage(ChatColor.GREEN + "Players Alive: " + ChatColor.DARK_RED
								+ Integer.toString(games.getPcount(lines[1])) + ChatColor.GRAY + "/"
								+ ChatColor.DARK_GREEN + Integer.toString(pmethods.numberInMap(lines[1])));
						player.sendMessage(ChatColor.GREEN + "Wave: " + ChatColor.DARK_RED
								+ Integer.toString(games.getWave(lines[1])));
						player.sendMessage(ChatColor.GREEN + "Zombies: " + ChatColor.DARK_RED
								+ Integer.toString(games.getZcount(lines[1])));
						player.sendMessage(ChatColor.GREEN + "Zombies Remaining: " + ChatColor.DARK_RED
								+ Integer.toString(games.getZcount(lines[1]) - games.getZslayed(lines[1])));
						player.sendMessage(ChatColor.GREEN + "Wave Max Zombies: " + ChatColor.DARK_RED
								+ Integer.toString(games.getMaxZombies(lines[1])));
					}
				}
			}
		}
	}
	
	public boolean isZombieSurvivalSign(String s) {
		if((s.equalsIgnoreCase("zombiesurvival") || s.equalsIgnoreCase("zs") || s.equalsIgnoreCase("zombie"))) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		String[] lines = e.getLines();
		if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("mystery") || lines[1].equalsIgnoreCase("box") || lines[1].equalsIgnoreCase("mysterybox") || lines[1].equalsIgnoreCase("mystery box")) && !lines[2].isEmpty() && !lines[3].isEmpty()) {
			Player player = e.getPlayer();
			e.setLine(0, "§4[Zombie Survival]");
			e.setLine(1, "§lMystery Box");
			if(lines[2].equalsIgnoreCase("active")) {
				e.setLine(2, "§2§lACTIVE");
			}
			else if(lines[2].equalsIgnoreCase("inactive")) {
				e.setLine(2, "§4§lINACTIVE");
			}
			if (!lines[3].isEmpty() && !lines[3].contains("$")) {
				e.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				e.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].equalsIgnoreCase("mystery") || lines[1].equalsIgnoreCase("box") || lines[1].equalsIgnoreCase("mysterybox") || lines[1].equalsIgnoreCase("mystery box"))) {
			e.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && ((lines[1].equalsIgnoreCase("upgrade class") || lines[1].equalsIgnoreCase("upgrade") || lines[1].equalsIgnoreCase("class upgrade"))) || (lines[2].equalsIgnoreCase("upgrade class") || lines[2].equalsIgnoreCase("upgrade") || lines[2].equalsIgnoreCase("class upgrade"))) {
			Player player = e.getPlayer();
			e.setLine(0, "§4[Zombie Survival]");
			e.setLine(1, "§k------------");
			e.setLine(2, "§2Upgrade Class");
			e.setLine(3, "§k------------");
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				e.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && (lines[1].isEmpty())) {
			e.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && ((lines[1].equalsIgnoreCase("join class") || lines[1].equalsIgnoreCase("join") || lines[1].equalsIgnoreCase("class join"))) && !lines[2].isEmpty()) {
			Player player = e.getPlayer();
			e.setLine(0, "§4[Zombie Survival]");
			e.setLine(1, "§2Join Class");
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				e.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].isEmpty()) {
			e.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && ((lines[1].equalsIgnoreCase("leave class") || lines[1].equalsIgnoreCase("leave") || lines[1].equalsIgnoreCase("class leave"))) && !lines[2].isEmpty()) {
			Player player = e.getPlayer();
			e.setLine(0, "§4[Zombie Survival]");
			e.setLine(1, "§1Leave Class");
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				e.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].isEmpty()) {
			e.setLine(0, "§4BAD FORMAT");
		}
		
		if (isZombieSurvivalSign(lines[0]) && ((lines[1].equalsIgnoreCase("buy class") || lines[1].equalsIgnoreCase("buy") || lines[1].equalsIgnoreCase("class buy"))) && !lines[2].isEmpty() && !lines[3].isEmpty()) {
			Player player = e.getPlayer();
			e.setLine(0, "§4[Zombie Survival]");
			e.setLine(1, "§1Buy Class");
			if (!lines[3].contains("$")) {
				e.setLine(3, "$" + lines[3]);
			}
			if (player.hasPermission("zs.edit")) {
				player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
			} else {
				e.setLine(0, "§4MUST BE OP");
			}
		} else if (isZombieSurvivalSign(lines[0]) && lines[1].isEmpty()) {
			e.setLine(0, "§4BAD FORMAT");
		}
	}
}
