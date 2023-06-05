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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BoosterCommand implements CommandExecutor {


	BoosterManager manager = BoosterManager.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

		if (!s.equals("bos")) {
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
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] " + ChatColor.DARK_RED + " [statistic] " + ChatColor.RED + " [scope] [type]");
						break;
					}
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] [statistic] " + ChatColor.DARK_RED + " [scope] " + ChatColor.RED + "[type]");
						break;
					}
					try {
						type = BoosterType.valueOf(args[4].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] [statistic] [scope] " + ChatColor.DARK_RED + "[type]");
						break;
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
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] " + ChatColor.DARK_RED + " [statistic] " + ChatColor.RED + " [scope] [multiplier] [duration]");
					}
					try {
						scope = BoosterScope.valueOf(args[3].toUpperCase());
					} catch (IllegalArgumentException ex) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] [statistic] " + ChatColor.DARK_RED + " [scope] " + ChatColor.RED + "[multiplier] [duration]");
						break;
					}
					double multiplier;
					long duration;
					try {
						multiplier = Double.parseDouble(args[4]);
					} catch (NumberFormatException exception) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] [statistic] [scope] " + ChatColor.DARK_RED + " [multiplier]" + ChatColor.RED + " [duration]");
						break;
					}
					try {
						duration = Long.parseLong(args[5]);
					} catch (NumberFormatException exception) {
						p.sendMessage(ChatColor.RED + " Usage: /booster create [name] [statistic] [scope] [multiplier]" + ChatColor.DARK_RED + " [duration]");
						break;
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
					p.sendMessage(ChatColor.RED + " Usage: /booster remove " + ChatColor.DARK_RED + " [player]" + " [name] [count]");
					break;
				}
				String boosterName = args[2];
				int count;
				try {
					count = Integer.parseInt(args[3]);
				} catch (NumberFormatException ex) {
					p.sendMessage(ChatColor.RED + " Usage: /booster remove [player] [name]" + ChatColor.DARK_RED + " [count]");
					break;
				}
				manager.removeBooster(pl, boosterName, count);
				break;
			case "queue":
				Player activationPlayer = Bukkit.getPlayer(args[1]);
				if (activationPlayer == null) {
					p.sendMessage(ChatColor.RED + " Usage: /booster queue" + ChatColor.RED + " [player]" + ChatColor.RED + " [name]");
					break;
				}
				if (args.length != 3) {
					p.sendMessage(ChatColor.RED + " Usage: /booster queue [player] " + ChatColor.RED + "[name]");
					break;
				}
				String bName = args[2];
				manager.queueBooster(manager.getBoosterForPlayer(activationPlayer, bName), activationPlayer, bName);
				break;
			case "give":
				if (args.length != 4) {
					break;
				}
				Player player = Bukkit.getPlayer(args[1]);
				if (player == null) {
					p.sendMessage(ChatColor.RED + " Usage: /booster give" + ChatColor.RED + " [player] " + ChatColor.RED + "[name] [amount]");
					break;
				}
				String boosterAlias = args[2];
				int amount = 0;
				try {
					amount = Integer.parseInt(args[3]);
				} catch (NumberFormatException exception) {
					p.sendMessage(ChatColor.RED + " Usage: /booster give [player] [name]" + ChatColor.RED + " [amount]");
					break;
				}
				manager.giveBooster(player, boosterAlias, amount).getDurationInMinutes();
				break;
			case "stats":
				sender.sendMessage(NetworkStatistic.statsList());
				break;
			case "list":
				manager.displayBoosterQueue(p);
				break;
			case "view":
				if (args.length != 2) {
					p.sendMessage(ChatColor.RED + " Usage: /booster view " + ChatColor.RED + " [player]");
					break;
				}

				if (Bukkit.getPlayer(args[1]) == null) {
					p.sendMessage(ChatColor.RED + " Error: that player isn't online.");
					break;
				}
				manager.viewBoostersOfPlayer(Bukkit.getPlayer(args[1]), p);
				break;
			case "help":
				sendHelpMessage(p);
				break;
			default:
				break;
		}

		return true;
	}

	public void sendHelpMessage(Player p) {
		HashMap<String, String> commandHelp = new LinkedHashMap<>();

		ChatColor RED = ChatColor.RED;
		ChatColor WHITE = ChatColor.WHITE;
		ChatColor BOLD = ChatColor.BOLD;

		String title = RED + "────" + WHITE + "──────[" + RED + BOLD + "Booster Help" + WHITE + "]──────" + RED + "────";
		String prefix = ChatColor.RED + "/bos" + ChatColor.WHITE + " ";

		String create = "create" + WHITE + " ";
		String createSuffix = "name statistic scope type";
		String remove = "remove" + WHITE + " ";
		String removeSuffix = "player name amount";
		String queue = "queue" + WHITE + " ";
		String queueSuffix = "player name";
		String give = "give" + WHITE + " ";
		String giveSuffix = "player name amount";
		String list = "list" + WHITE + " ";
		String view = "view" + WHITE + " ";
		String viewSuffix = "player";
		String stats = "stats" + WHITE + " ";
		String help = "help" + WHITE + " ";

		commandHelp.put(title, "");
		commandHelp.put(prefix + create, createSuffix);
		commandHelp.put(prefix + remove, removeSuffix);
		commandHelp.put(prefix + queue, queueSuffix);
		commandHelp.put(prefix + give, giveSuffix);
		commandHelp.put(prefix + list, "");
		commandHelp.put(prefix + view, viewSuffix);
		commandHelp.put(prefix + stats, "");
		commandHelp.put(prefix + help, "");


		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, String> stringedCommands : commandHelp.entrySet()) {
			for (String s : stringedCommands.getValue().split(" ")) {
				if (s.isEmpty() || s.equals(commandHelp.get(0))) continue;
				builder.append("[" + RED + s + WHITE + "]").append(" ");
			}
			p.sendMessage(stringedCommands.getKey() + builder.append(" "));
			builder = new StringBuilder();

		}

	}
}
