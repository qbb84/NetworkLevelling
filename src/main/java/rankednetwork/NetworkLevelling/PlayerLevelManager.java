package rankednetwork.NetworkLevelling;


import org.bukkit.entity.Player;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Config.Config;

public class PlayerLevelManager implements ExperienceChangeListener {

	private static PlayerLevelManager instance = null;

	private Config playerLevels = new Config(Main.getMain(), "player_levels");

	private PlayerLevelManager(){}

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
		if (newLevel != currentLevel) {
			setCurrentLevel(player, newLevel);
			// Trigger any other level-up behaviors here
			player.sendMessage("You have levelled up to " + newLevel);
		}
	}

	private int calculateLevel(int experience) {
		return experience / 3500; // for example, each level requires 1000 XP
	}

	private int getCurrentLevel(Player player) {
		// Fetch the player's current level from the relevant data source
		//TODO Player Level
		return 0;
	}

	private void setCurrentLevel(Player player, int newLevel) {
		// Update the player's level in the relevant data source
	}

	public Config getPlayerLevelsConf() {
		return playerLevels;
	}


}

