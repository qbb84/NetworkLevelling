package rankednetwork.NetworkLevelling.Commands;

import org.apache.commons.lang3.ObjectUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.awt.*;

public class BoosterCommand implements CommandExecutor {


	BoosterManager manager = BoosterManager.getInstance();

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

		if(!s.equals("booster")) {return false;}
		if(!(args.length > 0)) {return false;}
		if(!(sender instanceof Player)) {return false;}
		Player p = (Player) sender;

		switch (args[0].toLowerCase()) {
			case "create":
				if(args.length != 5) {return false;}
				String boosterName = args[1];
				String statistic = (NetworkStatistic.getAvailableStats().toString().toLowerCase().contains(args[2].toLowerCase())) ? args[2] : null;
				double multiplier;
				long duration;
				try{
					multiplier = Double.parseDouble(args[3]);
					duration = Long.parseLong(args[4]);
				}catch (NumberFormatException exception){
					return false;
				}
				if(statistic == null) return false;

				manager.create(boosterName, statistic, multiplier, duration, p);

				break;
			case "remove":
				//remove removes a booster //remove name removes it from a player
				break;
			case "activate":
				break;
			case "give":
				break;
			case "stats":
				sender.sendMessage(NetworkStatistic.statsList());
				break;
			case "view":
				//views boosters of a player
				break;
			default:
				sender.sendMessage("create/remove/activate/give");
				break;
		}

		return true;
	}
}
