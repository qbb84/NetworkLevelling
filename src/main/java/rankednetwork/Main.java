package rankednetwork;

import org.bukkit.plugin.java.JavaPlugin;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Commands.BoosterCommand;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.Config.MainConfigDefaults;
import rankednetwork.NetworkLevelling.CustomerBoosterEventsTest;
import rankednetwork.NetworkLevelling.PlayerLevelManager;

public final class Main extends JavaPlugin {

	private static Main main;

	public static Main getMain() {
		return main;
	}

	//TODO Full project Javadocs
	@Override
	public void onEnable() {
		main = this;
		new MainConfigDefaults();
		getCommand("bos").setExecutor(new BoosterCommand());
		BoosterManager.getInstance().initliazeStatisticsAvailableForBoosting();
		BoosterManager.getInstance().checkBoostersRunnable();
		getServer().getPluginManager().registerEvents(new CustomerBoosterEventsTest(), this);
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
