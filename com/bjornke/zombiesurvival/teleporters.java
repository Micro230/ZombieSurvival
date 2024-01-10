package com.bjornke.zombiesurvival;

public class teleporters {
	
	private boolean tele1 = false;
	private boolean tele2 = false;
	private boolean tele3 = false;
	private int teleporterAmountInMap = 0;
	
	public teleporters(int amount) {
		this.teleporterAmountInMap = amount;
	}
	
	public void activateTeleporter(int number) {
		switch(number) {
		case 1:
			this.tele1 = true;
			break;
		case 2:
			this.tele2 = true;
			break;
		case 3:
			this.tele3 = true;
			break;
		}
	}
	
	public boolean isTeleporterActive(int number) {
		switch(number) {
		case 1:
			if(this.tele1) {
				return true;
			}
			else {
				return false;
			}
		case 2:
			if(this.tele2) {
				return true;
			}
			else {
				return false;
			}
		case 3:
			if(this.tele3) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	public boolean areAllTeleportersActive() {
		switch(teleporterAmountInMap) {
		case 1:
			if(tele1) {
				return true;
			}
		case 2:
			if(tele1 && tele2) {
				return true;
			}
		case 3:
			if(tele1 && tele2 && tele3) {
				return true;
			}
		}
		return false;
	}
}
