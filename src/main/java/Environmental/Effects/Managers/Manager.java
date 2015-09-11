package Environmental.Effects.managers;

import Environmental.Effects.repeatingEffects.biomeBased.DaytimeModifier;
import Environmental.Effects.repeatingEffects.biomeBased.WeatherMachine;

public class Manager {
	private final GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> weatherManager;
	private final GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> dayTimeManager;
	private final EffectManager effectManager;
	public Manager(
			GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> weatherManager,
			GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> dayTimeManager,
			EffectManager effectManager
			) {
		this.weatherManager = weatherManager;
		this.dayTimeManager = dayTimeManager;
		this.effectManager = effectManager;

	}
	
	public GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> getWeatherManager() {
		return weatherManager;
	}
	
	public GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> getDaytimeManager() {
		return dayTimeManager;
	}
	
	public EffectManager getEffectManager() {
		return effectManager;
	}
}
