package com.github.maxopoly.environmentaleffects.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.environmentaleffects.datarepresentations.Area;
import com.github.maxopoly.environmentaleffects.datarepresentations.Area.Shape;
import com.github.maxopoly.environmentaleffects.exceptions.ConfigParseException;
import com.github.maxopoly.environmentaleffects.repeatingEffects.FireBallRain;
import com.github.maxopoly.environmentaleffects.repeatingEffects.LightningControl;
import com.github.maxopoly.environmentaleffects.repeatingEffects.WeatherMachine;

/**
 * Joke command, which disables any previous effects and enables permanent rain,
 * tons of fireball rain and tons of lightning
 * 
 * @author Max
 *
 */
public class WrathOfTheGods extends AbstractCommand {
	public WrathOfTheGods(JavaPlugin plugin) {
		super(plugin, "wrathofthegods");
	}

	@Override
	public boolean onCommand(CommandSender sender, List<String> args) {
		plugin.getServer().getScheduler().cancelTasks(plugin);
		plugin.getServer().broadcastMessage("THIS WORLD SHALL COME TO AN END");
		Area global = null;
		try {
			global = new Area(Shape.GLOBAL);
		} catch (ConfigParseException e) {

		}
		LinkedList<Area> areas = new LinkedList<Area>();
		areas.add(global);
		WeatherMachine wm = new WeatherMachine(areas, null, 1D, 3600000L, 200L);
		FireBallRain fbr = new FireBallRain(areas, null, 200L, 32, null);
		LightningControl lc = new LightningControl(areas, null, 300L, null,
				true, 64);
		sender.sendMessage("MAXIMUM DESTRUCTION ACTIVATED");
		return true;
	}
}
