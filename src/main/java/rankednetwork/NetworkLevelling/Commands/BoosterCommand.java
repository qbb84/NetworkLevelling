package rankednetwork.NetworkLevelling.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

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

					if (statistic == null) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <statistic> " + ChatColor.RED + " <scope> <type>");
						return false;
					}
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> <statistic> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <scope> " + ChatColor.RED + "<type>");
						return false;
					}
					try {
						type = BoosterType.valueOf(args[4].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> <statistic> <scope> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + "<type>");
						return false;
					}

					manager.create(boosterName, statistic, scope, type, p);
					break;
				} else {
					if (args.length != 6) {
						return false;
					}
					String boosterName = args[1];
					String statistic = (NetworkStatistic.getAvailableStats().toString().toLowerCase().contains(args[2].toLowerCase())) ? args[2] : null;
					BoosterScope scope = null;
					if (statistic == null) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <statistic> " + ChatColor.RED + " <scope> <multiplier> <duration>");
					}
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> <statistic> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <scope> " + ChatColor.RED + "<multiplier> <duration>");
						return false;
					}
					double multiplier;
					long duration;
					try {
						multiplier = Double.parseDouble(args[4]);
					} catch (NumberFormatException exception) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> <statistic> <scope> " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <multiplier>" + ChatColor.RED + " <duration>");
						return false;
					}
					try {
						duration = Long.parseLong(args[5]);
					} catch (NumberFormatException exception) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create <name> <statistic> <scope> <multiplier>" + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <duration>");
						return false;
					}

					manager.create(boosterName, statistic, scope, BoosterType.CUSTOM, multiplier, duration, p);
					break;
				}
			case "remove":
				if (args.length != 4) {
					return false;
				}
				Player pl = Bukkit.getPlayer(args[1]);
				if (pl == null) {
					p.sendMessage(ChatColor.RED + " Usage: /booster remove " + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <player>" + "<boostername> <count>");
					return false;
				}
				String boosterName = args[2];
				int count;
				try {
					count = Integer.parseInt(args[3]);
				} catch (NumberFormatException ex) {
					p.sendMessage(ChatColor.RED + " Usage: /booster remove <player> <boostername>" + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <count>");
					return false;
				}
				manager.removeBooster(pl, boosterName, count);
				break;
			case "queue":
				if (args[1].equalsIgnoreCase("list")) {
					manager.displayBoosterQueue(p);
					break;
				} else {
					p.sendMessage(ChatColor.RED + " Usage: /booster queue" + ChatColor.DARK_RED + ChatColor.UNDERLINE + " <list...>");
				}
				Player activationPlayer = Bukkit.getPlayer(args[1]);
				if (activationPlayer == null) {
					p.sendMessage(ChatColor.RED + " Usage: /booster queue" + ChatColor.RED + ChatColor.UNDERLINE + " <player>" + ChatColor.RED + "<booster_name>");
					return false;
				}
				if (args.length != 3) {
					p.sendMessage(ChatColor.RED + " Usage: /booster queue <player> " + ChatColor.RED + ChatColor.UNDERLINE + "<booster_name>");
					return false;
				}
				String bName = args[2];
				manager.queueBooster(manager.getBoosterForPlayer(activationPlayer, bName), activationPlayer, bName);
				break;
			case "give":
				if (args.length != 4) {
					return false;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					p.sendMessage(ChatColor.RED + " Usage: /booster give" + ChatColor.RED + ChatColor.UNDERLINE + " <player> " + ChatColor.RED + "<booster_name> <amount>");
					return false;
				}
				String boosterAlias = args[2];
				int amount = 0;
				try {
					amount = Integer.parseInt(args[3]);
				} catch (NumberFormatException exception) {
					p.sendMessage(ChatColor.RED + " Usage: /booster give <player> <booster_name>" + ChatColor.RED + ChatColor.UNDERLINE + " <amount>");
					return false;
				}
				manager.giveBooster(player, boosterAlias, amount).getDurationInMinutes();
				break;
			case "stats":
				sender.sendMessage(NetworkStatistic.statsList());
				break;
			case "view":
				if (args.length != 2) {
					p.sendMessage(ChatColor.RED + " Usage: /booster view " + ChatColor.RED + ChatColor.UNDERLINE + " <player>");
					return false;
				}

				if (Bukkit.getPlayer(args[2]) == null) {
					p.sendMessage(ChatColor.RED + " Error: that player isn't online.");
					return false;
				}


				manager.viewBoostersOfPlayer(Bukkit.getPlayer(args[2]), p);
				break;
			case "types":
				break;

			default:
				sender.sendMessage("create/remove/activate/give/give");
				break;
		}

		return true;
	}
}
