package com.github.maxopoly.environmentaleffects.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.maxopoly.environmentaleffects.EnvironmentalEffects;

public class Reload extends AbstractCommand {
	public Reload(JavaPlugin plugin) {
		super(plugin, "reload");
	}

	@Override
	public boolean onCommand(CommandSender sender, List<String> args) {
		sender.sendMessage("Reloading Environmental Effects");
		((EnvironmentalEffects)plugin).reload();
		return true;
	}

}
