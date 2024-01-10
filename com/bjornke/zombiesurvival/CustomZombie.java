package com.bjornke.zombiesurvival;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.LivingEntity;

import org.bukkit.craftbukkit.v1_18_R1.entity.CraftZombie;
import net.minecraft.world.level.World;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.EntityLiving;

public class CustomZombie extends EntityZombie {

	public CustomZombie(World world) {
		super(world);
		
		Zombie cz = (Zombie) this.getBukkitEntity();
		cz.setAdult();
		cz.setCustomNameVisible(true);
		
		this.bR.a(0, new PathfinderGoalNearestAttackableTarget(this, Player.class, true));
	}
	
	public void setNewSpeedAndHealth(float speed, float health) {
		AttributeInstance healthAttribute = ((LivingEntity) this).getAttribute(Attribute.GENERIC_MAX_HEALTH);
		AttributeInstance speedAttribute = ((LivingEntity) this).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
		healthAttribute.setBaseValue(3 + health);
		speedAttribute.setBaseValue(10 + speed);
	}
}