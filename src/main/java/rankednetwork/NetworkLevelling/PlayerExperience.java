package rankednetwork.NetworkLevelling;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Pair;
import rankednetwork.NetworkLevelling.Events.ExperienceChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	public void addExperienceChangeListener(ExperienceChangeListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public int getValue(Player player) {
		Pair<ConfigurationSection, Boolean> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.second) {
			return 0;
		}
		ConfigurationSection experience = tryFindPlayer.first;
		this.value = experience.getInt("experience");
		return value;
	}

	@Override
	public void setValue(Player player, int value) {
		Pair<ConfigurationSection, Boolean> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.second) return;
		tryFindPlayer.first.set("experience", value);
		levelManager.getPlayerLevelsConf().save();
		this.value = value;
		notifyExperienceChange(player.getUniqueId(), value);

		int experience = tryFindPlayer.first.getInt("experience");
		int level = tryFindPlayer.first.getInt("level");
		EventManager.registerEvents(new ExperienceChangeEvent(player.getUniqueId(), level, experience, value, PlayerLevelManager.getInstance(), ExperienceChangeEvent.MethodType.SET));

		updateExperienceBar(player);
	}

	@Override
	public void changeValue(Player player, int delta) {
		Pair<ConfigurationSection, Boolean> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.second) return;
		tryFindPlayer.first.set("experience", getValue(player) + delta);
		levelManager.getPlayerLevelsConf().save();
		this.value = getValue(player);
		notifyExperienceChange(player.getUniqueId(), getValue(player));

		int experience = tryFindPlayer.first.getInt("experience");
		int level = tryFindPlayer.first.getInt("level");

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

	@Contract("_ -> new")
	private @NotNull Pair<ConfigurationSection, Boolean> isInConfig(Player name) {
		ConfigurationSection playerSection = levelManager.getPlayerLevelsConf().getConfig().getConfigurationSection(name.getUniqueId().toString());
		Integer exp = playerSection.getInt("experience");
		if (playerSection == null) return new Pair<>(playerSection, false);
		if (exp == null) return new Pair<>(playerSection, false);
		return new Pair<>(playerSection, true);
	}

	public double getPercentageToNextLevel(Player player) {
		int currentExperience = getValue(player);
		int currentLevel = levelManager.getCurrentLevel(player.getUniqueId());
		int experienceForCurrentLevel = levelManager.getExperienceForLevel(currentLevel);
		int experienceForNextLevel = levelManager.getExperienceForLevel(currentLevel + 1);

		int experienceInCurrentLevel = currentExperience - experienceForCurrentLevel;
		int totalExperienceInLevel = experienceForNextLevel - experienceForCurrentLevel;

		return (double) experienceInCurrentLevel / totalExperienceInLevel * 100;
	}

	private void notifyExperienceChange(UUID playerUUID, int newValue) {
		for (ExperienceChangeListener listener : listeners) {
			listener.onExperienceChange(playerUUID, newValue);
		}
		PlayerLevelManager.getInstance().onExperienceChange(playerUUID, newValue);
	}

	public void updateExperienceBar(Player player) {
		double percentageToNextLevel = getPercentageToNextLevel(player);
		float progress = (float) (percentageToNextLevel / 100.0);
		player.setExp(progress);
	}

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
