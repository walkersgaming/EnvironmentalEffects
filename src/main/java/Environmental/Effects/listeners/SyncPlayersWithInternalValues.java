package Environmental.Effects.listeners;

import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import Environmental.Effects.managers.Manager;
import Environmental.Effects.repeatingEffects.biomeBased.DaytimeModifier;
import Environmental.Effects.repeatingEffects.biomeBased.WeatherMachine;

public class SyncPlayersWithInternalValues implements Listener{
	Manager manager;
	
	public SyncPlayersWithInternalValues(Manager manager) {
		this.manager = manager;
	}
	@EventHandler
	public void changeWeatherAndDaytimeOnLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p != null) {
			Biome b = p.getLocation().getBlock().getBiome();
			WeatherMachine wm = manager.getWeatherManager().get(b);
			wm.applyToPlayer(p);
			DaytimeModifier dtm = manager.getDaytimeManager().get(b);
			dtm.applyToPlayer(p);
		}
	}
	
	@EventHandler
	public void cancelAllNaturalWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

}
