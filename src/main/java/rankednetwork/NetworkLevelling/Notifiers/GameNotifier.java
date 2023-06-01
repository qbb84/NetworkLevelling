package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public class GameNotifier implements Notifier {

	@Override
	public void sendActivatedMessage(Booster<? extends NetworkStatistic> booster) {
		Player p = booster.getPlayer();
		switch (booster.getStatus()) {
			case ACTIVE:
				p.sendMessage(ChatColor.YELLOW + "Your booster " + booster.getBoosterName() + " is now being used!");
				break;
			case INQUEUE:
				p.sendMessage(ChatColor.YELLOW + "Your personal booster " + booster.getBoosterName() + " is now being queued!");
				break;
			case EXPIRED:
				p.sendMessage(ChatColor.GOLD + " Your " + booster.getBoosterName() + " booster has expired!");
				break;
			case INACTIVE:
				p.sendMessage("well well well");
				break;
			default:
				break;
		}
	}
}
