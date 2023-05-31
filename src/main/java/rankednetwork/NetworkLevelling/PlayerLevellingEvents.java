package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import rankednetwork.NetworkLevelling.Config.Config;

public class PlayerLevellingEvents implements Listener{


		@EventHandler
		public void onJoin(PlayerJoinEvent event) {
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


			playerLevels.save();



		}
}
