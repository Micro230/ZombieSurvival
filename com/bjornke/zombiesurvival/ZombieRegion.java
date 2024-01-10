package com.bjornke.zombiesurvival;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class ZombieRegion implements Serializable {

    private final int xMin;
    private final int xMax;
    private final int yMin;
    private final int yMax;
    private final int zMin;
    private final int zMax;
    private final double xMinCentered;
    private final double xMaxCentered;
    private final double yMinCentered;
    private final double yMaxCentered;
    private final double zMinCentered;
    private final double zMaxCentered;
    private final String worldname;
    private final String name;
    private final String map;
    protected final List<NavigationNode> nodes;
    private final NavigationNode neutralnode;

    public ZombieRegion(String nameinput, String mapinput, final Location point1, final Location point2, NavigationNode connectedneutral) {
        this.xMin = Math.min(point1.getBlockX(), point2.getBlockX());
        this.xMax = Math.max(point1.getBlockX(), point2.getBlockX());
        this.yMin = Math.min(point1.getBlockY(), point2.getBlockY());
        this.yMax = Math.max(point1.getBlockY(), point2.getBlockY());
        this.zMin = Math.min(point1.getBlockZ(), point2.getBlockZ());
        this.zMax = Math.max(point1.getBlockZ(), point2.getBlockZ());
        this.worldname = point1.getWorld().getName();
        this.xMinCentered = this.xMin + 0.5;
        this.xMaxCentered = this.xMax + 0.5;
        this.yMinCentered = this.yMin + 0.5;
        this.yMaxCentered = this.yMax + 0.5;
        this.zMinCentered = this.zMin + 0.5;
        this.zMaxCentered = this.zMax + 0.5;
        this.name = nameinput;
        this.map = mapinput;
        this.nodes = new ArrayList<NavigationNode>();
        this.neutralnode = connectedneutral;
    }
    
    public void init() {
    	//might not be necessary
    	for(NavigationNode n : this.nodes) {
    		n.init();
    	}
    }

    public Iterator<Block> blockList() {
        final ArrayList<Block> bL = new ArrayList<>(this.getTotalBlockSize());
        for(int x = this.xMin; x <= this.xMax; ++x) {
            for(int y = this.yMin; y <= this.yMax; ++y) {
                for(int z = this.zMin; z <= this.zMax; ++z) {
                    final Block b = Bukkit.getWorld(worldname).getBlockAt(x, y, z);
                    bL.add(b);
                }
            }
        }
        return bL.iterator();
    }
    
    public void addNode(NavigationNode n) {
		this.nodes.add(n);
    }
    
    public NavigationNode getNeutralNode() {
    	return neutralnode;
    }
    
    public NavigationNode getClosestNavNode(Location loc) {
    	NavigationNode testnode = null;
    	if(this.nodes != null) {
	    	for (NavigationNode nn : this.nodes) {
	    		if(testnode == null) {
	    			testnode = nn;
	    		}
	    		if(testnode != null) {
	    			if (nn.location.distanceSquared(loc) < testnode.location.distanceSquared(loc)) {//both locations are null but the objects arent.  Wtf?
	    				testnode = nn;
	    			}
				}
			}
    	}
    	return testnode;
    }
    
    public String getName() {
        return name;
    }

    public Location getCenter() {
        return new Location(Bukkit.getWorld(worldname), (this.xMax - this.xMin) / 2 + this.xMin, (this.yMax - this.yMin) / 2 + this.yMin, (this.zMax - this.zMin) / 2 + this.zMin);
    }

    public double getDistance() {
        return this.getPoint1().distance(this.getPoint2());
    }

    public double getDistanceSquared() {
        return this.getPoint1().distanceSquared(this.getPoint2());
    }

    public int getHeight() {
        return this.yMax - this.yMin + 1;
    }

    public Location getPoint1() {
        return new Location(Bukkit.getWorld(worldname), this.xMin, this.yMin, this.zMin);
    }

    public Location getPoint2() {
        return new Location(Bukkit.getWorld(worldname), this.xMax, this.yMax, this.zMax);
    }

    public Location getRandomLocation() {
        final Random rand = new Random();
        final int x = rand.nextInt(Math.abs(this.xMax - this.xMin) + 1) + this.xMin;
        final int y = rand.nextInt(Math.abs(this.yMax - this.yMin) + 1) + this.yMin;
        final int z = rand.nextInt(Math.abs(this.zMax - this.zMin) + 1) + this.zMin;
        return new Location(Bukkit.getWorld(worldname), x, y, z);
    }

    public int getTotalBlockSize() {
        return this.getHeight() * this.getXWidth() * this.getZWidth();
    }

    public int getXWidth() {
        return this.xMax - this.xMin + 1;
    }

    public int getZWidth() {
        return this.zMax - this.zMin + 1;
    }

    public boolean isIn(final Location loc) {
        return loc.getWorld() == Bukkit.getWorld(worldname) && loc.getBlockX() >= this.xMin && loc.getBlockX() <= this.xMax && loc.getBlockY() >= this.yMin && loc.getBlockY() <= this.yMax && loc
                .getBlockZ() >= this.zMin && loc.getBlockZ() <= this.zMax;
    }

    public boolean isIn(final Player player) {
        return this.isIn(player.getLocation());
    }

    public boolean isInWithMarge(final Location loc, final double marge) {
        return loc.getWorld() == Bukkit.getWorld(worldname) && loc.getX() >= this.xMinCentered - marge && loc.getX() <= this.xMaxCentered + marge && loc.getY() >= this.yMinCentered - marge && loc
                .getY() <= this.yMaxCentered + marge && loc.getZ() >= this.zMinCentered - marge && loc.getZ() <= this.zMaxCentered + marge;
    }
}