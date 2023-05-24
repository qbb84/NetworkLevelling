package me.rewind.server;

import me.rewind.server.NetworkLevelling.Boosters.Booster;
import me.rewind.server.NetworkLevelling.Boosters.BoosterManager;
import me.rewind.server.NetworkLevelling.PlayerExperience;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {



    @Override
    public void onEnable() {
        // Plugin startup logic
        BoosterManager expBooster = new BoosterManager();


        Booster<PlayerExperience> booster = new Booster<>(getServer().getPlayer("Rewind"), null,1, 1);
        expBooster.activateBooster(booster);
        expBooster.sendDiscordMessage("", booster);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
