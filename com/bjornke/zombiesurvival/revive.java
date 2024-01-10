/*     */ package com.bjornke.zombiesurvival;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.block.BlockFace;
/*     */ import org.bukkit.block.Sign;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;

/*     */ 
/*     */ public class revive
/*     */   implements Listener
/*     */ {
/*     */   Plugin plugin;
/*  33 */   public Map<Sign, String> timedSigns = new ConcurrentHashMap<>();
/*     */   
/*     */   public void counterTask() {
/*  36 */     this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
/*     */           public void run() {
/*  38 */             for (Iterator<Sign> it = revive.this.timedSigns.keySet().iterator(); it.hasNext(); ) {
/*  39 */               Sign s = it.next();
/*  40 */               String line4 = s.getLine(3);
/*  41 */               String chars = line4.substring(2);
/*  42 */               int time = Integer.parseInt(chars);
/*  43 */               time--;
/*  44 */               if (time > 0) {
/*  45 */                 if (time > 15) {
/*  46 */                   s.setLine(3, "§a" + Integer.toString(time));
/*  47 */                   s.update(); continue;
/*  48 */                 }  if (time > 5) {
/*  49 */                   s.setLine(3, "§e" + Integer.toString(time));
/*  50 */                   s.update(); continue;
/*     */                 } 
/*  52 */                 s.setLine(3, "§c" + Integer.toString(time));
/*  53 */                 s.update();
/*     */                 continue;
/*     */               } 
/*  56 */               Block b = s.getBlock();
/*  57 */               b.setType(Material.AIR);
/*  58 */               it.remove();
/*     */             
/*     */             }
/*     */           
/*     */           }
/*  63 */         }, 20L, 20L);
/*     */   }
/*     */   
/*     */   public void removeSign(String name) {
/*  67 */     for (Iterator<Sign> it = this.timedSigns.keySet().iterator(); it.hasNext(); ) {
/*  68 */       Sign s = it.next();
/*  69 */       if (((String)this.timedSigns.get(s)).matches(name)) {
/*  70 */         Block b = s.getBlock();
/*  71 */         b.setType(Material.AIR);
/*  72 */         it.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createRevive(Player p) {
/*  78 */     Block b = p.getLocation().getBlock();
/*  79 */     Location middle = p.getLocation();
/*  80 */     if (b.getType() != Material.AIR) {
/*  81 */       for (int y = middle.getBlockY() - 2; y <= middle.getBlockY() + 2; y++) {
/*  82 */         for (int x = middle.getBlockX() - 6; x <= middle.getBlockX() + 6; x++) {
/*  83 */           for (int z = middle.getBlockZ() - 6; z <= middle.getBlockZ() + 6; z++) {
/*  84 */             Location temp = new Location(middle.getWorld(), x, middle.getBlockY(), z);
/*  85 */             if (temp.getBlock().getType() == Material.AIR && temp.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
/*  86 */               b = temp.getBlock();
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*  93 */     if (b.getType() == Material.AIR) {
/*  94 */       b.getWorld().strikeLightningEffect(b.getLocation());
/*  95 */       b.setType(Material.OAK_SIGN);
/*  96 */       Sign s = (Sign)b.getState();
/*  97 */       s.setLine(0, "BREAK TO REVIVE");
/*  98 */       s.setLine(1, "§1" + p.getName());
/*  99 */       s.setLine(2, "§4TIME LEFT");
/* 100 */       s.setLine(3, "§a30");
/* 101 */       s.update();
/* 102 */       this.timedSigns.put(s, p.getName());
/*     */     } else {
/* 104 */       this.plugin.getLogger().warning("Could not create revive sign for: " + p.getName());
/*     */     } 
/*     */   }

/*     */   public void onSignBreak(BlockBreakEvent e, ItemStack[] inventory, ItemStack[] armor) {
/* 110 */     Block b = e.getBlock();
/* 111 */     Player player = e.getPlayer();
/* 113 */       Sign s = (Sign)b.getState();
/* 114 */       if (this.timedSigns.containsKey(s)) {
/* 115 */         Player p = Bukkit.getPlayer(this.timedSigns.get(s));
/* 116 */         if (p != null && pmethods.inGame(p) && !p.isDead()) {
					if(inventory != null && armor != null) {
						p.getInventory().setContents(inventory);
						p.getInventory().setArmorContents(armor);
					}
/* 117 */           b.setType(Material.AIR);
/* 118 */           games.setPcount(pmethods.playerGame(p), games.getPcount(pmethods.playerGame(p)) + 1);
/* 119 */           main.dead.remove(p.getName());
/* 120 */           spectate.spectators.remove(p.getName());
/* 121 */           p.teleport(s.getLocation());
/* 122 */           p.setAllowFlight(false);
/* 123 */           p.setFlying(false);
/* 124 */           p.setGameMode(GameMode.SURVIVAL);
/* 125 */           p.setHealth(20);
/* 126 */           p.setFoodLevel(20);
/* 127 */           utilities.unhidePlayer(p);
/* 128 */           this.timedSigns.remove(s);
/* 129 */           player.sendMessage(ChatColor.GREEN + "You revived: " + ChatColor.GRAY + p.getName());
/* 130 */           p.sendMessage(ChatColor.GREEN + "You were revived by: " + ChatColor.GRAY + player.getName());
/* 131 */           e.setCancelled(true);
/* 132 */         } else if (p != null || !pmethods.inGame(p)) {
/* 133 */           b.setType(Material.AIR);
/* 134 */           this.timedSigns.remove(s);
/* 135 */           e.setCancelled(true);
/*     */         } 
/*     */       } 
/*     */   }
/*     */   
/*     */   public void Destroy() {
/*     */     try {
/* 143 */       finalize();
/* 144 */     } catch (Throwable e) {
/* 145 */       this.plugin.getLogger().warning("Failed to destroy class");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Micro230\Desktop\Micro230 Server Reboot\ZombieSurvival_v3.8.3.jar!\com\bjornke\zombiesurvival\revive.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */