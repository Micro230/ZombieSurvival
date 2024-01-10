/*     */ package com.bjornke.zombiesurvival;
/*     */ import java.util.HashMap;
import java.util.Map;

/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.HumanEntity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.entity.EntityCombustEvent;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
/*     */ import org.bukkit.event.entity.EntityShootBowEvent;
/*     */ import org.bukkit.event.entity.EntityTargetEvent;
/*     */ import org.bukkit.event.entity.FoodLevelChangeEvent;
/*     */ import org.bukkit.event.inventory.InventoryClickEvent;
/*     */ import org.bukkit.event.inventory.InventoryOpenEvent;
/*     */ import org.bukkit.event.player.PlayerDropItemEvent;
/*     */ import org.bukkit.event.player.PlayerEggThrowEvent;
/*     */ import org.bukkit.event.player.PlayerExpChangeEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEntityEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.event.player.PlayerPortalEvent;
/*     */ 
/*     */ public class spectate implements Listener {
/*  28 */   static Map<String, String> spectators = new HashMap<>();
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerClick(PlayerInteractEvent e) {
/*  32 */     Player p = e.getPlayer();
/*  33 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/*  34 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerDrop(PlayerDropItemEvent e) {
/*  40 */     Player p = e.getPlayer();
/*  41 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/*  42 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerClickEntity(PlayerInteractEntityEvent e) {
/*  48 */     Player p = e.getPlayer();
/*  49 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/*  50 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerEggThrow(PlayerEggThrowEvent e) {
/*  56 */     Player p = e.getPlayer();
/*  57 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/*  58 */       e.setHatching(false);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerPickup(EntityPickupItemEvent e) {
/*  64 */     Entity p = e.getEntity();
/*  65 */     if (spectators.containsKey(p.getName()) && p instanceof Player && games.getState((String)spectators.get(p.getName())) > 1) {
/*  66 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerPortal(PlayerPortalEvent e) {
/*  72 */     Player p = e.getPlayer();
/*  73 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1)
/*  74 */       e.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerInventory(InventoryOpenEvent e) {
/*     */     Player p;
/*  80 */     HumanEntity he = e.getPlayer();
/*     */     
/*  82 */     if (he instanceof Player) {
/*  83 */       p = (Player)he;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  87 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1)
/*  88 */       e.setCancelled(true); 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerInventory(InventoryClickEvent e) {
/*     */     Player p;
/*  94 */     HumanEntity he = e.getWhoClicked();
/*     */     
/*  96 */     if (he instanceof Player) {
/*  97 */       p = (Player)he;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 101 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 102 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerExp(PlayerExpChangeEvent e) {
/* 108 */     Player p = e.getPlayer();
/* 109 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 110 */       e.setAmount(0);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerBreakBlock(BlockBreakEvent e) {
/* 116 */     Player p = e.getPlayer();
/* 117 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 118 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerPlaceBlock(BlockPlaceEvent e) {
/* 124 */     Player p = e.getPlayer();
/* 125 */     if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 126 */       e.setCancelled(true);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerAttack(EntityDamageByEntityEvent e) {
/* 132 */     Entity damager = e.getDamager();
/* 133 */     if (damager instanceof Player) {
/* 134 */       Player p = (Player)damager;
/* 135 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 136 */         e.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerDamage(EntityDamageEvent e) {
/* 143 */     Entity damaged = e.getEntity();
/* 144 */     if (damaged instanceof Player) {
/* 145 */       Player p = (Player)damaged;
/* 146 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 147 */         e.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerFood(FoodLevelChangeEvent e) {
/* 154 */     HumanEntity humanEntity = e.getEntity();
/* 155 */     if (humanEntity instanceof Player) {
/* 156 */       Player p = (Player)humanEntity;
/* 157 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 158 */         e.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerCombust(EntityCombustEvent e) {
/* 165 */     Entity damaged = e.getEntity();
/* 166 */     if (damaged instanceof Player) {
/* 167 */       Player p = (Player)damaged;
/* 168 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 169 */         e.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerShoot(EntityShootBowEvent e) {
/* 176 */     LivingEntity livingEntity = e.getEntity();
/* 177 */     if (livingEntity instanceof Player) {
/* 178 */       Player p = (Player)livingEntity;
/* 179 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1) {
/* 180 */         e.setCancelled(true);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.LOWEST)
/*     */   public void onPlayerTargetted(EntityTargetEvent e) {
/* 187 */     Entity damaged = e.getTarget();
/* 188 */     if (damaged instanceof Player) {
/* 189 */       Player p = (Player)damaged;
/* 190 */       if (spectators.containsKey(p.getName()) && games.getState((String)spectators.get(p.getName())) > 1)
/* 191 */         e.setCancelled(true); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Micro230\Desktop\Micro230 Server Reboot\ZombieSurvival_v3.8.3.jar!\com\bjornke\zombiesurvival\spectate.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */