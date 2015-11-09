package com.github.maxopoly.repeatingEffects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
	private static HashMap<UUID, MobConfig> currentMobs = new HashMap<UUID, MobConfig>();;

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
				LinkedList<Entity> resultedMobs = mc
						.createMob(p.getLocation());
				if (resultedMobs != null) {
					for (Entity mob : resultedMobs) {
						if (mob != null) {
							addEntity(mob, mc);
						}
					}
				}
			}
		}
	}

	/**
	 * Kills all mobs currently handled by any MobSpawningHandler. This is run
	 * every time the this plugin (or in most cases the server) is disabled,
	 * because this plugin is currently not hooked up to a database to make
	 * drops and effects consistent over restarts.
	 */
	public static void killAll() {
		for (World w : Bukkit.getWorlds()) {
			for (Entity e : w.getEntities()) {
				if (currentMobs.containsKey(e.getUniqueId())) {
					e.remove();
					currentMobs.remove(e.getUniqueId());
				}
			}
		}

	}

	/**
	 * Gets the stored mob config for a given entity
	 * 
	 * @param e
	 *            Entity whose config is wanted
	 * @return The config of the given entity if one exists or null if no config
	 *         for this entity exists
	 */
	public static MobConfig getConfig(Entity e) {
		MobConfig mb = currentMobs.get(e.getUniqueId());
		return mb;
	}

	/**
	 * Adds an entity together with a config to the internal tracking
	 * 
	 * @param e
	 *            Entity to add
	 * @param config
	 *            Config to add
	 */
	public static void addEntity(Entity e, MobConfig config) {
		currentMobs.put(e.getUniqueId(), config);
	}

	/**
	 * Removes an entity and it's config from the internal tracking
	 * 
	 * @param e
	 *            Entity to remove
	 */
	public static void removeEntity(Entity e) {
		currentMobs.remove(e.getUniqueId());
	}

}
