package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

public class BoosterExpirationEvent extends BoosterEvent {

	private static final HandlerList handlers = new HandlerList();

	public BoosterExpirationEvent(@NotNull UUID uuid, @NotNull String boosterName, @NotNull Booster<?> booster) {
		super(uuid, boosterName, booster);
	}

	public BoosterExpirationEvent(@NotNull UUID playerUUID, @NotNull String boosterName, @NotNull Double boosterPower, @NotNull BoosterScope scope, @NotNull BoosterType type, @NotNull NetworkStatistic statistic, @NotNull Booster.Status status) {
		super(playerUUID, boosterName, boosterPower, scope, type, statistic, status);

	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

}
