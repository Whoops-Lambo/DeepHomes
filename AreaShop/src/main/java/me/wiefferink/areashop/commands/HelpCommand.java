package me.devaustin.deephomes.commands;

import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandDeepHomes {

	@Override
	public String getCommandStart() {
		return "deephomes help";
	}

	@Override
	public String getHelp(CommandSender target) {
		if(target.hasPermission("deephomes.help")) {
			return "help-help";
		}
		return null;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		plugin.getCommandManager().showHelp(sender);
	}
}
