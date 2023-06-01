package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public class BoosterExpirationEvent extends BoosterEvent {

	private static final HandlerList handlers = new HandlerList();

	public BoosterExpirationEvent(@NotNull Player player, @NotNull String boosterName, @NotNull Booster<?> booster) {
		super(player, boosterName, booster);
	}

	public BoosterExpirationEvent(@NotNull Player player, @NotNull String boosterName, @NotNull Double boosterPower, @NotNull BoosterScope scope, @NotNull BoosterType type, @NotNull NetworkStatistic statistic, @NotNull Booster.Status status) {
		super(player, boosterName, boosterPower, scope, type, statistic, status);
	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}
}
