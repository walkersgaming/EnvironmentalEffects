package Environmental.Effects.managers;

import java.util.HashSet;

import org.bukkit.block.Biome;

import Environmental.Effects.repeatingEffects.biomeBased.RepeatingEffectBiomeBased;

public class GeneralManagerRepeatingEffectBiomeBased<K> {
	private HashSet<RepeatingEffectBiomeBased> set;

	public GeneralManagerRepeatingEffectBiomeBased() {
		set = new HashSet<RepeatingEffectBiomeBased>();
	}

	public void add(RepeatingEffectBiomeBased rebb) {
		set.add(rebb);
	}

	public K get(Biome b) {
		for (RepeatingEffectBiomeBased rebb : set) {
			if (rebb.getBiome() == b) {
				return (K) rebb;
			}
		}
		return null; // no rebb for this biome exists
	}
}
