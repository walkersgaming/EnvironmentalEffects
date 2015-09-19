package com.github.maxopoly.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Reload extends AbstractCommand {
	public Reload(JavaPlugin plugin) {
		super(plugin, "reload");
	}

	@Override
	public boolean onCommand(CommandSender sender, List<String> args) {
		plugin.reloadConfig();
		return true;
	}

}
