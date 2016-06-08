package com.github.maxopoly.environmentaleffects.listeners.effects;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import com.github.maxopoly.environmentaleffects.Effect;
import com.github.maxopoly.environmentaleffects.datarepresentations.Area;
import com.github.maxopoly.environmentaleffects.datarepresentations.MobConfig;
import com.github.maxopoly.environmentaleffects.repeatingEffects.RandomMobSpawningHandler;

public class SpawnerSpawnModifier extends Effect implements Listener {
	HashMap<EntityType, MobConfig> spawnerConfig;

	public SpawnerSpawnModifier(LinkedList<Area> includedAreas,
			LinkedList<Area> excludedAreas,
			HashMap<EntityType, MobConfig> spawnerConfig) {
		super(includedAreas, excludedAreas, null);
		this.spawnerConfig = spawnerConfig;
		plugin.getServer().getPluginManager()
		.registerEvents(this, plugin);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void spawnerSpawn(SpawnerSpawnEvent e) {
		MobConfig mc = spawnerConfig.get(e.getEntityType());
		if (isInArea(e.getLocation()) && mc != null) {
			e.setCancelled(true);
			LinkedList<Entity> spawned = mc.createMob(e.getLocation());
			if (spawned != null) {
				for (Entity m : spawned) {
					RandomMobSpawningHandler.addEntity(m, mc);
				}
			}
		}
	}
	
	public Collection<MobConfig> getConfigs() {
		return spawnerConfig.values();
	}
}
