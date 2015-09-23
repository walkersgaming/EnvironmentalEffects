package com.github.maxopoly.repeatingEffects;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

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
	final protected JavaPlugin plugin;
	protected Collection<? extends Player> currentPlayers;
	protected long updateTime;
	protected LinkedList<Area> areas;
	protected PlayerEnvironmentState pes;
	protected Random rng;

	public RepeatingEffect(JavaPlugin plugin, LinkedList<Area> areas,
			long updateTime, PlayerEnvironmentState pes) {
		this.plugin = plugin;
		this.updateTime = updateTime;
		this.areas = areas;
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
	 * Changes how often the effect is updated for each player
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
	 * effects are applied by this instance
	 * 
	 * @param loc
	 *            location be compared with the areas
	 * @return true if the location is inside the area affected by this
	 *         instance, false if not
	 */
	public boolean isInArea(Location loc) {
		for (Area a : areas) {
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
	 * @return true if the player fulfills all the criteria, false if not
	 */
	public boolean conditionsMet(Player p) {
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

}
