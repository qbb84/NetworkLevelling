package rankednetwork.NetworkLevelling.Events;


import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.ExperienceChangeListener;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExperienceChangeEvent extends LevelEvent {

	private static final HandlerList handlers = new HandlerList();
	private final int newExperience;
	private final ExperienceChangeListener source;
	private final Result result;
	private final MethodType methodType;

	public ExperienceChangeEvent(@NotNull UUID playerUUID, int playerLevel, int playerExperience, int newExperience, ExperienceChangeListener source, MethodType methodType) {
		super(playerUUID, playerLevel, playerExperience);
		this.newExperience = newExperience;
		this.source = source;
		this.methodType = methodType;
		this.result = (this.methodType == MethodType.CHANGED) ? Result.INCREASE : (newExperience > playerExperience) ? Result.INCREASE : Result.DECREASE;
	}

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

	public int getNewExperience() {
		return newExperience;
	}

	public ExperienceChangeListener getSource() {
		return source;
	}

	public Result getResult() {
		return result;
	}

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
