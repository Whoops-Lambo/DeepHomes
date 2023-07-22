package me.devaustin.deephomes.commands;

import me.devaustin.deephomes.regions.BuyRegion;
import me.devaustin.deephomes.regions.GeneralRegion;
import me.devaustin.deephomes.regions.RentRegion;
import me.devaustin.deephomes.tools.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DelfriendCommand extends CommandDeepHomes {

	@Override
	public String getCommandStart() {
		return "deephomes delfriend";
	}

	@Override
	public String getHelp(CommandSender target) {
		if(target.hasPermission("deephomes.delfriendall") || target.hasPermission("deephomes.delfriend")) {
			return "help-delFriend";
		}
		return null;
	}

	/**
	 * Check if a person can remove friends.
	 * @param person The person to check
	 * @param region The region to check for
	 * @return true if the person can remove friends, otherwise false
	 */
	public static boolean canUse(CommandSender person, GeneralRegion region) {
		if(person.hasPermission("deephomes.delfriendall")) {
			return true;
		}
		if(person instanceof Player) {
			Player player = (Player)person;
			return region.isOwner(player) && player.hasPermission("deephomes.delfriend");
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("deephomes.delfriend") && !sender.hasPermission("deephomes.delfriendall")) {
			plugin.message(sender, "delfriend-noPermission");
			return;
		}
		if(args.length < 2) {
			plugin.message(sender, "delfriend-help");
			return;
		}
		GeneralRegion region;
		if(args.length <= 2) {
			if(sender instanceof Player) {
				// get the region by location
				List<GeneralRegion> regions = Utils.getImportantRegions(((Player)sender).getLocation());
				if(regions.isEmpty()) {
					plugin.message(sender, "cmd-noRegionsAtLocation");
					return;
				} else if(regions.size() > 1) {
					plugin.message(sender, "cmd-moreRegionsAtLocation");
					return;
				} else {
					region = regions.get(0);
				}
			} else {
				plugin.message(sender, "cmd-automaticRegionOnlyByPlayer");
				return;
			}
		} else {
			region = plugin.getFileManager().getRegion(args[2]);
			if(region == null) {
				plugin.message(sender, "cmd-notRegistered", args[2]);
				return;
			}
		}
		if(sender.hasPermission("deephomes.delfriendall")) {
			if((region instanceof RentRegion && !((RentRegion)region).isRented())
					|| (region instanceof BuyRegion && !((BuyRegion)region).isSold())) {
				plugin.message(sender, "delfriend-noOwner", region);
				return;
			}
			OfflinePlayer friend = Bukkit.getOfflinePlayer(args[1]);
			if(!region.getFriendsFeature().getFriends().contains(friend.getUniqueId())) {
				plugin.message(sender, "delfriend-notAdded", friend.getName(), region);
				return;
			}
			if(region.getFriendsFeature().deleteFriend(friend.getUniqueId(), sender)) {
				region.update();
				plugin.message(sender, "delfriend-successOther", friend.getName(), region);
			}
		} else {
			if(sender.hasPermission("deephomes.delfriend") && sender instanceof Player) {
				if(region.isOwner((Player)sender)) {
					OfflinePlayer friend = Bukkit.getOfflinePlayer(args[1]);
					if(!region.getFriendsFeature().getFriends().contains(friend.getUniqueId())) {
						plugin.message(sender, "delfriend-notAdded", friend.getName(), region);
						return;
					}
					if(region.getFriendsFeature().deleteFriend(friend.getUniqueId(), sender)) {
						region.update();
						plugin.message(sender, "delfriend-success", friend.getName(), region);
					}
				} else {
					plugin.message(sender, "delfriend-noPermissionOther", region);
				}
			} else {
				plugin.message(sender, "delfriend-noPermission", region);
			}
		}
	}

	@Override
	public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
		ArrayList<String> result = new ArrayList<>();
		if(toComplete == 2) {
			for(Player player : Utils.getOnlinePlayers()) {
				result.add(player.getName());
			}
		} else if(toComplete == 3) {
			result.addAll(plugin.getFileManager().getRegionNames());
		}
		return result;
	}
}








