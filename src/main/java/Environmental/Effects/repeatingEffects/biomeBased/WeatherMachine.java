package Environmental.Effects.repeatingEffects.biomeBased;

import java.util.Random;

import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Allows to set randomize weather in specific biomes to configured values. The
 * weather will only be changed client side, because this supports multiple
 * biomes on the same map and server side rain is global. Use this classes
 * methods to determine whether it's raining for players right now, they might
 * be in async with the server's weather. Players might walk into our biome at
 * any time, so this runnable constantly schedules itself to ensure the weather
 * is right for every player. This doesn't mean it's rerolled everytime run() is
 * run
 * 
 * @author Max
 *
 */
public class WeatherMachine extends RepeatingEffectBiomeBased {

	double rainChance; // between 0 and 1, where 1 is 100%
	long minRainDuration; // in ticks
	Random RNG;
	boolean rain;
	WeatherType currentWeather;
	long rainUpdate;
	int i;

	WeatherMachine(JavaPlugin plugin, Biome biome, double rainChance,
			long minRainDuration, long rainUpdate) {
		super(plugin, biome);
		this.rainChance = rainChance;
		this.minRainDuration = minRainDuration;
		this.rainUpdate = rainUpdate;
		RNG = new Random();
		run();
	}

	public void run() {
		i += rainUpdate;
		if (i >= minRainDuration) {
			i = 0;
			willItRain();
		}
		setRainForPlayers();
		scheduleNextRun();
	}

	/**
	 * Sets the weather for all players in this classes biome to the currently
	 * selected weather type
	 */
	public void setRainForPlayers() {
		currentPlayers = getCurrentPlayers();
		for (Player p : currentPlayers) {
			if (p.getPlayerWeather() != getCurrentWeatherType()) {
				applyToPlayer(p);
			}
		}
	}

	/**
	 * Schedules the next run in 10 seconds to update rain for all players
	 */
	public void scheduleNextRun() {
		plugin.getServer().getScheduler()
				.scheduleSyncDelayedTask(plugin, this, rainUpdate);
	}

	/**
	 * Recalculates whether it should rain based on probabilities
	 */
	public void willItRain() {
		rain = RNG.nextDouble() <= rainChance;
		if (rain) {
			currentWeather = WeatherType.DOWNFALL;
		} else {
			currentWeather = WeatherType.CLEAR;
		}
	}

	/**
	 * Getter for the rain bool
	 * 
	 * @return true if it's raining, false if not
	 */
	public boolean isItRaining() {
		return rain;
	}

	/**
	 * Getter for the WeatherType
	 * 
	 * @return current WeatherType
	 */
	public WeatherType getCurrentWeatherType() {
		if (isItRaining()) {
			return WeatherType.DOWNFALL;
		}
		return WeatherType.CLEAR;
	}
	
	public void applyToPlayer(Player p) {
		if (isPlayerinBiome(p)) {
			p.setPlayerWeather(getCurrentWeatherType());
		}
	}

}
