package Environmental.Effects.repeatingEffects.biomeBased;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class EffectGenerator extends RepeatingEffectBiomeBased {
	private Effect effect;
	private int amount;
	private float speed;
	private long delayBetweenRuns;

	public EffectGenerator(JavaPlugin plugin, Biome biome, Effect effect, int amount,
			float speed, long delayBetweenRuns) {
		super(plugin, biome);
		this.effect = effect;
		this.amount = amount;
		this.speed = speed;
		this.delayBetweenRuns = delayBetweenRuns;
	}

	public void applyToPlayer(Player p) {
		if (isPlayerinBiome(p)) {
			p.spigot().playEffect(p.getEyeLocation(), effect, effect.getId(),
					0, 0F, 0F, 0F, speed, amount, 1);
		}
	}

	public void playAtLocation(Location loc, int radius) {
		loc.getWorld().playEffect(loc, effect, radius);
	}
	
	public void run() {
		currentPlayers = getCurrentPlayers();
		for(Player p:currentPlayers) {
			applyToPlayer(p);
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, delayBetweenRuns);
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public long getDelay() {
		return delayBetweenRuns;
	}
	
	public Effect getEffect() {
		return effect;
	}
	
	public int getAmount() {
		return amount;
	}

}
