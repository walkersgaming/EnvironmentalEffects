package com.github.maxopoly.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor {
	private JavaPlugin plugin;
	HashMap<String, AbstractCommand> commands = new HashMap<String, AbstractCommand>();

	public CommandHandler(JavaPlugin instance) {
		plugin = instance;

		plugin.getCommand("ee").setExecutor(this);

		registerCommands(new AbstractCommand[] {new Reload(plugin)});
	}

	private void registerCommands(AbstractCommand[] abstractCommands) {

		for (AbstractCommand abstractCommand : abstractCommands) {
			commands.put(abstractCommand.getName(), abstractCommand);
			List<String> aliases = abstractCommand.getAliases();
			if (abstractCommand.getAliases() != null) {
				for (String alias : aliases) {
					commands.put(alias, abstractCommand);
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (args.length == 0 || !commands.containsKey(args[0].toLowerCase()))
			return false;

		AbstractCommand abstractCommand = commands.get(args[0].toLowerCase());

		if (abstractCommand.getPermission() != null
				&& !sender.hasPermission(abstractCommand.getPermission())) {
			sender.sendMessage("You don't have the permission to use this command!");
			return true;
		}

		if (abstractCommand.onCommand(sender,
				Arrays.asList(args).subList(1, args.length)) == false
				&& abstractCommand.getUsage() != null) {
			sender.sendMessage("The correct usage is"
					+ abstractCommand.getUsage());
		}

		return true;
	}
}
