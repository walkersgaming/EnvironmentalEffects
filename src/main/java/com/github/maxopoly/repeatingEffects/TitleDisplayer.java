package com.github.maxopoly.repeatingEffects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.datarepresentations.Area;
import com.github.maxopoly.datarepresentations.PlayerEnvironmentState;

public class TitleDisplayer extends RepeatingEffect {
	private String title;
	private String subtitle;
	private HashMap <UUID, Boolean> alreadyShownToPlayer;

	public TitleDisplayer(JavaPlugin plugin, LinkedList<Area> areas, long updateTime,
			PlayerEnvironmentState pes, String title, String subtitle) {
		super(plugin, areas, updateTime, pes);
		this.title = title;
		this.subtitle = subtitle;
		alreadyShownToPlayer = new HashMap<UUID, Boolean>();
	}

	public void applyToPlayer(Player p) {
		UUID uuid = p.getUniqueId();
		if (conditionsMet(p)) {
			if (!alreadyShownToPlayer.get(uuid)) {
				sendTitle(p);
				alreadyShownToPlayer.put(uuid, true);
			}
		}
		else {
			alreadyShownToPlayer.put(uuid, false);
		}
	}
	
	public void sendTitle(Player p) {
		//TitleManager.sendTimings(p,20,40,20);
		p.sendTitle(title, subtitle);
	}
	
	public void addPlayer(Player p, boolean initialValue) {
		alreadyShownToPlayer.put(p.getUniqueId(), initialValue);
	}

}
