package com.github.maxopoly.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class AbstractCommand {
	protected final JavaPlugin plugin;
	protected final String name;

	public AbstractCommand(JavaPlugin instance, String commandName) {
		plugin = instance;
		name = commandName;
	}

	public abstract boolean onCommand(CommandSender sender, List<String> args);

	public boolean onConsoleCommand(CommandSender sender, List<String> args) {
		return onCommand(sender, args);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		try {
			return plugin.getCommand("ee " + name).getDescription();
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getUsage() {
		try {
			return plugin.getCommand("ee " + name).getUsage();
		} catch (NullPointerException e) {
			return null;
		}
	}

	public String getPermission() {
		try {
			return plugin.getCommand("ee " + name).getPermission();
		} catch (NullPointerException e) {
			return null;
		}
	}

	public List<String> getAliases() {
		try {
			return plugin.getCommand("ee " + name).getAliases();
		} catch (NullPointerException e) {
			return null;
		}
	}
}
