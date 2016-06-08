package com.github.maxopoly.environmentaleffects.datarepresentations;

import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

/**
 * This simulates an environmental state of a player. Because this plugin
 * desyncs the server's weather and time with the player's, this is needed to
 * let other effects check whether a specific weather or day/night combination
 * is met. The Booleans describing states are null if the state doesn't matter,
 * for example if it just has to be night, but the rainstate doesn't matter.
 * Night is here defined as >12000 ticks, which means 6pm - 6am, the values for
 * that are from the minecraft wiki and the official day/night end
 * 
 * @author Max
 *
 */
public class PlayerEnvironmentState {
	private Boolean rain;
	private Boolean night;

	public PlayerEnvironmentState(Boolean rain, Boolean night) {
		this.rain = rain;
		this.night = night;
	}

	/**
	 * Gets whether a player meets the conditions defined in this instance
	 * 
	 * @param p
	 *            Player to check
	 * @return true if the player meets the conditions, false if not
	 */
	public boolean conditionMet(Player p) {
		if (rain == null
				|| (p.getPlayerWeather() == WeatherType.DOWNFALL) == rain) {
			if (night == null || (p.getPlayerTime() > 12000L) == night) {
				return true;
			}
		}
		return false;
	}
	
	public Boolean getRain() {
		return rain;
	}
	public Boolean getNight() {
		return night;
	}
}
