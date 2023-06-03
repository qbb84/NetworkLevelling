package rankednetwork.NetworkLevelling.Events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EventBase {
	/**
	 * Returns the UUID of the player related to the event.
	 *
	 * @return a UUID representing the player.
	 */
	UUID getPlayerUUID();

	/**
	 * Returns the time at which the event was created.
	 *
	 * @return a LocalDateTime representing the creation time of the event.
	 */
	LocalDateTime getCreationTime();

}
