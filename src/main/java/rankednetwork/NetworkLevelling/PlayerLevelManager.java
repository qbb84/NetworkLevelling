package rankednetwork.NetworkLevelling;


import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.Events.PlayerLevelUpEvent;

import java.util.UUID;

/**
 * The PlayerLevelManager is a singleton class implements ExperienceChangeListener and defines the player level manager which handles
 * the changes in the player's experience and their levels.
 *
 * @author Rewind
 * @version 1.0
 * @since 2023-06-01
 */
public class PlayerLevelManager implements ExperienceChangeListener {

	private static PlayerLevelManager instance = null;

	private final Config playerLevels = new Config(Main.getMain(), "player_levels");

	private PlayerLevelManager() {
	}

	public static PlayerLevelManager getInstance() {
		if (instance == null) {
			instance = new PlayerLevelManager();
		}
		return instance;
	}

	@Override
	public void onExperienceChange(UUID playerUUID, int newExperience) {
		//TODO Fix latest experienced to get the latest !TOTAL
		int currentLevel = getCurrentLevel(playerUUID);
		int newLevel = calculateLevel(newExperience);
		Player player = Bukkit.getPlayer(playerUUID);
		player.sendMessage("current: " + currentLevel + " newLevel: " + newLevel + " experience: " + newExperience);
		if (newLevel != currentLevel && newLevel > 0) {
			setCurrentLevel(playerUUID, newLevel);

			EventManager.registerEvents(new PlayerLevelUpEvent(playerUUID, getCurrentLevel(playerUUID), getExperienceForLevel(newLevel), newExperience, currentLevel, newLevel));

			player.setLevel(PlayerLevelManager.getInstance().getCurrentLevel(playerUUID));


		}
	}

	public int calculateLevel(int experience) {
		int level = 1;
		int xpForNextLevel = 3000;
		while (experience >= xpForNextLevel) {
			experience -= xpForNextLevel;
			level++;
			xpForNextLevel += 3000;
		}
		return level;
	}

	public int getCurrentLevel(UUID playerUUID) {
		ConfigurationSection playerSection = playerLevels.getConfig().getConfigurationSection(playerUUID.toString());
		return playerSection.getInt("level");
	}


	public void setCurrentLevel(UUID playerUUID, int newLevel) {
		ConfigurationSection playerSection = playerLevels.getConfig().getConfigurationSection(playerUUID.toString());
		playerSection.set("level", newLevel);
		playerLevels.save();
	}

	public int getExperienceForLevel(int level) {
		int totalXp = 0;
		for (int i = 1; i < level; i++) {
			totalXp += 3000 + 3000 * (i - 1);
		}
		return totalXp;
	}


	public Config getPlayerLevelsConf() {
		return playerLevels;
	}


}

