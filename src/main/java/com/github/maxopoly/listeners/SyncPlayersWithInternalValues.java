package com.github.maxopoly.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.maxopoly.managers.RepeatingEffectManager;
import com.github.maxopoly.repeatingEffects.DaytimeModifier;
import com.github.maxopoly.repeatingEffects.WeatherMachine;

public class SyncPlayersWithInternalValues implements Listener {
	RepeatingEffectManager manager;

	public SyncPlayersWithInternalValues(RepeatingEffectManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void changeWeatherAndDaytimeOnLogin(PlayerJoinEvent e) {
		updateValues(e.getPlayer());
	}

	@EventHandler
	public void cancelAllNaturalWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void changeWeatherAndDaytimeAfterDeath(PlayerRespawnEvent e) {
		updateValues(e.getPlayer());
	}

	public void updateValues(Player p) {
		if (p != null) {
			WeatherMachine wm = (WeatherMachine) manager.getEffect(
					WeatherMachine.class, p.getLocation());
			if (wm != null) {
				wm.applyToPlayer(p);
			}
			DaytimeModifier dtm = (DaytimeModifier) manager.getEffect(
					DaytimeModifier.class, p.getLocation());
			if (dtm != null) {
				dtm.applyToPlayer(p);
			}
		}
	}
}
