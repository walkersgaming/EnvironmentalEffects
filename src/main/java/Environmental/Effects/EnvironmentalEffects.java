package Environmental.Effects;

import java.util.Map;

import org.bukkit.Effect;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import Environmental.Effects.listeners.SyncPlayersWithInternalValues;
import Environmental.Effects.managers.EffectManager;
import Environmental.Effects.managers.GeneralManagerRepeatingEffectBiomeBased;
import Environmental.Effects.managers.Manager;
import Environmental.Effects.repeatingEffects.biomeBased.DaytimeModifier;
import Environmental.Effects.repeatingEffects.biomeBased.EffectGenerator;
import Environmental.Effects.repeatingEffects.biomeBased.WeatherMachine;

public class EnvironmentalEffects extends JavaPlugin {
	Manager manager;
	GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> weatherManager;
	GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> dayTimeManager;
	EffectManager effectManager;

	public void onEnable() {
		createManagers();
		parseConfig();
		registerListeners();
	}

	public void parseConfig() {
		sendConsoleMessage("Initializing config");
		FileConfiguration config = getConfig();
		long rainUpdate = config.getLong("rainupdate");
		long timeUpdate = config.getLong("timeupdate");
		for (String biomeString : config.getKeys(false)) {
			Biome biome = Biome.valueOf(biomeString);
			ConfigurationSection biomeSection = config
					.getConfigurationSection(biomeString);

			// Intialize weather machines
			ConfigurationSection weatherSection = biomeSection
					.getConfigurationSection("weathermachine");
			double rainChance = weatherSection.getDouble("rain_Chance");
			long minRainDuration = weatherSection
					.getLong("minimum_rain_duration");
			WeatherMachine wm = new WeatherMachine(this, biome, rainChance,
					minRainDuration, rainUpdate);
			weatherManager.add(wm);

			// Initialize daytime modifier
			ConfigurationSection dayTimeSection = biomeSection
					.getConfigurationSection("daytime_modifier");
			DaytimeModifier dtm;
			if (dayTimeSection.get("permanent_time") != null) {
				Long permanentTime = dayTimeSection.getLong("permanent_time");
				dtm = new DaytimeModifier(this, biome, permanentTime, 0F,
						timeUpdate);
			} else {
				Float daytimeSpeed = (float) dayTimeSection
						.getDouble("dayspeed");
				dtm = new DaytimeModifier(this, biome, null, daytimeSpeed,
						timeUpdate);
			}
			dtm.scheduleNextRun();
			dayTimeManager.add(dtm);

			// Initialize effects
			ConfigurationSection effectSection = config
					.getConfigurationSection("effects");
			Map<String, Object> effects = effectSection.getValues(false);
			for (Map.Entry<String, Object> currentEffect : effects.entrySet()) {
				ConfigurationSection detailsCurrentEffect = effectSection
						.getConfigurationSection(currentEffect.getKey());
				Effect effectType = Effect.getByName(detailsCurrentEffect.getString("effect_Type"));
				double speed = detailsCurrentEffect.getDouble("speed");
				int amount = detailsCurrentEffect.getInt("amount");
				long delay = detailsCurrentEffect.getLong("delay");
				EffectGenerator eg = new EffectGenerator(this,biome,effectType,amount,(float)speed,delay);
				effectManager.add(eg);
			}
		}

	}

	public void onDisable() {

	}

	public void createManagers() {
		weatherManager = new GeneralManagerRepeatingEffectBiomeBased<WeatherMachine>();
		dayTimeManager = new GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier>();
		effectManager = new EffectManager();
		manager = new Manager(weatherManager, dayTimeManager,effectManager);

	}

	/**
	 * Completly reloads the plugin. This allows dynamic config changing and
	 * reloading without server restarts
	 */
	public void reload() {
		sendConsoleMessage("Reloading config");
		this.getServer().getScheduler().cancelTasks(this);
		createManagers();
		parseConfig();

	}

	/**
	 * Initializes and registers all the listener of this plugin
	 */
	public void registerListeners() {
		this.getServer()
				.getPluginManager()
				.registerEvents(new SyncPlayersWithInternalValues(manager),
						this);

	}

	public void sendConsoleMessage(String message) {
		this.getLogger().info("[EnvironmentalEffects] " + message);
	}

}
