package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

public class BoosterQueueEvent extends BoosterEvent {

	private static final HandlerList handlers = new HandlerList();

	public BoosterQueueEvent(@NotNull UUID uuid, @NotNull String boosterName, @NotNull Booster<?> booster) {
		super(uuid, boosterName, booster);
	}

	public BoosterQueueEvent(UUID playerUUID, String boosterName, Double boostAmount, BoosterScope scope, BoosterType boosterType, NetworkStatistic statistic, Booster.Status status) {
		super(playerUUID, boosterName, boostAmount, scope, boosterType, statistic, status);
	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}



