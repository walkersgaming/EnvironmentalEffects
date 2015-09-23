package com.github.maxopoly.repeatingEffects;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;

public class PotionBuff extends RepeatingEffect {
	private PotionEffectType pet;
	private int duration; // in ticks
	private int level;

	public PotionBuff(JavaPlugin plugin, LinkedList<Area> areas,
			PotionEffectType pet, int level, int duration,
			PlayerEnvironmentState pes) {
		super(plugin, areas, (duration / 4) * 3, pes);
		this.pet = pet;
		this.level = level;
		this.duration = duration;
	}

	public void applyToPlayer(Player p) {
		if (conditionsMet(p)) {
			PotionEffect pe = new PotionEffect(pet, level, duration);
			p.addPotionEffect(pe);
		}
	}

	/**
	 * @return length of the potion effect applied in ticks
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Changes the duration of the potion effect, which is applied by this
	 * instance. If a permanent effect is wanted, the updateTime will also need
	 * to be adjusted after changing this
	 * 
	 * @param duration
	 *            new duration for the potion effect
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * @return the level of the potion effect applied by this instance
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Change the level of the potion effect applied by this instance
	 * 
	 * @param level
	 *            new level of the effect
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
