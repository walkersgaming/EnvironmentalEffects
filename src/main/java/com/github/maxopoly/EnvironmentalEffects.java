package com.github.maxopoly;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.commands.CommandHandler;
import com.github.maxopoly.exceptions.ConfigParseException;
import com.github.maxopoly.listeners.MobListeners;
import com.github.maxopoly.listeners.SyncPlayersWithInternalValues;
import com.github.maxopoly.listeners.TerrainDamageListeners;
import com.github.maxopoly.managers.RepeatingEffectManager;
import com.github.maxopoly.repeatingEffects.RandomMobSpawningHandler;

public class EnvironmentalEffects extends JavaPlugin {
	private static JavaPlugin plugin;
	private CommandHandler commandHandler;
	private static RepeatingEffectManager manager;
	private ConfigParser cp;

	public void onEnable() {
		plugin = this;
		commandHandler = new CommandHandler(this);
		cp = new ConfigParser(this);
		try {
			manager = cp.parseConfig();
		} catch (ConfigParseException e) {
			e.printStackTrace();
		}
		registerListeners();
	}

	public static EnvironmentalEffects getPlugin() {
		return (EnvironmentalEffects) plugin;
	}
	
	public static RepeatingEffectManager getManager() {
		return manager;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return commandHandler.onCommand(sender, cmd, label, args);
	}

	public void onDisable() {
		RandomMobSpawningHandler.killAll();
	}

	/**
	 * Completly reloads the plugin. This allows dynamic config changing and
	 * reloading without server restarts
	 */
	public void reload() {
		sendConsoleMessage("Reloading config");
		RandomMobSpawningHandler.killAll();
		this.getServer().getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		cp = new ConfigParser(this);
		try {
			manager = cp.parseConfig();
		} catch (ConfigParseException e) {
			e.printStackTrace();
		}
		registerListeners();

	}

	/**
	 * Initializes and registers all the listener of this plugin
	 */
	public void registerListeners() {
		this.getServer()
				.getPluginManager()
				.registerEvents(new SyncPlayersWithInternalValues(manager),
						this);
		this.getServer().getPluginManager()
				.registerEvents(new MobListeners(cp.spawnerConfig, cp.cancelAllOtherSpawns), this);
		this.getServer()
				.getPluginManager()
				.registerEvents(
						new TerrainDamageListeners(cp.fireballTerrainDamage,
								cp.fireballTerrainIgnition,
								cp.disableFirespread), this);
	}

	public static void sendConsoleMessage(String message) {
		plugin.getLogger().info("[EnvironmentalEffects] " + message);
	}

}
