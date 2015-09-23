package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;

/**
 * Spawns lightning around players, this can be either just a visual effect or
 * actual lightning, which deals damage.
 * 
 * @author Max
 *
 */
public class LightningControl extends RepeatingEffect {
	boolean dealDamage;
	int range;

	public LightningControl(JavaPlugin plugin, LinkedList<Area> areas,
			long updatetime, PlayerEnvironmentState pes, boolean dealDamage,
			int range) {
		super(plugin, areas, updatetime, pes);
		this.dealDamage = dealDamage;
		this.range = range;
	}

	/**
	 * Creates a single lightning somewhere around a player if the conditions
	 * are met
	 */
	public void applyToPlayer(Player p) {
		if (conditionsMet(p)) {
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
