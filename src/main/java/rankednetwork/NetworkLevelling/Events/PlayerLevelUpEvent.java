package rankednetwork.NetworkLevelling.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.PlayerLevelManager;

import java.util.UUID;

/**
 * Represents an event where a player's level has increased.
 */
public class PlayerLevelUpEvent extends LevelEvent {

	public static HandlerList handlers = new HandlerList();
	private final int oldLevel;
	private final int newLevel;
	private final int lastExperienceGained;

	/**
	 * Creates a new PlayerLevelUpEvent with the given details.
	 *
	 * @param playerUUID           the UUID of the player whose level increased
	 * @param playerLevel          the new level of the player
	 * @param playerExperience     the new experience of the player
	 * @param lastExperienceGained the last amount of experience that was gained
	 * @param oldLevel             the old level of the player
	 * @param newLevel             the new level of the player
	 */
	public PlayerLevelUpEvent(@NotNull UUID playerUUID, int playerLevel, int playerExperience, int lastExperienceGained, int oldLevel, int newLevel) {
		super(playerUUID, playerLevel, playerExperience);
		this.lastExperienceGained = lastExperienceGained;
		this.oldLevel = oldLevel;
		this.newLevel = newLevel;
	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the old level of the player before the level up event.
	 *
	 * @return the old level of the player
	 */
	public int getOldLevel() {
		return oldLevel;
	}

	/**
	 * Gets the new level of the player after the level up event.
	 *
	 * @return the new level of the player
	 */
	public int getNewLevel() {
		return newLevel;
	}


	public int getLastExperienceGained() {
		return lastExperienceGained;
	}

	/**
	 * Gets the total amount of experience gained by the player during the level up event.
	 *
	 * @return the total amount of experience gained
	 */
	public int getExperiencedGained() {
		return PlayerLevelManager.getInstance().getExperienceForLevel(getNewLevel()) - PlayerLevelManager.getInstance().getExperienceForLevel(oldLevel);
	}
}
