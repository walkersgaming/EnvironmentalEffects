package com.github.maxopoly.repeatingEffects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.github.maxopoly.datarepresentations.Area;

public class DispenserBuff extends RepeatingEffect {
	private HashMap<PotionEffect, Double> onHitDebuffs;
	int extraDamage;

	public DispenserBuff(JavaPlugin plugin, LinkedList<Area> areas,
			int extraDamage, HashMap<PotionEffect, Double> onHitDebuffs) {
		super(plugin, areas, 24 * 60 * 60 * 30, null);
		this.onHitDebuffs = onHitDebuffs;
		this.extraDamage = extraDamage;
	}

	public void applyToPlayer(Player p) {
		p.damage((double) extraDamage);
		if (onHitDebuffs != null) {
			for (Map.Entry<PotionEffect, Double> current : onHitDebuffs
					.entrySet()) {
				if (rng.nextDouble() <= current.getValue()) {
					p.addPotionEffect(current.getKey(), true);
				}
			}
		}
	}

	public HashMap<PotionEffect, Double> getDebuffs() {
		return onHitDebuffs;
	}
}
