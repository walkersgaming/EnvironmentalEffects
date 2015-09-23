package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.datarepresentations.Area;

import vg.civcraft.mc.citadel.Citadel;
import vg.civcraft.mc.citadel.reinforcement.Reinforcement;

/**
 * Allows to decay reinforcements created by the citadel plugin
 * (https://github.com/civcraft/citadel)
 * 
 * @author Max
 *
 */
public class ReinforcementDecay extends RepeatingEffect {
	int reinforcementBreakPerRun;

	public ReinforcementDecay(JavaPlugin plugin, LinkedList<Area> areas,
			long updateTime, int reinforcementBreakPerRun) {
		super(plugin, areas, updateTime,null);
		this.reinforcementBreakPerRun = reinforcementBreakPerRun;
	}

	/**
	 * No implementation because this effect will be tied to locations
	 */
	public void applyToPlayer(Player p) {
	}

	/**
	 * Reduces every reinforcement in the areas of this instance by
	 * reinforcementBreakPerRun
	 */
	public void run() {
		for (Area a : areas) {
			int xsize = a.getxSize();
			int zsize = a.getzSize();
			Location loc = a.getCenter();
			switch (a.getShape()) {
			case CIRCLE:
				for (int x = loc.getBlockX() - xsize; x < loc.getBlockX()
						+ xsize; x++) {
					for (int z = loc.getBlockZ() - zsize; z < loc.getBlockZ()
							+ zsize; z++) {
						if (Math.sqrt(x * x + z * z) > xsize) {
							continue;
						}
						for (int y = 1; y < 256; y++) {
							Block current = loc.getWorld().getBlockAt(
									new Location(loc.getWorld(), x, y, z));
							if (current.getType() != Material.AIR) {
								Reinforcement r = Citadel
										.getReinforcementManager()
										.getReinforcement(current);
								if (r != null) {
									r.setDurability(r.getDurability()
											- reinforcementBreakPerRun);
								}
							}
						}
					}
				}
				break;
			case RECTANGLE:
				for (int x = loc.getBlockX() - xsize; x < loc.getBlockX()
						+ xsize; x++) {
					for (int z = loc.getBlockZ() - zsize; z < loc.getBlockZ()
							+ zsize; z++) {
						for (int y = 1; y < 256; y++) {
							Block current = loc.getWorld().getBlockAt(
									new Location(loc.getWorld(), x, y, z));
							Reinforcement r = Citadel.getReinforcementManager()
									.getReinforcement(current);
							if (r != null) {
								r.setDurability(r.getDurability()
										- reinforcementBreakPerRun);
							}
						}
					}
				}
				break;
			case GLOBAL:
			case BIOME:
				return;

			}
		}
	}
}
