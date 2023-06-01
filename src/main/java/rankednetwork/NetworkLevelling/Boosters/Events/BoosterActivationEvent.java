package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public class BoosterActivationEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final Player player;
	private final String boosterName;
	private final double boosterPower;
	private final NetworkStatistic statistic;

	public BoosterActivationEvent(Player player, String boosterName, double boosterPower, BoosterScope scope, BoosterType type, NetworkStatistic statistic) {
		this.player = player;
		this.boosterName = boosterName;
		this.boosterPower = boosterPower;
		this.statistic = statistic;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public String getBoosterName() {
		return boosterName;
	}

	public double getBoosterPower() {
		return boosterPower;
	}

	public NetworkStatistic getStatistic() {
		return statistic;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}