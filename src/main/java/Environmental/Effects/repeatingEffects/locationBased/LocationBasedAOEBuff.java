package Environmental.Effects.repeatingEffects.locationBased;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LocationBasedAOEBuff extends RepeatingEffectLocationBased {
	PotionEffectType pet;
	int duration; // in ticks
	int level;

	public LocationBasedAOEBuff(JavaPlugin plugin, Shape shape, double size,
			Location loc, PotionEffectType pet, int level, int duration) {
		this(plugin, shape, size, size, loc, pet, duration, level);
	}

	public LocationBasedAOEBuff(JavaPlugin plugin, Shape shape, double sizeX,
			double sizeY, Location loc, PotionEffectType pet, int level,
			int duration) {
		super(plugin, shape, sizeX, sizeY, loc);
		this.pet = pet;
		this.level = level;
		this.duration = duration;
	}

	public void applyToPlayer(Player p) {
		if (isInRange(p.getLocation())) {
			PotionEffect pe = new PotionEffect(pet, level, duration);
			p.addPotionEffect(pe);
		}
	}

	public void run() {
		currentPlayers = getCurrentPlayers();
		for(Player p:currentPlayers) {
			applyToPlayer(p);
		}
		plugin.getServer().getScheduler()
		.scheduleSyncDelayedTask(plugin, this, duration/2);

	}

}
