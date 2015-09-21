package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LightningControl extends RepeatingEffect {
	boolean dealDamage;
	int range;

	public LightningControl(JavaPlugin plugin, LinkedList<Area> areas,
			long updatetime, boolean dealDamage, int range) {
		super(plugin, areas, updatetime);
		this.dealDamage = dealDamage;
		this.range = range;
	}

	public void run() {
		currentPlayers = getCurrentPlayers();
		for (Player p : currentPlayers) {
			applyToPlayer(p);
		}
		scheduleNextRun();
	}

	public void applyToPlayer(Player p) {
		if (p != null && isPlayerInArea(p)) {
			int x = (int) p.getLocation().getX() + rng.nextInt(range * 2)
					- (range);
			int z = (int) p.getLocation().getZ() + rng.nextInt(range * 2)
					- (range);
			Location loc = new Location(p.getWorld(), x, p.getLocation()
					.getBlockY(), z);
			if (dealDamage) {
				p.getWorld().spigot().strikeLightning(loc, false);
			} else {
				p.getWorld().spigot().strikeLightningEffect(loc, false);
			}

		}
	}

}
