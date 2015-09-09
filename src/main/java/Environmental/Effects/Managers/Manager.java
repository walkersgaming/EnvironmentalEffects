package Environmental.Effects.Managers;

import Environmental.Effects.repeatingEffects.biomeBased.DaytimeModifier;
import Environmental.Effects.repeatingEffects.biomeBased.WeatherMachine;

public class Manager {
	private GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> weatherManager;
	private GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> dayTimeManager;
	public Manager(
			GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> weatherManager,
			GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> dayTimeManager) {
		this.weatherManager = weatherManager;
		this.dayTimeManager = dayTimeManager;

	}
	
	public GeneralManagerRepeatingEffectBiomeBased<WeatherMachine> getWeatherManager() {
		return weatherManager;
	}
	
	public GeneralManagerRepeatingEffectBiomeBased<DaytimeModifier> getDaytimeManager() {
		return dayTimeManager;
	}
}
