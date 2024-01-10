package com.bjornke.zombiesurvival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.Damageable;

public class CustomPlayerData {
	
	private final UUID player;
	private boolean canfire;
	private int spareammo;
	private int clip0 = 0;
	private int clip1 = 0;
	private int clip2 = 0;
	private int clip3 = 0;
	private int clip4 = 0;
	private int clip5 = 0;
	private int clip0max = 8;
	private int clip1max = 12;
	private int clip2max = 32;
	private int clip3max = 4;
	private int clip4max = 64;
	private int clip5max = 16;
	
	private ItemStack[] deadinventory;
	private ItemStack[] deadarmor;
	
	public int eventrun1;
	public int eventrun2;
	public int eventrun3;
	public int eventrun4;
	public int eventrun5;
	public int eventrun6;
	public int eventrun7;
	public int eventrun8;
	public int eventrun9;
	public int eventrun10;
	public int eventrun11;
	public int eventrun12;
	public int eventrun13;
	public int eventrun14;
	public int eventrun15;
	public int eventrun16;
	public int eventrun17;
	public int eventrun18;
	public int eventrun19;
	public int eventrun20;
	
	public int shootperkmultiplier = 1;
	public int healthperkmultiplier = 0;
	public int reviveperkmultiplier = 1;
	public int reloadperkmultiplier = 1;
	
	public boolean perk0active = false;
	public boolean perk1active = false;
	public boolean perk2active = false;
	public boolean perk3active = false;
	
	public boolean papwoodactive = false;
	public boolean papstoneactive = false;
	public boolean papironactive = false;
	public boolean papgoldactive = false;
	public boolean papdiamondactive = false;
	public boolean papnetheractive = false;
	
	public List<String> autorepairitems = new ArrayList<>();
	public List<String> autoregenitems = new ArrayList<>();
	
	public MicroCraft Microcraft = new MicroCraft();
	
	public CustomPlayerData(UUID player) {
		this.player = player;
		this.canfire = true;
	}
	
	public void setDeathInventory(ItemStack[] items) {
		deadinventory = items;
	}
	
	public void setDeathArmor(ItemStack[] items) {
		deadarmor = items;
	}
	
	public ItemStack[] getDeathInventory() {
		return deadinventory;
	}
	
	public ItemStack[] getDeathArmor() {
		return deadarmor;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(this.player);
	}
	
	public UUID getUUID() {
		return this.player;
	}
	
	public String getName() {
		return Bukkit.getPlayer(this.player).getName();
	}
	
	public void canFire(boolean bool) {
		this.canfire = bool;
	}
	
	public boolean canFire() {
		return this.canfire;
	}
	
	public int getAmmoClip(int weapon) {
		switch(weapon) {
			case 0:
				return this.clip0;
			case 1:
				return this.clip1;
			case 2:
				return this.clip2;
			case 3:
				return this.clip3;
			case 4:
				return this.clip4;
			case 5:
				return this.clip5;
			default:
				return 0;
		}
	}
	
	public int getMaxAmmoClip(int clip) {
		switch(clip) {
			case 0:
				return this.clip0max;
			case 1:
				return this.clip1max;
			case 2:
				return this.clip2max;
			case 3:
				return this.clip3max;
			case 4:
				return this.clip4max;
			case 5:
				return this.clip5max;
			default:
				return 0;
		}
	}
	
	public void setAmmoClip(int weapon, int amount) {
		switch(weapon) {
			case 0:
				this.clip0 = amount;
				break;
			case 1:
				this.clip1 = amount;
				break;
			case 2:
				this.clip2 = amount;
				break;
			case 3:
				this.clip3 = amount;
				break;
			case 4:
				this.clip4 = amount;
				break;
			case 5:
				this.clip5 = amount;
				break;
		}
	}
	
	public int getAmmoSpare() {
		PlayerInventory inventory = this.getPlayer().getInventory();
		int i = 0;
		for (ItemStack item : inventory.getContents()) {
		    if (item != null) {
		    	if(item.getType() == Material.ARROW) {
		    		i = i + item.getAmount();
		    	}
		    }
		}
		this.spareammo = i;
		return this.spareammo;
	}
	
	public void setAmmoSpare(int amount) {
		PlayerInventory inventory = this.getPlayer().getInventory();
		int amountstackfix = amount;
		for (ItemStack item : inventory.getContents()) {
			if (item != null) {
			    if (item.getType() == Material.ARROW) {
			    	if(amountstackfix > 64) {
			    		item.setAmount(64);
			    		amountstackfix -= 64;
			    	}
			    	else if(amountstackfix == 64) {
			    		item.setAmount(0);
			    		break;
			    	}
			    	else {
			    		if(item.getAmount() < amountstackfix) {
			    			amountstackfix -= item.getAmount();
				    		inventory.remove(item);
				    	}
			    		else {
			    			item.setAmount(amountstackfix);
				    		break;
			    		}
			    	}
			    }
			}
		}
	}
	
	public void resetpap() {
		this.papwoodactive = false;
		this.papstoneactive = false;
		this.papironactive = false;
		this.papgoldactive = false;
		this.papdiamondactive = false;
		this.papnetheractive = false;
	}
	
	public void activatepap(String item) {
		switch(item) {
		case "WOODEN_HOE":
			this.papwoodactive = true;
			break;
		case "STONE_HOE":
			this.papstoneactive = true;
			break;
		case "IRON_HOE":
			this.papironactive = true;
			break;
		case "GOLDEN_HOE":
			this.papgoldactive = true;
			break;
		case "DIAMOND_HOE":
			this.papdiamondactive = true;
			break;
		case "NETHERITE_HOE":
			this.papnetheractive = true;
			break;
		}
	}
	
	public boolean papIsActivated(String item) {
		switch(item) {
		case "WOODEN_HOE":
			if(this.papwoodactive) {
				return true;
			}
			else {
				return false;
			}
		case "STONE_HOE":
			if(this.papstoneactive) {
				return true;
			}
			else {
				return false;
			}
		case "IRON_HOE":
			if(this.papironactive) {
				return true;
			}
			else {
				return false;
			}
		case "GOLDEN_HOE":
			if(this.papgoldactive) {
				return true;
			}
			else {
				return false;
			}
		case "DIAMOND_HOE":
			if(this.papdiamondactive) {
				return true;
			}
			else {
				return false;
			}
		case "NETHERITE_HOE":
			if(this.papnetheractive) {
				return true;
			}
			else {
				return false;
			}
		default:
			return false;
		}
	}
	
	public void resetMachines() {
		this.perk0active = false;
		this.perk1active = false;
		this.perk2active = false;
		this.perk3active = false;
		this.shootperkmultiplier = 1;
		this.healthperkmultiplier = 0;
		this.reviveperkmultiplier = 1;
		this.reloadperkmultiplier = 1;
	}
	
	public void activateMachine(int machine) {
		switch(machine) {
		case 0:
			this.shootperkmultiplier = 2;
			this.perk0active = true;
			break;
		case 1:
			this.healthperkmultiplier = 10;
			this.perk1active = true;
			break;
		case 2:
			this.reviveperkmultiplier = 2;
			this.perk2active = true;
			break;
		case 3:
			this.reloadperkmultiplier = 2;
			this.perk3active = true;
			break;
		}
	}
	
	public boolean hasMachine(int machine) {
		switch(machine) {
		case 0:
			if(this.perk0active) {
				return true;
			}
			else {
				return false;
			}
		case 1:
			if(this.perk1active) {
				return true;
			}
			else {
				return false;
			}
		case 2:
			if(this.perk2active) {
				return true;
			}
			else {
				return false;
			}
		case 3:
			if(this.perk3active) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public void repairItems() {
		Player p = getPlayer();
		List<Material> alreadyrepaired = new ArrayList<>();
		for(ItemStack playeritem : p.getInventory().getContents()) {
			if(playeritem != null) {
				for(String item : autorepairitems) {
					if(playeritem.getType() == Material.valueOf(item) && !alreadyrepaired.contains(playeritem.getType())) {
						alreadyrepaired.add(playeritem.getType());
						ItemMeta imeta = playeritem.getItemMeta();
				        ((Damageable) imeta).setDamage(0);
				        playeritem.setItemMeta(imeta);
				        p.updateInventory();
				        p.sendMessage("Test Message REPAIRED " + item);
					}
				}
			}
		}
	}
	
	public void restockItems() {
		Player p = getPlayer();
		HashMap<String, Integer> itemsheld = new HashMap<>();
		for(ItemStack playeritem : p.getInventory().getContents()) {
			if(playeritem != null) {
				if (playeritem.getType() == Material.POTION || playeritem.getType() == Material.SPLASH_POTION || playeritem.getType() == Material.LINGERING_POTION || playeritem.getType() == Material.TIPPED_ARROW) {
					String piname = playeritem.getItemMeta().getDisplayName();
					Integer piamount = playeritem.getAmount();
					if(itemsheld.containsKey(piname)) {
						itemsheld.put(piname, itemsheld.get(piname)+piamount);
					}
					else {
						itemsheld.put(piname, piamount);
					}
				}
			}
		}
		List<Material> alreadyaddedpotion = new ArrayList<>();
		for(String item : autoregenitems) {
			if(!alreadyaddedpotion.contains(Material.getMaterial(item))) {
				String[] line = item.split(",");
				Material itemid = Material.getMaterial(line[0]);
				int amount = 1;
				ItemStack itm = new ItemStack(itemid, amount);
				
				if (line.length > 1) {
					amount = Integer.parseInt(line[1]);
					if (line.length > 2) {
						String potiontype = line[2];
						int potionduration = Integer.parseInt(line[3]);
						int potionstrength = Integer.parseInt(line[4]);
						String potionname = line[5];
						int r = Integer.parseInt(line[6]);
						int g = Integer.parseInt(line[7]);
						int b = Integer.parseInt(line[8]);
						for(Entry<String, Integer> invitems : itemsheld.entrySet()) {
							if(invitems.getKey().equals(potionname)) {
								amount = amount - invitems.getValue();
								break;
							}
						}					
						itm = Microcraft.parsePotion(itemid, potiontype, potionduration, potionstrength, potionname, r, g, b, amount);
						alreadyaddedpotion.add(itm.getType());
					}
				}
				p.getInventory().addItem(itm);
			}
		}
	}
	
	public void clearAutoRepair() {
		autorepairitems.clear();
	}
	
	public List<String> getAutoRepair() {
		return autorepairitems;
	}
	
	public void setAutoRepair(String item) {
		autorepairitems.add(item);
	}
	
	public void clearAutoRegenerate() {
		autoregenitems.clear();
	}
	
	public List<String> getAutoRegenerate() {
		return autoregenitems;
	}
	
	public void setAutoRegenerate(String item) {
		autoregenitems.add(item);
	}
}
