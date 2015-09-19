package com.github.maxopoly.repeatingEffects;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.mobs.MobConfig;

/**
 * Manages the spawning of completly configurable mobs. All of the mobs are kept
 * track of in an internal hashmap, currently a server restart will stop any
 * special effects of the mobs from working and they also wont drop special loot
 * afterwards, because they are being kept track of anymore
 * 
 * @author Max
 *
 */
public class MobSpawningHandler extends RepeatingEffect {

	private LinkedList<MobConfig> mobConfigs;
	private static HashMap<Monster, MobConfig> currentMobs;

	public MobSpawningHandler(JavaPlugin plugin, LinkedList<Area> areas,
			LinkedList<MobConfig> mobConfigs, long updateTime) {
		super(plugin, areas, updateTime);
		this.mobConfigs = mobConfigs;
		if (currentMobs == null) {
			currentMobs = new HashMap<Monster, MobConfig>();
		}

	}

	/**
	 * Attempt to spawn monsters near the player. Cycles through all the
	 * available mobconfigs for this area and tries to create each of them
	 * according to it's config. This can still fail because there is no free
	 * space for it or because of the spawn chance which is not taken into
	 * account before calling this method
	 */
	public void applyToPlayer(Player p) {
		if (p != null) {
			for (MobConfig mc : mobConfigs) {
				LinkedList<Monster> resultedMobs = mc
						.createMob(p.getLocation());
				if (resultedMobs != null) {
					for (Monster mob : resultedMobs) {
						if (mob != null) {
							currentMobs.put(mob, mc);
						}
					}
				}
			}
		}
	}

	/**
	 * Cycle through players and attempt to spawn mobs on them if they are in
	 * the right area
	 */
	public void run() {
		currentPlayers = getCurrentPlayers();
		for (Player p : currentPlayers) {
			if (isPlayerInArea(p)) {
				applyToPlayer(p);
			}
		}
		scheduleNextRun();
	}
	
	public static MobConfig getConfig(Monster mob) {
		MobConfig mb = currentMobs.get(currentMobs);
		return mb;
	}

}
