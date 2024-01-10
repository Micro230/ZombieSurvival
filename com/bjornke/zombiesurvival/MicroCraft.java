package com.bjornke.zombiesurvival;

import java.util.HashMap;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MicroCraft {
	
	public MicroCraft() {
	}
	
	public Player getPlayerByName(String name) {
		return Bukkit.getPlayer(name);
	}
	
	public Player getPlayerByUUID(UUID uuid) {
		return Bukkit.getPlayer(uuid);
	}
	
	public ItemStack parsePotion(Material itemid, String potiontype, int potionduration, int potionstrength, String potionname, int r, int g, int b, int amount) {		
		ItemStack item = new ItemStack(itemid, amount);
		if (itemid == Material.POTION || itemid == Material.SPLASH_POTION || itemid == Material.LINGERING_POTION || itemid == Material.TIPPED_ARROW) {
			PotionMeta ifpotion = (PotionMeta) item.getItemMeta();
			ifpotion.addCustomEffect(new PotionEffect(PotionEffectType.getByName(potiontype),potionduration * 20, potionstrength), true);
			ifpotion.setDisplayName(potionname);
			ifpotion.setColor(Color.fromRGB(r, g, b));
			item.setItemMeta(ifpotion);
		}
		return item;
	}
	
	public void replaceInventoryItem(Player p, ItemStack replaceItem, ItemStack item) {
		Inventory inv = p.getInventory();
		ItemStack[] contents = inv.getContents();
		HashMap<HashMap<Material, Integer>, Integer> slots = new HashMap<>();
		
		for(int i=0; i == inv.getSize(); i++) {
			HashMap<Material, Integer> temp = new HashMap<Material, Integer>();
			ItemStack content = contents[i];
			if(content == null) {
				temp.put(null, 0);
				slots.put(null, i);
			}
			else {
				temp.put(content.getType(), content.getAmount());
				slots.put(temp, i);
			}
		}
		
		for(ItemStack is : contents) {
			for(Entry<HashMap<Material, Integer>, Integer> immutable : slots.entrySet()) {
				if(immutable.getKey() != null) {
					if(immutable.getKey().containsKey(is.getType()) && immutable.getKey().get(is.getType()) == is.getAmount()){
						if(replaceItem.getType() == is.getType()) {
							int slot = slots.get(immutable.getKey());
							inv.setItem(slot, item);
						}
					}
				}
			}
		}
	}
}