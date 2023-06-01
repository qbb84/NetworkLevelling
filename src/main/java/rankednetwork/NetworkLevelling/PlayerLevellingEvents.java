package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActiveEvent;
import rankednetwork.NetworkLevelling.Config.Config;

import java.util.HashMap;
import java.util.Map;

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

		p.setLevel(PlayerLevelManager.getInstance().getCurrentLevel(p));


		playerLevels.save();

	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (event.getBlock().getType().equals(Material.OAK_LOG) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			Player p = event.getPlayer();
			PlayerExperience experience = new PlayerExperience();
			ExperienceChangeListener listener = PlayerLevelManager.getInstance();
			experience.addExperienceChangeListener(listener);


			experience.changeValue(p, 500);
			p.sendMessage(String.valueOf(experience.getValue(p)));
			p.sendMessage(String.valueOf(experience.getPercentageToNextLevel(p)));
		}
	}

	@EventHandler
	public void onBoosterActivation(BoosterActiveEvent event) {
		Player p = event.getPlayer();
		PlayerExperience experience = new PlayerExperience();
		ExperienceChangeListener listener = PlayerLevelManager.getInstance();
		experience.addExperienceChangeListener(listener);

		experience.changeValue(p, 500);
		p.sendMessage(String.valueOf(experience.getValue(p)));
		p.sendMessage(String.valueOf(experience.getPercentageToNextLevel(p)));


	}


}
