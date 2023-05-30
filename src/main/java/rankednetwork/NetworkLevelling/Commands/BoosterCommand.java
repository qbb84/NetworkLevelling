package rankednetwork.NetworkLevelling.Commands;

import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.*;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.awt.*;
import java.util.Arrays;

public class BoosterCommand implements CommandExecutor {


	BoosterManager manager = BoosterManager.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

		if (!s.equals("booster")) {
			return false;
		}
		if (!(args.length > 0)) {
			return false;
		}
		if (!(sender instanceof Player)) {
			return false;
		}
		Player p = (Player) sender;

		switch (args[0].toLowerCase()) {
			case "create":
				if (args.length == 5) {
					String boosterName = args[1];
					String statistic = (NetworkStatistic.getAvailableStats().toString().toLowerCase().contains(args[2].toLowerCase())) ? args[2] : null;
					BoosterScope scope = null;
					BoosterType type = null;
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
						type = BoosterType.valueOf(args[4].toUpperCase());
					} catch (IllegalArgumentException ex) {
						return false;
					}
					if (statistic == null) return false;
					if (scope == null) return false;
					if (type == null) return false;

					manager.create(boosterName, statistic, scope, type, p);
					break;
				}else {
					if (args.length != 6) {
						return false;
					}
					String boosterName = args[1];
					String statistic = (NetworkStatistic.getAvailableStats().toString().toLowerCase().contains(args[2].toLowerCase())) ? args[2] : null;
					BoosterScope scope = null;
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
					} catch (IllegalArgumentException ex) {
						return false;
					}
					double multiplier;
					long duration;
					try {
						multiplier = Double.parseDouble(args[4]);
						duration = Long.parseLong(args[5]);
					} catch (NumberFormatException exception) {
						return false;
					}
					if (statistic == null) return false;
					if (scope == null) return false;

					manager.create(boosterName, statistic, scope, BoosterType.CUSTOM, multiplier, duration, p);
					break;
				}
			case "remove":
				if(args.length != 4){return false;}
				Player pl = Bukkit.getPlayer(args[1]);
				if(pl == null){return false;}
				String boosterName = args[2];
				int count = Integer.parseInt(args[3]);
				manager.removeBooster(pl, boosterName, count);
				break;
			case "queue":
				if(args[1].equalsIgnoreCase("list")){
					manager.displayBoosterQueue(p);
					break;
				}
				Player activationPlayer = Bukkit.getPlayer(args[1]);
				if(activationPlayer == null) {return false;}
				if(args.length != 3){return false;}
				String bName = args[2];
				manager.queueBooster(manager.getBoosterForPlayer(activationPlayer, bName), activationPlayer, bName);
				break;
			case "give":
				if (args.length != 4) {return false;}
				Player player = Bukkit.getPlayer(args[1]);
				if(player == null){return false;}
				String boosterAlias = args[2];
				int amount = 0;
				try {
					amount = Integer.parseInt(args[3]);
				} catch (NumberFormatException exception) {
					return false;
				}
				manager.giveBooster(player, boosterAlias, amount);
				break;
			case "stats":
				sender.sendMessage(NetworkStatistic.statsList());
				break;
			case "view":
				if(Bukkit.getPlayer(args[1]) == null){return false;}
				//if(args.length != 1) {return false;}
				manager.viewBoostersOfPlayer(Bukkit.getPlayer(args[1]), p);
				break;
			case "types":
				break;

			default:
				sender.sendMessage("create/remove/activate/give");
				break;
		}

		return true;
	}
}
