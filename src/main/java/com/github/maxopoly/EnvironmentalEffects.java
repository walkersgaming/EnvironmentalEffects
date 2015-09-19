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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.maxopoly.commands.CommandHandler;
import com.github.maxopoly.exceptions.ConfigParseException;
import com.github.maxopoly.listeners.MobListeners;
import com.github.maxopoly.listeners.SyncPlayersWithInternalValues;
import com.github.maxopoly.managers.RepeatingEffectManager;
import com.github.maxopoly.mobs.MobConfig;
import com.github.maxopoly.repeatingEffects.Area;
import com.github.maxopoly.repeatingEffects.Area.Shape;
import com.github.maxopoly.repeatingEffects.DaytimeModifier;
import com.github.maxopoly.repeatingEffects.EffectGenerator;
import com.github.maxopoly.repeatingEffects.FireBallRain;
import com.github.maxopoly.repeatingEffects.MobSpawningHandler;
import com.github.maxopoly.repeatingEffects.PotionBuff;
import com.github.maxopoly.repeatingEffects.WeatherMachine;

public class EnvironmentalEffects extends JavaPlugin {
	private static JavaPlugin plugin;
	private RepeatingEffectManager manager;
	private CommandHandler commandHandler;

	public void onEnable() {
		createManagers();
		try {
			parseConfig();
		} catch (ConfigParseException e) {
			e.printStackTrace();
		}
		registerListeners();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		return commandHandler.onCommand(sender, cmd, label, args);
	}

	/**
	 * Parses the config, creates everything needed and adds it to the manager
	 */
	public void parseConfig() throws ConfigParseException {
		sendConsoleMessage("Initializing config");
		this.saveDefaultConfig();
		this.reloadConfig();
		FileConfiguration config = getConfig();
		long rainUpdate = config.getLong("rainupdate");
		sendConsoleMessage("Rain for players will be updated every "
				+ rainUpdate + " ticks");
		long timeUpdate = config.getLong("timeupdate");
		sendConsoleMessage("Daytime for players will be updated every "
				+ timeUpdate + " ticks");
		String worldname = config.getString("worldname");
		sendConsoleMessage("Worldname is:" + worldname);

		// Intialize weather machines
		ConfigurationSection weatherSection = config
				.getConfigurationSection("weathermachines");
		if (weatherSection != null) {
			for (String key : weatherSection.getKeys(false)) {
				ConfigurationSection currentWeatherSection = weatherSection
						.getConfigurationSection(key);
				double rainChance = currentWeatherSection
						.getDouble("rain_chance");
				long minRainDuration = parseTime(currentWeatherSection
						.getString("minimum_rain_duration"));
				LinkedList<Area> areas = parseAreas(
						currentWeatherSection.getConfigurationSection("areas"),
						worldname);
				WeatherMachine wm = new WeatherMachine(this, areas, rainChance,
						minRainDuration, rainUpdate);
				wm.scheduleNextRun();
				manager.add(wm);
				sendConsoleMessage("Initialized weather machine for " + key
						+ ", rainchance: " + rainChance
						+ " ,minimum rain duration: " + minRainDuration);
			}
		} else {
			sendConsoleMessage("No weather config found weather will be vanilla!");
		}

		// Initialize daytime modifier
		ConfigurationSection dayTimeSection = config
				.getConfigurationSection("daytime_modifier");
		if (dayTimeSection != null) {
			for (String key : dayTimeSection.getKeys(false)) {
				ConfigurationSection currentSection = dayTimeSection
						.getConfigurationSection(key);
				DaytimeModifier dtm;
				Float daytimeSpeed = (float) currentSection
						.getDouble("dayspeed");
				long startingTime = parseTime(currentSection
						.getString("starting_time"));
				LinkedList<Area> areas = parseAreas(
						currentSection.getConfigurationSection("areas"),
						worldname);
				dtm = new DaytimeModifier(this, areas, startingTime,
						daytimeSpeed, timeUpdate);

				dtm.scheduleNextRun();
				manager.add(dtm);
				sendConsoleMessage("Initialized daytime modifier " + key
						+ "starting time:" + startingTime + ", daytime speed:"
						+ daytimeSpeed);
			}
		} else {
			sendConsoleMessage("No daytime config found for biome daytime will be vanilla!");
		}

		// Initialize effects
		ConfigurationSection effectSection = config
				.getConfigurationSection("effects");
		if (effectSection != null) {
			for (String currentEffect : effectSection.getKeys(false)) {
				ConfigurationSection detailsCurrentEffect = effectSection
						.getConfigurationSection(currentEffect);
				Effect effectType = Effect.valueOf(detailsCurrentEffect
						.getString("effect_type"));
				double speed = detailsCurrentEffect.getDouble("speed");
				int amount = detailsCurrentEffect.getInt("amount");
				long delay = parseTime(detailsCurrentEffect.getString("delay"));
				LinkedList<Area> areas = parseAreas(
						detailsCurrentEffect.getConfigurationSection("areas"),
						worldname);
				EffectGenerator eg = new EffectGenerator(this, areas,
						effectType, amount, (float) speed, delay);
				eg.scheduleNextRun();
				manager.add(eg);
				sendConsoleMessage("Initialized effect handler for "
						+ currentEffect + "type: " + effectType.getName()
						+ " ,speed: " + speed + ", amount: " + amount
						+ ", delay: " + delay);
			}
		}

		// Intialize cool fireballs
		ConfigurationSection fireballSection = config
				.getConfigurationSection("fireball");
		if (fireballSection != null) {
			for (String key : fireballSection.getKeys(false)) {
				ConfigurationSection currentSection = fireballSection
						.getConfigurationSection(key);
				long frequency = parseTime(currentSection
						.getString("frequency"));
				int range = currentSection.getInt("range");
				LinkedList<Area> areas = parseAreas(
						currentSection.getConfigurationSection("areas"),
						worldname);
				FireBallRain fbr = new FireBallRain(this, areas, frequency,
						range);
				fbr.scheduleNextRun();
				manager.add(fbr);
				sendConsoleMessage("Loaded fireball rain " + key
						+ "; frequency:" + frequency + ", range:" + range);
			}
		}

		// Initialize potion buffs
		ConfigurationSection potionSection = config
				.getConfigurationSection("potioneffects");
		if (potionSection != null) {
			for (String key : potionSection.getKeys(false)) {
				ConfigurationSection currentSection = potionSection
						.getConfigurationSection(key);
				PotionEffectType pet = PotionEffectType
						.getByName(currentSection.getString("type"));
				int duration = currentSection.getInt("duration");
				int level = currentSection.getInt("level");
				LinkedList<Area> areas = parseAreas(
						currentSection.getConfigurationSection("areas"),
						worldname);
				PotionBuff pb = new PotionBuff(this, areas, pet, level,
						duration);
				pb.scheduleNextRun();
				manager.add(pb);
			}

		}

		// Initialize mobs
		ConfigurationSection mobSection = config
				.getConfigurationSection("monster");
		if (mobSection != null) {
			for (String key : mobSection.getKeys(false)) {
				ConfigurationSection currentSection = mobSection
						.getConfigurationSection(key);
				LinkedList<Area> areas = parseAreas(
						currentSection.getConfigurationSection("areas"),
						worldname);
				long updateTime = parseTime(currentSection
						.getString("updatetime"));
				LinkedList<MobConfig> mobconfigs = new LinkedList<MobConfig>();
				ConfigurationSection mobconfigsection = currentSection
						.getConfigurationSection("mobconfig");
				if (mobconfigsection != null) {
					for (String mobkey : mobconfigsection.getKeys(false)) {
						ConfigurationSection currentMobConfig = mobconfigsection
								.getConfigurationSection(mobkey);
						EntityType type = EntityType.valueOf(currentMobConfig
								.getString("type"));
						String name = currentMobConfig.getString("name", null);
						int range = currentMobConfig.getInt("range", 32);
						int amount = currentMobConfig.getInt("amount", 1);
						int maximumTries = currentMobConfig.getInt(
								"maximum_spawn_attempts", 5);
						String deathmsg = currentMobConfig.getString(
								"deathmessage", null);
						double spawnChance = currentMobConfig
								.getDouble("spawn_chance");
						ConfigurationSection dropsSection = currentMobConfig
								.getConfigurationSection("drops");
						LinkedList<ItemStack> drops = null;
						if (dropsSection != null) {
							drops = getItemStacks(dropsSection);
						}
						ConfigurationSection armourSection = currentMobConfig
								.getConfigurationSection("equipment");
						LinkedList<ItemStack> armour = null;
						if (armourSection != null) {
							armour = getItemStacks(armourSection);
						}
						HashMap<PotionEffectType, Integer> buffs = new HashMap<PotionEffectType, Integer>();
						ConfigurationSection buffSection = currentMobConfig
								.getConfigurationSection("buffs");
						if (buffSection != null) {
							for (String buffkey : buffSection.getKeys(false)) {
								ConfigurationSection currentBuffSection = potionSection
										.getConfigurationSection(buffkey);
								PotionEffectType pet = PotionEffectType
										.getByName(currentBuffSection
												.getString("type"));
								int level = currentBuffSection.getInt("level");
								buffs.put(pet, level);
							}
						}
						ConfigurationSection onHitDebuffSection = currentMobConfig
								.getConfigurationSection("on_hit_debuffs");
						HashMap<PotionEffect, Double> onHitDebuffs = new HashMap<PotionEffect, Double>();
						if (onHitDebuffSection != null) {
							for (String debuffkey : onHitDebuffSection
									.getKeys(false)) {
								ConfigurationSection currentDebuffSection = onHitDebuffSection
										.getConfigurationSection(debuffkey);
								PotionEffectType pet = PotionEffectType
										.getByName(currentDebuffSection
												.getString("type"));
								int level = currentDebuffSection
										.getInt("level");
								long duration = parseTime(currentDebuffSection
										.getString("duration"));
								double chance = currentDebuffSection
										.getDouble("chance");
								PotionEffect pe = new PotionEffect(pet,
										(int) duration, level);
								onHitDebuffs.put(pe, chance);
							}
							MobConfig mobconfig = new MobConfig(type, name,
									buffs, armour, drops, onHitDebuffs,
									deathmsg, spawnChance, amount, range,
									maximumTries);
							mobconfigs.add(mobconfig);
						}
					}
				} else {
					throw new ConfigParseException("No mobconfigs for" + key);
				}
				MobSpawningHandler msh = new MobSpawningHandler(this, areas,
						mobconfigs, updateTime);
				msh.scheduleNextRun();
				manager.add(msh);
			}
		}
	}

	public void onDisable() {

	}

	public void createManagers() {
		plugin = this;
		commandHandler = new CommandHandler(this);
		manager = new RepeatingEffectManager();

	}

	/**
	 * Completly reloads the plugin. This allows dynamic config changing and
	 * reloading without server restarts
	 */
	public void reload() {
		sendConsoleMessage("Reloading config");
		this.getServer().getScheduler().cancelTasks(this);
		createManagers();
		try {
			parseConfig();
		} catch (ConfigParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Initializes and registers all the listener of this plugin
	 */
	public void registerListeners() {
		this.getServer()
				.getPluginManager()
				.registerEvents(new SyncPlayersWithInternalValues(manager),
						this);
		this.getServer()
		.getPluginManager()
		.registerEvents(new MobListeners(),
				this);
	}

	public static void sendConsoleMessage(String message) {
		plugin.getLogger().info("[EnvironmentalEffects] " + message);
	}

	public LinkedList<Area> parseAreas(ConfigurationSection cs, String worldname)
			throws ConfigParseException {
		LinkedList<Area> areas = new LinkedList<Area>();
		List<String> biomes = cs.getStringList("biomes");
		ConfigurationSection locs = cs.getConfigurationSection("locations");
		if (biomes != null) {
			for (String current : biomes) {
				if (current.toLowerCase().equals("global")) {
					Area global = new Area(Shape.GLOBAL);
					areas.add(global);
					return areas;
				}
				Biome b = Biome.valueOf(current);
				if (b != null) {
					Area temp = new Area(Shape.BIOME, b);
					areas.add(temp);
				} else {
					throw new ConfigParseException(current + " is not a biome");
				}
			}
		}
		if (locs != null) {
			for (String current : locs.getKeys(false)) {
				ConfigurationSection currentSection = locs
						.getConfigurationSection(current);
				Shape shape = Shape.valueOf(currentSection.getString("shape"));
				int xSize = currentSection.getInt("xsize");
				int zSize = currentSection.getInt("zsize");
				Location center = parseLocation(
						currentSection.getConfigurationSection("center"),
						worldname);
				Area temp = new Area(shape, center, xSize, zSize);
				if (temp != null) {
					areas.add(temp);
				}
			}
		}
		return areas;
	}

	public Location parseLocation(ConfigurationSection c, String worldname) {
		long x = c.getLong("x");
		long y = c.getLong("y",0L);
		long z = c.getLong("z");
		return new Location(plugin.getServer().getWorld(worldname), x, y, z);
	}

	public long parseTime(String arg) throws ConfigParseException {
		long result = 0;
		boolean set = true;
		try {
			Long.parseLong(arg);
		} catch (NumberFormatException e) {
			set = false;
		}
		if (set) {
			return result;
		}
		while (arg != "") {
			int length = 0;
			switch (arg.charAt(arg.length() - 1)) {
			case 't': // ticks
				long ticks = getLastNumber(arg);
				result += ticks;
				length = String.valueOf(ticks).length() + 1;
				break;
			case 's': // seconds
				long seconds = getLastNumber(arg);
				result += 20 * seconds; // 20 ticks in a second
				length = String.valueOf(seconds).length() + 1;
				break;
			case 'm': // minutes
				long minutes = getLastNumber(arg);
				result += 20 * 60 * minutes;
				length = String.valueOf(minutes).length() + 1;
				break;
			case 'h': // hours
				long hours = getLastNumber(arg);
				result += 20 * 3600;
				length = String.valueOf(hours).length() + 1;
				break;
			default:
				throw new ConfigParseException(arg.charAt(arg.length() - 1)
						+ " is not a valid time description character");
			}
			arg = arg.substring(0, arg.length() - length);
		}
		return result;
	}

	public long getLastNumber(String arg) {
		StringBuilder number = new StringBuilder();
		for (int i = arg.length() - 2; i >= 0; i--) {
			if (Character.isDigit(arg.charAt(i))) {
				number.append(arg.substring(i, i + 1));
			} else {
				break;
			}
		}
		long result = Long.parseLong(number.toString());
		return result;
	}

	public LinkedList<ItemStack> getItemStacks(ConfigurationSection cs) {
		LinkedList<ItemStack> result = new LinkedList<ItemStack>();
		for (String key : cs.getKeys(false)) {
			ConfigurationSection currentSection = cs
					.getConfigurationSection(key);
			Material material = Material.getMaterial(currentSection
					.getString("material"));
			int amount = currentSection.getInt("amount", 1);
			ItemStack item = new ItemStack(material, amount);
			ItemMeta meta = item.getItemMeta();
			String displayName = currentSection.getString("display_name");
			if (displayName != null) {
				meta.setDisplayName(displayName);
			}
			String lore = currentSection.getString("lore");
			if (lore != null) {
				List<String> lorelist = new LinkedList<String>();
				lorelist.add(lore);
				meta.setLore(lorelist);
			}
			ConfigurationSection enchants = currentSection
					.getConfigurationSection("enchants");
			if (enchants != null) {
				for (String enchantKey : enchants.getKeys(false)) {
					ConfigurationSection enchantSection = enchants
							.getConfigurationSection(enchantKey);
					Enchantment enchant = Enchantment.getByName(enchantSection
							.getString("enchant"));
					int level = enchantSection.getInt("level");
					meta.addEnchant(enchant, level, true);
				}
			}
			item.setItemMeta(meta);
			result.add(item);
		}
		return result;
	}

}
