package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActivationEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActiveEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterExpirationEvent;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.Notifiers.GameNotifier;
import rankednetwork.NetworkLevelling.Notifiers.GlobalBoosterDiscordNotifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PlayerLevellingEvents implements Listener {

	private final HashMap<String, Object> configMap = new HashMap<>();


	@EventHandler
	public void onJoin(@NotNull PlayerJoinEvent event) {
		Player p = event.getPlayer();
		String UUID = p.getUniqueId().toString();
		Config playerLevels = PlayerLevelManager.getInstance().getPlayerLevelsConf();

		ConfigurationSection section = playerLevels.getConfig().getConfigurationSection(UUID);

		if (section == null) {
			section = playerLevels.getConfig().createSection(UUID);
		}
		Player checkUUID = Bukkit.getPlayer(p.getUniqueId());


		if (!section.isSet("name") || !section.get("name").equals(checkUUID.getName())) {
			section.set("name", checkUUID.getName());
		}

		configMap.put("level", 1);
		configMap.put("experience", 0);

		for (Map.Entry<String, Object> obj : configMap.entrySet()) {
			if (!section.isSet(obj.getKey())) {
				section.set(obj.getKey(), obj.getValue());
			}
		}

		PlayerExperience experience = new PlayerExperience();
		experience.updateExperienceBar(p);


		playerLevels.save();

	}

	@EventHandler
	public void onBoosterActive(BoosterActiveEvent event) {

		UUID playerUUID = event.getPlayer().getUniqueId();

		// Obtain the Player object from the server using the UUID
		Player p = Bukkit.getServer().getPlayer(playerUUID);

		// Check if the Player object is null or if the player is not online
		if (p == null || !p.isOnline()) {
			Bukkit.broadcastMessage("Player is not online or could not be found.");
			return;
		}
		//Send messages

		PlayerExperience experience = new PlayerExperience();
		ExperienceChangeListener listener = PlayerLevelManager.getInstance();
		experience.addExperienceChangeListener(listener);

		double boostPercentage = event.getBoosterPower();
		int playerLevel = PlayerLevelManager.getInstance().getCurrentLevel(event.getPlayer());
		double xpPerMinute = 600.0 * playerLevel * boostPercentage;
		int bound = Math.max(playerLevel / 5 * 10, 1);
		int randomXP = new Random().nextInt(bound);
		int xp = (randomXP > 0) ? (int) xpPerMinute + randomXP : (int) xpPerMinute;

		experience.changeValue(p, xp);
		experience.updateExperienceBar(p);
		p.sendMessage(String.valueOf(experience.getValue(p)));
		p.sendMessage(String.valueOf(experience.getPercentageToNextLevel(p)));

	}

	@EventHandler
	public void onBoosterExpire(BoosterExpirationEvent event) {
		new GameNotifier().sendActivatedMessage(event.getBooster());
	}

	@EventHandler
	public void onBoosterActivation(BoosterActivationEvent event) {
		if (event.getScope().equals(BoosterScope.GLOBAL)) {
			Booster<?> booster = BoosterManager.getInstance().getBoosterForPlayer(event.getPlayer(), event.getBoosterName());
			new GlobalBoosterDiscordNotifier().sendGlobalDiscordMessage(booster);
			new GameNotifier().sendActivatedMessage(booster);
		}
	}


}
