package me.devaustin.deephomes.commands;

import me.devaustin.deephomes.tools.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class GrouplistCommand extends CommandDeepHomes {

	@Override
	public String getCommandStart() {
		return "deephomes grouplist";
	}

	@Override
	public String getHelp(CommandSender target) {
		if(target.hasPermission("deephomes.grouplist")) {
			return "help-grouplist";
		}
		return null;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("deephomes.grouplist")) {
			plugin.message(sender, "grouplist-noPermission");
			return;
		}
		List<String> groups = plugin.getFileManager().getGroupNames();
		if(groups.isEmpty()) {
			plugin.message(sender, "grouplist-noGroups");
		} else {
			plugin.message(sender, "grouplist-success", Utils.createCommaSeparatedList(groups));
		}
	}

	@Override
	public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
		return new ArrayList<>();
	}

}










