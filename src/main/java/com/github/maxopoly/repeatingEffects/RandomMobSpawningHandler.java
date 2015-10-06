package com.github.maxopoly.repeatingEffects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.MobConfig;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;

/**
 * Manages the spawning of completly configurable mobs. All of the mobs are kept
 * track of in an internal hashmap, currently a server restart will stop any
 * special effects of the mobs from working and they also wont drop special loot
 * afterwards, because they are being kept track of anymore
 * 
 * @author Max
 *
 */
public class RandomMobSpawningHandler extends RepeatingEffect {

	private LinkedList<MobConfig> mobConfigs;
	private static HashMap<Monster, MobConfig> currentMobs = new HashMap<Monster, MobConfig>();;

	public RandomMobSpawningHandler(JavaPlugin plugin, LinkedList<Area> areas,
			LinkedList<MobConfig> mobConfigs, long updateTime,
			PlayerEnvironmentState pes) {
		super(plugin, areas, updateTime, pes);
		this.mobConfigs = mobConfigs;
	}

	/**
	 * Attempt to spawn monsters near the player. Cycles through all the
	 * available mobconfigs for this area and tries to create each of them
	 * according to it's config. This can still fail because there is no free
	 * space for it or because of the spawn chance which is not taken into
	 * account before calling this method
	 */
	public void applyToPlayer(Player p) {
		if (conditionsMet(p)) {
			for (MobConfig mc : mobConfigs) {
				LinkedList<Monster> resultedMobs = mc
						.createMob(p.getLocation());
				if (resultedMobs != null) {
					for (Monster mob : resultedMobs) {
						if (mob != null) {
							addMonster(mob, mc);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Kills all mobs currently handled by any MobSpawningHandler. This is run every time the 
	 * this plugin (or in most cases the server) is disabled, because this plugin is currently not 
	 * hooked up to a database to make drops and effects consistent over restarts.
	 */
	public static void killAll() {
		for(Map.Entry<Monster,MobConfig> current:currentMobs.entrySet()) {
			current.getKey().remove();
		}
		
	}

	public static MobConfig getConfig(Monster mob) {
		MobConfig mb = currentMobs.get(mob);
		return mb;
	}

	public static void addMonster(Monster mob, MobConfig config) {
		currentMobs.put(mob, config);
	}
	
	public static void removeMonster(Monster mob) {
		currentMobs.remove(mob);
	}

}
