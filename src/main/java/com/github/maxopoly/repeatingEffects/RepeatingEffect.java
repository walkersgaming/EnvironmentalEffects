package com.github.maxopoly.repeatingEffects;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.maxopoly.EnvironmentalEffects;
import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;

/**
 * Pretty much every effect caused by this plugin repeats in some sort of way,
 * this provides a super class for any repeating effect with some convenience
 * methods
 * 
 * @author Max
 *
 */
public abstract class RepeatingEffect implements Runnable {
	/**
	 * needed as reference for internal scheduling of events
	 */
	final protected EnvironmentalEffects plugin;
	protected Collection<? extends Player> currentPlayers;
	protected long updateTime;
	protected LinkedList<Area> includedAreas;
	protected LinkedList<Area> excludedAreas;
	protected PlayerEnvironmentState pes;
	protected Random rng;

	public RepeatingEffect(LinkedList<Area> includedAreas,
			LinkedList<Area> excludedAreas, long updateTime,
			PlayerEnvironmentState pes) {
		this.plugin = EnvironmentalEffects.getPlugin();
		this.updateTime = updateTime;
		this.includedAreas = includedAreas;
		this.excludedAreas = excludedAreas;
		this.rng = new Random();
		this.pes = pes;
		plugin.getServer().getScheduler()
				.scheduleSyncRepeatingTask(plugin, this, 0L, updateTime);
	}

	/**
	 * Should apply what ever effect the class gives out to the player or
	 * attempt to apply it
	 * 
	 * @param p
	 *            player to which the effect should be applied to
	 */
	public abstract void applyToPlayer(Player p);

	/**
	 * @return all players currently on this server
	 */
	public Collection<? extends Player> getCurrentPlayers() {
		return plugin.getServer().getOnlinePlayers();
	}

	/**
	 * When rescheduled, this amount of ticks later the effect will applied
	 * again. 20 ticks = 1 second
	 * 
	 * @return amount of ticks between runs
	 */
	public long getUpdateTime() {
		return updateTime;
	}

	/**
	 * Changes how often the effect is updated for each player. Be aware that
	 * this will not affect the already scheduled task
	 * 
	 * @param updateTime
	 *            how often the effect should be updated (in ticks)
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Standard run method for an effect. May be overwritten by a subclass
	 */
	public void run() {
		for (Player p : getCurrentPlayers()) {
			applyToPlayer(p);
		}
	}

	/**
	 * Checks whether the given location is inside any of the areas in which
	 * effects are applied by this instance and not in a restricted area
	 * 
	 * @param loc
	 *            location be compared with the areas
	 * @return true if the location is inside the area affected by this
	 *         instance, false if not
	 */
	public boolean isInArea(Location loc) {
		for (Area a : includedAreas) {
			if (a.isInArea(loc) && !isInRestrictedArea(loc)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks whether a given location is in the area which is restricted for
	 * this effect
	 * 
	 * @param loc
	 * @return
	 */
	public boolean isInRestrictedArea(Location loc) {
		if (excludedAreas == null) {
			return false;
		}
		for (Area a : excludedAreas) {
			if (a.isInArea(loc)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Convenience method to check whether the player is in the right area and
	 * whether the environmental conditions are met
	 * 
	 * @param p
	 *            player to check
	 * @return true if the player fulfills all the criteria, false if not or if
	 *         the player has the permission to bypass effects
	 */
	public boolean conditionsMet(Player p) {
		if (p.hasPermission("EE.admin")) {
			return false;
			// this permission allows to ignore any effects
		}
		return environmentConditionMet(p) && isPlayerInArea(p);
	}

	/**
	 * Checks whether the environmental conditions for this instance are met for
	 * a player. If the conditions are null, it's always assumed they are true,
	 * also checks whether the player is null
	 * 
	 * @param p
	 *            player to check
	 * @return true if the player meets the conditions, false if not
	 */
	public boolean environmentConditionMet(Player p) {
		return p != null && (pes == null || pes.conditionMet(p));
	}

	/**
	 * Checks whether the given player is inside any of the areas in which
	 * effects are applied by this instance
	 * 
	 * @param p
	 *            player whos location is compared with the areas
	 * @return true if the player is inside the effects area, false if not
	 */
	public boolean isPlayerInArea(Player p) {
		return isInArea(p.getLocation());
	}

	/**
	 * @return Whether this instance applies globally
	 */
	public boolean isGlobal() {
		for (Area a : includedAreas) {
			if (a.isGlobal()) {
				return true;
			}
		}
		return false;
	}

}
