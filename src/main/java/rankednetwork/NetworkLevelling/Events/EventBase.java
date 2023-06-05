package rankednetwork.NetworkLevelling.Events;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This interface defines the common methods that should be implemented by any class that represents an event
 * in the Network Levelling system. An event is characterized by its association with a player, represented
 * by a UUID, and the time of its creation.
 */
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
