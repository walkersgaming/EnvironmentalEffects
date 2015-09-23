package com.github.maxopoly;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.maxopoly.commands.CommandHandler;
import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.MobConfig;
import com.github.maxopoly.datarepresentations.Area.Shape;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;
import com.github.maxopoly.exceptions.ConfigParseException;
import com.github.maxopoly.listeners.MobListeners;
import com.github.maxopoly.listeners.SyncPlayersWithInternalValues;
import com.github.maxopoly.listeners.TerrainDamageListeners;
import com.github.maxopoly.managers.RepeatingEffectManager;
import com.github.maxopoly.repeatingEffects.DaytimeModifier;
import com.github.maxopoly.repeatingEffects.EffectGenerator;
import com.github.maxopoly.repeatingEffects.FireBallRain;
import com.github.maxopoly.repeatingEffects.MobSpawningHandler;
import com.github.maxopoly.repeatingEffects.PotionBuff;
import com.github.maxopoly.repeatingEffects.WeatherMachine;

public class EnvironmentalEffects extends JavaPlugin {
	private static JavaPlugin plugin;
	private CommandHandler commandHandler;
	private RepeatingEffectManager manager;
	private ConfigParser cp;

	public void onEnable() {
		reload();
		registerListeners();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return commandHandler.onCommand(sender, cmd, label, args);
	}

	public void onDisable() {

	}

	/**
	 * Completly reloads the plugin. This allows dynamic config changing and
	 * reloading without server restarts
	 */
	public void reload() {
		sendConsoleMessage("Reloading config");
		this.getServer().getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);
		cp = new ConfigParser(this);
		try {
			manager =  cp.parseConfig();
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
				.registerEvents(new MobListeners(), this);
		this.getServer()
				.getPluginManager()
				.registerEvents(
						new TerrainDamageListeners(cp.fireballTerrainDamage,
								cp.fireballTerrainIgnition, cp.disableFirespread),
						this);
	}

	public static void sendConsoleMessage(String message) {
		plugin.getLogger().info("[EnvironmentalEffects] " + message);
	}

}
