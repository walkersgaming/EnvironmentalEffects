package Environmental.Effects.managers;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Effect;
import org.bukkit.block.Biome;

import Environmental.Effects.repeatingEffects.biomeBased.EffectGenerator;

public class EffectManager {
	private HashMap <Biome,HashSet<EffectGenerator>> biomes;
	
	public EffectManager() {
		biomes = new HashMap <Biome,HashSet<EffectGenerator>> ();
	}
	
	public HashSet <EffectGenerator> getEffectsForBiome(Biome b) {
		return biomes.get(b);
	}
	
	public EffectGenerator getEffectGenerator(HashSet<EffectGenerator> set,Effect e) {
		for(EffectGenerator eg:set) {
			if (eg.getEffect() == e) {
				return eg;
			}
		}
		return null;
	}
	
	public EffectGenerator getEffectInBiome(Biome b, Effect e) {
		return getEffectGenerator(getEffectsForBiome(b), e);
	}
	
	public void add(EffectGenerator eg) {
		HashSet<EffectGenerator> temp;
		if (biomes.get(eg.getBiome())== null) {
			temp = new HashSet<EffectGenerator>();
			temp.add(eg);
			biomes.put(eg.getBiome(), temp);
		}
		else {
			temp = biomes.get(eg.getBiome());
			temp.add(eg);
		}
	}

}
