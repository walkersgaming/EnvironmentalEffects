package com.github.maxopoly.listeners;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.github.maxopoly.datarepresentations.MobConfig;
import com.github.maxopoly.repeatingEffects.MobSpawningHandler;

public class MobListeners implements Listener {
	Random rng;

	public MobListeners() {
		rng = new Random();
	}

	@EventHandler
	public void monsterDamagePlayer(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Monster
				&& e.getEntity() instanceof Player) {
			Entity damager = e.getDamager();
			MobConfig config = MobSpawningHandler.getConfig((Monster) damager);
			if (config != null) {
				HashMap<PotionEffect, Double> debuffs = config
						.getOnHitDebuffs();
				if (debuffs != null) {
					Entity damaged = e.getEntity();
					for (Map.Entry<PotionEffect, Double> current : debuffs
							.entrySet()) {
						if (rng.nextDouble() <= current.getValue()) {
							((Player) damaged).addPotionEffect(
									current.getKey(), true);
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void monsterDeath(EntityDeathEvent e) {
		Entity en = e.getEntity();
		MobConfig config;
		if (en instanceof Monster
				&& (config = MobSpawningHandler.getConfig((Monster) en)) != null) {
			if (((LivingEntity) en).getKiller() instanceof Player) {
				String deathMsg = config.getDeathMessage();
				if (deathMsg != null && !deathMsg.equals("")) {
					((LivingEntity) en).getKiller().sendMessage(deathMsg);
				}
			}
			List<ItemStack> drops = e.getDrops();
			drops.clear();
			LinkedList<ItemStack> dropsToInsert = config.getDrops();
			if (dropsToInsert != null) {
				for (ItemStack is : dropsToInsert) {
					drops.add(is);
				}
			}

		}
	}

}
