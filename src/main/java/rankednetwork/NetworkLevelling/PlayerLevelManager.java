package rankednetwork.NetworkLevelling;


import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Config.Config;

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
	public void onExperienceChange(Player player, int newExperience) {
		int currentLevel = getCurrentLevel(player);
		int newLevel = calculateLevel(newExperience);
		player.sendMessage("current: " + currentLevel + " newLevel: " + newLevel + " experience: " + newExperience);
		if (newLevel != currentLevel && newLevel > 0) {
			setCurrentLevel(player, newLevel);
			// Trigger any other level-up behaviors here
			player.sendMessage("You have levelled up to " + newLevel);
			player.setLevel(PlayerLevelManager.getInstance().getCurrentLevel(player));
		}
	}

	public int calculateLevel(int experience) {
		int level = 1;
		int xpForNextLevel = 3500;
		while (experience >= xpForNextLevel) {
			experience -= xpForNextLevel;
			level++;
			xpForNextLevel += 3500;
		}
		return level;
	}

	public int getCurrentLevel(Player player) {
		ConfigurationSection playerSection = playerLevels.getConfig().getConfigurationSection(player.getUniqueId().toString());
		return playerSection.getInt("level");
	}


	public void setCurrentLevel(Player player, int newLevel) {
		ConfigurationSection playerSection = playerLevels.getConfig().getConfigurationSection(player.getUniqueId().toString());
		playerSection.set("level", newLevel);
		playerLevels.save();
	}

	public int getExperienceForLevel(int level) {
		int totalXp = 0;
		for (int i = 1; i < level; i++) {
			totalXp += 3500 + 3500 * (i - 1);
		}
		return totalXp;
	}


	public Config getPlayerLevelsConf() {
		return playerLevels;
	}


}

