package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Not functional yet
 * @author Max
 *
 */
public class EffectGenerator extends RepeatingEffect {
	private Effect effect;
	private int amount;
	private float speed;

	public EffectGenerator(JavaPlugin plugin, LinkedList<Area> areas, Effect effect, int amount,
			float speed, long updateTime) {
		super(plugin, areas,updateTime);
		this.effect = effect;
		this.amount = amount;
		this.speed = speed;
	}

	public void applyToPlayer(Player p) {
		if (isPlayerInArea(p)) {
			p.spigot().playEffect(p.getEyeLocation(), effect, 0,
					0, 0F, 0F, 0F, speed, amount, 1);
			//TODO fix this
		}
	}

	public void playAtLocation(Location loc, int radius) {
		loc.getWorld().playEffect(loc, effect, radius);
	}
	
	public void run() {
		currentPlayers = getCurrentPlayers();
		for(Player p:currentPlayers) {
			applyToPlayer(p);
		}
		scheduleNextRun();
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Effect getEffect() {
		return effect;
	}
	
	public int getAmount() {
		return amount;
	}

}
