package com.github.maxopoly.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.exceptions.ConfigParseException;
import com.github.maxopoly.repeatingEffects.Area;
import com.github.maxopoly.repeatingEffects.Area.Shape;
import com.github.maxopoly.repeatingEffects.FireBallRain;
import com.github.maxopoly.repeatingEffects.LightningControl;
import com.github.maxopoly.repeatingEffects.WeatherMachine;

public class WrathOfTheGods extends AbstractCommand {
	public WrathOfTheGods(JavaPlugin plugin) {
		super(plugin, "wrathofthegods");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, List<String> args) {
		sender.sendMessage("THIS WORLD SHALL COME TO AN END");
		plugin.getServer().getScheduler().cancelTasks(plugin);
		Area global=null;
		try {
		global = new Area(Shape.GLOBAL);
		}
		catch (ConfigParseException e) {
			
		}
		LinkedList <Area> areas = new LinkedList <Area> ();
		areas.add(global);
		WeatherMachine wm = new WeatherMachine(plugin, areas, 1D, 3600000L, 200L);
		wm.scheduleNextRun();
		FireBallRain fbr = new FireBallRain(plugin,areas,200L,32);
		fbr.scheduleNextRun();
		LightningControl lc = new LightningControl(plugin, areas, 300L, true, 64);
		lc.scheduleNextRun();
		sender.sendMessage("MAXIMUM DESTRUCTION ACTIVATED");
		return true;
	}
}
