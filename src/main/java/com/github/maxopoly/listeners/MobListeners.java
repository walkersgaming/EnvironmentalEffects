package com.github.maxopoly.listeners;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;

import com.github.maxopoly.datarepresentations.MobConfig;
import com.github.maxopoly.repeatingEffects.MobSpawningHandler;

public class MobListeners implements Listener {
	Random rng;

	public MobListeners() {
		rng = new Random();
	}

	@EventHandler
	public void monsterDamagePlayer(EntityDamageByEntityEvent e) {
		if (e.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Monster damager = null;
		if (e.getDamager() instanceof Monster) {
			damager = (Monster) e.getDamager();
		} else {
			if (e.getDamager() instanceof Projectile) {
				ProjectileSource ps = ((Projectile) e.getDamager())
						.getShooter();
				if (ps instanceof Monster) {
					damager = (Monster) ps;
				} else {
					return;
				}
			} else {
				return;
			}
		}
		MobConfig config = MobSpawningHandler.getConfig(damager);
		if (config != null) {
			HashMap<PotionEffect, Double> debuffs = config.getOnHitDebuffs();
			if (debuffs != null) {
				for (Map.Entry<PotionEffect, Double> current : debuffs
						.entrySet()) {
					if (rng.nextDouble() <= current.getValue()) {
						((Player) e.getEntity()).addPotionEffect(
								current.getKey(), true);
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
			MobSpawningHandler.removeMonster((Monster) en);
			if (((LivingEntity) en).getKiller() instanceof Player) {
				String deathMsg = config.getDeathMessage();
				if (deathMsg != null && !deathMsg.equals("")) {
					((LivingEntity) en).getKiller().sendMessage(deathMsg);
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

}
