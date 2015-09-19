package com.github.maxopoly.managers;

import java.util.HashSet;
import java.util.LinkedList;

import org.bukkit.Location;

import com.github.maxopoly.repeatingEffects.RepeatingEffect;

public class RepeatingEffectManager {
	private HashSet<RepeatingEffect> set;

	public RepeatingEffectManager() {
		set = new HashSet<RepeatingEffect>();
	}

	public void add(RepeatingEffect re) {
		set.add(re);
	}

	public LinkedList<RepeatingEffect> getEffects(
			Class<? extends RepeatingEffect> c) {
		LinkedList<RepeatingEffect> result = new LinkedList<RepeatingEffect>();
		for (RepeatingEffect re : set) {
			if (re.getClass() == c) {
				result.add(re);
			}
		}
		return result;
	}
	
	public LinkedList <RepeatingEffect> getEffectsForLocation(Location loc) {
		LinkedList<RepeatingEffect> result = new LinkedList<RepeatingEffect>();
		for (RepeatingEffect re : set) {
			if (re.isInArea(loc)) {
				result.add(re);
			}
		}
		return result;
	}
	
	public RepeatingEffect getEffect(Class<? extends RepeatingEffect> c,Location loc) {
		for(RepeatingEffect re:set) {
			if(re.getClass()==c && re.isInArea(loc)) {
				return re;
			}
		}
		return null;
	}
}
