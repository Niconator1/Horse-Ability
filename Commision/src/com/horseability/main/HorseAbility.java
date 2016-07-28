package com.horseability.main;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class HorseAbility {

	private Player user;
	private Horse[] horses;
	private Vector direction;
	private int duration = 8*20;

	public HorseAbility(Player user, Horse[] horses, Vector direction) {
		this.user = user;
		this.horses = horses;
		this.direction = direction;
	}

	public Player getUser() {
		return this.user;
	}

	public Horse[] getHorses() {
		return this.horses;
	}

	public Vector getDirection() {
		return this.direction;
	}

	public int getDuration() {
		return this.duration;
	}

	public void decreaseDuration() {
		this.duration--;
	}
}
