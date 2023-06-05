package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

/**
 * This class represents a booster queue event in the Network Levelling system.
 * This event is fired when a booster is queued.
 */
public class BoosterQueueEvent extends BoosterEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Constructs a BoosterQueueEvent with a specified UUID, booster name, and booster.
	 *
	 * @param uuid        The UUID of the player who queued the booster.
	 * @param boosterName The name of the booster.
	 * @param booster     The booster that was queued.
	 */
	public BoosterQueueEvent(@NotNull UUID uuid, @NotNull String boosterName, @NotNull Booster<?> booster) {
		super(uuid, boosterName, booster);
	}

	/**
	 * Constructs a BoosterQueueEvent with detailed booster information.
	 *
	 * @param playerUUID  The UUID of the player who queued the booster.
	 * @param boosterName The name of the booster.
	 * @param boostAmount The amount of boost the booster provides.
	 * @param scope       The scope of the booster.
	 * @param boosterType The type of the booster.
	 * @param statistic   The statistic the booster affects.
	 * @param status      The status of the booster.
	 */
	public BoosterQueueEvent(UUID playerUUID, String boosterName, Double boostAmount, BoosterScope scope, BoosterType boosterType, NetworkStatistic statistic, Booster.Status status) {
		super(playerUUID, boosterName, boostAmount, scope, boosterType, statistic, status);
	}

	public static @NotNull HandlerList getHandlerList() {
		return HANDLERS;
	}

	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}



