package rankednetwork.NetworkLevelling.Notifiers;

import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

/**
 * This interface defines methods for sending notifications about boosters.
 */
public interface Notifier {

	/**
	 * Sends a notification about a specific booster.
	 *
	 * @param booster the booster to send a notification about
	 */
	void sendNotification(Booster<? extends NetworkStatistic> booster);

	/**
	 * Sends a notification to a player with a specific UUID.
	 *
	 * @param uuid the UUID of the player to send a notification to
	 */
	void sendNotification(UUID uuid);
}
