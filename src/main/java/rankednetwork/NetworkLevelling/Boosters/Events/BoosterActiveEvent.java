package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public class BoosterActiveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final String boosterName;
	private final double boostAmount;
	private final BoosterScope scope;
	private final BoosterType boosterType;
	private final NetworkStatistic statistic;

	public BoosterActiveEvent(Player player, String boosterName, double boostAmount, BoosterScope scope, BoosterType boosterType, NetworkStatistic statistic) {
		this.player = player;
		this.boosterName = boosterName;
		this.boostAmount = boostAmount;
		this.scope = scope;
		this.boosterType = boosterType;
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

	public double getBoostAmount() {
		return boostAmount;
	}

	public BoosterScope getScope() {
		return scope;
	}

	public BoosterType getBoosterType() {
		return boosterType;
	}

	public NetworkStatistic getStatistic() {
		return statistic;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}

