package Environmental.Effects;

import org.bukkit.plugin.java.JavaPlugin;

import Environmental.Effects.Managers.Manager;
import Environmental.Effects.listeners.SyncPlayersWithInternalValues;

public class EnvironmentalEffects extends JavaPlugin {
	Manager manager;

	public void onEnable() {
		parseConfig();
		registerListeners();
		startScheduledEvents();
	}

	public void parseConfig() {

	}

	public void onDisable() {

	}

	/**
	 * Completly reloads the plugin. This allows dynamic config changing and
	 * reloading without server restarts
	 */
	public void reload() {

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

	/**
	 * Schedules all the stuff is run on a regular base in the background
	 */
	public void startScheduledEvents() {

	}

}
