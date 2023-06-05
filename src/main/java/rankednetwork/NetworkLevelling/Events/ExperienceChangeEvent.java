package rankednetwork.NetworkLevelling.Events;


import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.ExperienceChangeListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an event when a player's experience changes.
 * This event contains information about the change, including the source of the change
 * and whether the experience was increased or decreased.
 */
public class ExperienceChangeEvent extends LevelEvent {

	private static final HandlerList handlers = new HandlerList();
	private final int newExperience;
	private final ExperienceChangeListener source;
	private final Result result;
	private final MethodType methodType;

	/**
	 * Creates a new ExperienceChangeEvent with the given details.
	 *
	 * @param playerUUID       the UUID of the player whose experience changed
	 * @param playerLevel      the level of the player
	 * @param playerExperience the original experience of the player
	 * @param newExperience    the new experience of the player
	 * @param source           the source of the change
	 * @param methodType       the method that caused the change
	 */
	public ExperienceChangeEvent(@NotNull UUID playerUUID, int playerLevel, int playerExperience, int newExperience, ExperienceChangeListener source, MethodType methodType) {
		super(playerUUID, playerLevel, playerExperience);
		this.newExperience = newExperience;
		this.source = source;
		this.methodType = methodType;
		this.result = (this.methodType == MethodType.CHANGED) ? Result.INCREASE : (newExperience > playerExperience) ? Result.INCREASE : Result.DECREASE;
	}

	/**
	 * Creates a new ExperienceChangeEvent with the given details.
	 *
	 * @param playerUUID       the UUID of the player whose experience changed
	 * @param playerLevel      the level of the player
	 * @param playerExperience the original experience of the player
	 * @param newExperience    the new experience of the player
	 * @param source           the source of the change
	 * @param result           the result of the change (increased or decreased)
	 * @param methodType       the method that caused the change
	 */
	public ExperienceChangeEvent(@NotNull UUID playerUUID, int playerLevel, int playerExperience, int newExperience, ExperienceChangeListener source, Result result, MethodType methodType) {
		super(playerUUID, playerLevel, playerExperience);
		this.newExperience = newExperience;
		this.source = source;
		this.result = result;
		this.methodType = methodType;
	}

	public static @NotNull HandlerList getHandlerList() {
		return handlers;
	}

	public @NotNull HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * Gets the new experience of the player after the change.
	 *
	 * @return the new experience of the player
	 */
	public int getNewExperience() {
		return newExperience;
	}

	public ExperienceChangeListener getSource() {
		return source;
	}

	/**
	 * Gets the result of the change.
	 *
	 * @return the result of the change, either increased or decreased
	 */
	public Result getResult() {
		return result;
	}

	/**
	 * Gets the method that caused the change.
	 *
	 * @return the method that caused the change
	 */
	public MethodType getMethodType() {
		return methodType;
	}

	@Override
	public LocalDateTime getCreationTime() {
		return null;
	}

	public enum Result {
		INCREASE,
		DECREASE

	}

	public enum MethodType {
		SET,
		CHANGED
	}
}
