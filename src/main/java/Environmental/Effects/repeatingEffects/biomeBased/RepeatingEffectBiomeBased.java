package Environmental.Effects.repeatingEffects.biomeBased;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Environmental.Effects.repeatingEffects.RepeatingEffect;

public abstract class RepeatingEffectBiomeBased extends RepeatingEffect {
	protected Biome biome;

	public RepeatingEffectBiomeBased(JavaPlugin plugin, Biome biome) {
		super(plugin);
		this.biome = biome;
	}

	public boolean isPlayerinBiome(Player p) {
		return (p.getLocation().getBlock().getBiome() == biome);
	}

	public Biome getBiome() {
		return biome;
	}

	public void setBiome(Biome biome) {
		this.biome = biome;
	}

}
