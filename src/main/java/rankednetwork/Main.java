package rankednetwork;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Commands.BoosterCommand;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.Config.DiscordConfigDefaults;
import rankednetwork.NetworkLevelling.PlayerExperience;
import rankednetwork.NetworkLevelling.PlayerLevelManager;
import rankednetwork.NetworkLevelling.PlayerLevellingEvents;

public final class Main extends JavaPlugin {

    private static Main main;


    @Override
    public void onEnable() {
        main = this;
        new DiscordConfigDefaults();
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

    public static Main getMain() {
        return main;
    }

    public void defaultPlayerLevelConfig(){
        Config playerLevels = PlayerLevelManager.getInstance().getPlayerLevelsConf();

    }
}
