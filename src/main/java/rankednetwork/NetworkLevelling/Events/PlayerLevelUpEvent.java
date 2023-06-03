package rankednetwork.NetworkLevelling.Events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.PlayerLevelManager;

import java.util.UUID;

public class PlayerLevelUpEvent extends LevelEvent {

	public static HandlerList handlers = new HandlerList();
	private final int oldLevel;
	private final int newLevel;
	private final int lastExperienceGained;


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

	public int getOldLevel() {
		return oldLevel;
	}

	public int getNewLevel() {
		return newLevel;
	}

	public int getLastExperienceGained() {
		return lastExperienceGained;
	}

	public int getExperiencedGained() {
		return PlayerLevelManager.getInstance().getExperienceForLevel(getNewLevel()) - PlayerLevelManager.getInstance().getExperienceForLevel(oldLevel);
	}
}
