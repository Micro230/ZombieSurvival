package com.bjornke.zombiesurvival;

import org.bukkit.Location;

import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.goal.PathfinderGoal;

public class CustomPathfinder extends PathfinderGoal {
	private double speed;

	private EntityInsentient entity;

	private Location loc;

	private Navigation navigation;

	public CustomPathfinder(EntityInsentient entity, Location loc, double speed) {
		this.entity = entity;
		this.loc = loc;
		this.navigation = (Navigation) this.entity.D();
		this.speed = speed;
	}

	public boolean a() {
		return true;
	}
}