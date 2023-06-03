package rankednetwork;

import org.bukkit.plugin.java.JavaPlugin;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Commands.BoosterCommand;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.Config.MainConfigDefaults;
import rankednetwork.NetworkLevelling.PlayerLevelManager;
import rankednetwork.NetworkLevelling.PlayerLevellingEvents;

public final class Main extends JavaPlugin {

	private static Main main;

	public static Main getMain() {
		return main;
	}

	@Override
	public void onEnable() {
		main = this;
		new MainConfigDefaults();
		getCommand("booster").setExecutor(new BoosterCommand());
		BoosterManager.getInstance().initliazeStatisticsAvailableForBoosting();
		BoosterManager.getInstance().checkBoostersRunnable();
		getServer().getPluginManager().registerEvents(new PlayerLevellingEvents(), this);
		BoosterManager.getInstance().loadBoosterQueue();

	}

	@Override
	public void onDisable() {
		BoosterManager.getInstance().saveBoosterQueues();

	}

	public void defaultPlayerLevelConfig() {
		Config playerLevels = PlayerLevelManager.getInstance().getPlayerLevelsConf();

	}
}
