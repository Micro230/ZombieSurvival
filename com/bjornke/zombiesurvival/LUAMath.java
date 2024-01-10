package com.bjornke.zombiesurvival;

import java.util.Random;

/**
 * @author Micro230
 *
 */
public class LUAMath {
	
	private Random rand = new Random();
	
	public LUAMath() {
		
	}
	
	//CONVERT RANGE
	/**
	 * Convert range of integer while maintaining ratio
	 * @param value Input value
	 * @param oldmin Old range minimum value
	 * @param oldmax Old range maximum value
	 * @param newmin New range minimum value
	 * @param newmax New range maximum value
	 * @return Converted value
	 */
	public int ConvertRange(int value, int oldmin, int oldmax, int newmin, int newmax) {
		return (((value - oldmin) * (newmax - newmin)) / (oldmax - oldmin)) + newmin;
	}
	
	/**
	 * Convert range of long while maintaining ratio
	 * @param value Input value
	 * @param oldmin Old range minimum value
	 * @param oldmax Old range maximum value
	 * @param newmin New range minimum value
	 * @param newmax New range maximum value
	 * @return Converted value
	 */
	public long ConvertRange(long value, long oldmin, long oldmax, long newmin, long newmax) {
		return (((value - oldmin) * (newmax - newmin)) / (oldmax - oldmin)) + newmin;
	}
	
	/**
	 * Convert range of float while maintaining ratio
	 * @param value Input value
	 * @param oldmin Old range minimum value
	 * @param oldmax Old range maximum value
	 * @param newmin New range minimum value
	 * @param newmax New range maximum value
	 * @return Converted value
	 */
	public float ConvertRange(float value, float oldmin, float oldmax, float newmin, float newmax) {
		return (((value - oldmin) * (newmax - newmin)) / (oldmax - oldmin)) + newmin;
	}
	
	/**
	 * Convert range of double while maintaining ratio
	 * @param value Input value
	 * @param oldmin Old range minimum value
	 * @param oldmax Old range maximum value
	 * @param newmin New range minimum value
	 * @param newmax New range maximum value
	 * @return Converted value
	 */
	public double ConvertRange(double value, double oldmin, double oldmax, double newmin, double newmax) {
		return (((value - oldmin) * (newmax - newmin)) / (oldmax - oldmin)) + newmin;
	}

	//CLAMP
	/**
	 * Clamp a value between 2 integers
	 * @param value Value to clamp
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return Clamped value
	 */
	public double clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
	
	/**
	 * Clamp a value between 2 longs
	 * @param value Value to clamp
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return Clamped value
	 */
	public double clamp(long value, long min, long max) {
        return Math.max(min, Math.min(max, value));
    }
	
	/**
	 * Clamp a value between 2 floats
	 * @param value Value to clamp
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return Clamped value
	 */
	public double clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
	
	/**
	 * Clamp a value between 2 doubles
	 * @param value Value to clamp
	 * @param min Minimum value
	 * @param max Maximum value
	 * @return Clamped value
	 */
	public double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
	
	//RANDOM
	/**
	 * Gets a random number between 2 values
	 * @param min Random minimum value (Inclusive)
	 * @param max Random maximum value (Inclusive)
	 * @return Random value
	 */
	public int random(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Gets a random number between 2 longs<p>
	 * WARNING: This does NOT include the final number<p>
	 * Example: 0-1 does NOT include 1 but will include 0.9999~
	 * @param min Random minimum long (Inclusive)
	 * @param max Random maximum long (Inclusive up to the maximum value)
	 * @return Random value
	 */
	public long random(long min, long max) {
		return min + rand.nextLong() * (max - min);
	}
	
	/**
	 * Gets a random number between 2 floats<p>
	 * WARNING: This does NOT include the final number<p>
	 * Example: 0-1 does NOT include 1 but will include 0.9999~
	 * @param min Random minimum float (Inclusive)
	 * @param max Random maximum float (Inclusive up to the maximum value)
	 * @return Random value
	 */
	public float random(float min, float max) {
		return min + rand.nextFloat() * (max - min);
	}
	
	/**
	 * Gets a random number between 2 doubles<p>
	 * WARNING: This does NOT include the final number<p>
	 * Example: 0-1 does NOT include 1 but will include 0.9999~
	 * @param min Random minimum double (Inclusive)
	 * @param max Random maximum double (Inclusive up to the maximum value)
	 * @return Random value
	 */
	public double random(double min, double max) {
		return min + rand.nextDouble() * (max - min);
	}
}