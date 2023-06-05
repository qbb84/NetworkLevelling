package rankednetwork.NetworkLevelling;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Events.ExperienceChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PlayerExperience represents the experience points for a player in the game.
 * It extends the NetworkStatistic class and provides the specific implementation
 * for the experience points.
 * This class also provides mechanisms to register and notify listeners for any changes in the experience points.
 * It interacts with the PlayerLevelManager to perform various operations related to player levels.
 *
 * @author Rewind
 * @see NetworkStatistic
 * @see ExperienceChangeListener
 * @see PlayerLevelManager
 * @see BoosterMetadata
 */
@BoosterMetadata(name = "XP")
public class PlayerExperience extends NetworkStatistic {

	private final UUID player;
	private final List<ExperienceChangeListener> listeners = new ArrayList<>();
	PlayerLevelManager levelManager;


	public PlayerExperience() {
		this(null);
	}


	public PlayerExperience(UUID player) {
		super(player, "PlayerExperience", "XP");
		this.player = player;
		this.levelManager = PlayerLevelManager.getInstance();
	}

	/**
	 * Add a listener for changes in the player's experience.
	 *
	 * @param listener the listener to add
	 */
	public void addExperienceChangeListener(ExperienceChangeListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Gets the current value of the player's experience.
	 *
	 * @param player the player whose experience to fetch
	 * @return the player's current experience value
	 */
	@Override
	public int getValue(Player player) {
		Optional<ConfigurationSection> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.isPresent()) {
			return 0;
		}
		ConfigurationSection experience = tryFindPlayer.get();
		this.value = experience.getInt("experience");
		return value;
	}

	/**
	 * Sets the player's experience to the specified value.
	 *
	 * @param player the player whose experience to set
	 * @param value  the new value of the player's experience
	 */
	@Override
	public void setValue(Player player, int value) {
		Optional<ConfigurationSection> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.isPresent()) return;
		tryFindPlayer.get().set("experience", value);
		levelManager.getPlayerLevelsConf().save();
		this.value = value;
		notifyExperienceChange(player.getUniqueId(), value);

		int experience = tryFindPlayer.get().getInt("experience");
		int level = tryFindPlayer.get().getInt("level");
		EventManager.registerEvents(new ExperienceChangeEvent(player.getUniqueId(), level, experience, value, PlayerLevelManager.getInstance(), ExperienceChangeEvent.MethodType.SET));

		updateExperienceBar(player);
	}

	/**
	 * Changes the player's experience by the specified delta.
	 *
	 * @param player the player whose experience to change
	 * @param delta  the amount to change the player's experience by
	 */
	@Override
	public void changeValue(Player player, int delta) {
		Optional<ConfigurationSection> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.isPresent()) return;
		tryFindPlayer.get().set("experience", getValue(player) + delta);
		levelManager.getPlayerLevelsConf().save();
		this.value = getValue(player);
		notifyExperienceChange(player.getUniqueId(), getValue(player));

		int experience = tryFindPlayer.get().getInt("experience");
		int level = tryFindPlayer.get().getInt("level");

		EventManager.registerEvents(new ExperienceChangeEvent(player.getUniqueId(), level, experience, delta, PlayerLevelManager.getInstance(), ExperienceChangeEvent.MethodType.CHANGED));

		updateExperienceBar(player);
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String setName(String name) {
		return this.name = name;
	}

	@Override
	public String setType(String type) {
		return this.type = type;
	}

	/**
	 * Fetches the player's configuration section if it exists and the player has experience points.
	 *
	 * @param name the player to fetch the configuration section for
	 * @return an Optional containing the player's ConfigurationSection if it exists and the player has experience, otherwise an empty Optional
	 */
	@Contract("_ -> new")
	private @NotNull Optional<ConfigurationSection> isInConfig(Player name) {
		ConfigurationSection playerSection = levelManager.getPlayerLevelsConf().getConfig().getConfigurationSection(name.getUniqueId().toString());

		if (playerSection == null) {
			return Optional.empty();
		}
		Integer exp = playerSection.getInt("experience");

		if (exp == -1) {
			return Optional.empty();
		}
		return Optional.of(playerSection);
	}

	/**
	 * Gets the percentage of the way the player is towards the next level.
	 *
	 * @param player the player to calculate the percentage for
	 * @return the percentage towards the next level
	 */
	public double getPercentageToNextLevel(Player player) {
		int currentExperience = getValue(player);
		int currentLevel = levelManager.getCurrentLevel(player.getUniqueId());
		int experienceForCurrentLevel = levelManager.getExperienceForLevel(currentLevel);
		int experienceForNextLevel = levelManager.getExperienceForLevel(currentLevel + 1);

		int experienceInCurrentLevel = currentExperience - experienceForCurrentLevel;
		int totalExperienceInLevel = experienceForNextLevel - experienceForCurrentLevel;

		return (double) experienceInCurrentLevel / totalExperienceInLevel * 100;
	}

	/**
	 * Notify all registered listeners of a change in the player's experience.
	 *
	 * @param playerUUID the UUID of the player whose experience changed
	 * @param newValue   the new value of the player's experience
	 */
	private void notifyExperienceChange(UUID playerUUID, int newValue) {
		listeners.forEach(listener -> listener.onExperienceChange(playerUUID, newValue));
		//PlayerLevelManager.getInstance().onExperienceChange(playerUUID, newValue);
	}

	/**
	 * Update the player's experience bar to reflect their current experience and level.
	 *
	 * @param player the player whose experience bar to update
	 */
	public void updateExperienceBar(Player player) {
		double percentageToNextLevel = getPercentageToNextLevel(player);
		float progress = (float) (percentageToNextLevel / 100.0);
		player.setExp(progress);
	}

	/**
	 * Generates a string representation of the PlayerExperience object.
	 *
	 * @return a string representation of the PlayerExperience object
	 */
	@Override
	public String toString() {
		return "PlayerExperience{" +
				"player=" + player +
				", listeners=" + listeners +
				", levelManager=" + levelManager +
				", value=" + value +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", player=" + player +
				'}';
	}

}
