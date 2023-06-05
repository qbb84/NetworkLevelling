package rankednetwork.NetworkLevelling;

import java.util.UUID;

/**
 * This interface defines a method that responds to experience changes.
 */
public interface ExperienceChangeListener {

	/**
	 * Called when a player's experience changes.
	 *
	 * @param playerUUID the UUID of the player whose experience changed
	 * @param newValue   the new experience value
	 */
	void onExperienceChange(UUID playerUUID, int newValue);
}