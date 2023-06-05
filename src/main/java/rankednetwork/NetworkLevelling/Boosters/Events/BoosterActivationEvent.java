package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

/**
 * This class represents a booster activation event in the Network Levelling system.
 * This event is fired when a booster is activated.
 */
public class BoosterActivationEvent extends BoosterEvent {

	private static final HandlerList HANDLERS = new HandlerList();

	/**
	 * Constructs a BoosterActivationEvent with a specified UUID, booster name, and booster.
	 *
	 * @param uuid        The UUID of the player who activated the booster.
	 * @param boosterName The name of the booster.
	 * @param booster     The booster that was activated.
	 */
	public BoosterActivationEvent(@NotNull UUID uuid, @NotNull String boosterName, @NotNull Booster<?> booster) {
		super(uuid, boosterName, booster);
	}

	/**
	 * Constructs a BoosterActivationEvent with detailed booster information.
	 *
	 * @param playerUUID   The UUID of the player who activated the booster.
	 * @param boosterName  The name of the booster.
	 * @param boosterPower The power of the booster.
	 * @param scope        The scope of the booster.
	 * @param type         The type of the booster.
	 * @param statistic    The statistic the booster affects.
	 * @param status       The status of the booster.
	 */
	public BoosterActivationEvent(UUID playerUUID, String boosterName, double boosterPower, BoosterScope scope, BoosterType type, NetworkStatistic statistic, Booster.Status status) {
		super(playerUUID, boosterName, boosterPower, scope, type, statistic, status);
	}

	public static @NotNull HandlerList getHandlerList() {
		return HANDLERS;
	}


	public @NotNull HandlerList getHandlers() {
		return HANDLERS;
	}
}