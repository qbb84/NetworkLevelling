package rankednetwork.NetworkLevelling;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Pair;

import java.util.ArrayList;
import java.util.List;

@BoosterMetadata(name = "XP")
public class PlayerExperience extends NetworkStatistic {


	private final Player player;
	private final List<ExperienceChangeListener> listeners = new ArrayList<>();
	PlayerLevelManager levelManager;

	public PlayerExperience() {
		this(null);
	}


	public PlayerExperience(Player player) {
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
			player.sendMessage("GET VALUE");
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
		notifyExperienceChange(player, value);

		updateExperienceBar(player);
	}

	@Override
	public void changeValue(Player player, int delta) {
		Pair<ConfigurationSection, Boolean> tryFindPlayer = isInConfig(player);
		if (!tryFindPlayer.second) return;
		tryFindPlayer.first.set("experience", getValue(player) + delta);
		levelManager.getPlayerLevelsConf().save();
		this.value = getValue(player);
		notifyExperienceChange(player, getValue(player));

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
		int currentLevel = levelManager.getCurrentLevel(player);
		int experienceForCurrentLevel = levelManager.getExperienceForLevel(currentLevel);
		int experienceForNextLevel = levelManager.getExperienceForLevel(currentLevel + 1);

		int experienceInCurrentLevel = currentExperience - experienceForCurrentLevel;
		int totalExperienceInLevel = experienceForNextLevel - experienceForCurrentLevel;

		return (double) experienceInCurrentLevel / totalExperienceInLevel * 100;
	}

	private void notifyExperienceChange(Player player, int newValue) {
		for (ExperienceChangeListener listener : listeners) {
			listener.onExperienceChange(player, newValue);
		}
		PlayerLevelManager.getInstance().onExperienceChange(player, newValue);
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
