package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.Config.MainConfigDefaults;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import rankednetwork.NetworkLevelling.PlayerLevelManager;

import java.util.HashMap;
import java.util.UUID;

public class GameNotifier implements Notifier {

	@Override
	public void sendNotification(Booster<? extends NetworkStatistic> booster) {
		UUID playerUUID = booster.getPlayerUUID();

		// Obtain the Player object from the server using the UUID
		Player p = Bukkit.getServer().getPlayer(playerUUID);

		// Check if the Player object is null or if the player is not online
		if (p == null || !p.isOnline()) {
			Bukkit.broadcastMessage("Player is not online or could not be found.");
			return;
		}
		HashMap<String, Object> stringMutation = new HashMap<>();
		stringMutation.put("{b_name}", booster.getBoosterName());
		stringMutation.put("{p}", booster.getOfflinePlayer().getName());
		if (booster.getBoosterType().getBoosterTypeName().equalsIgnoreCase(BoosterType.CUSTOM.getBoosterTypeName())) {
			stringMutation.put("{b_amount}", booster.getBoostAmount() + "%");
			stringMutation.put("{b_duration}", booster.getDurationInMinutes());
			//TODO Create a map that stores the XP a player gets whilst it's active in the booster class
			//TODO Or just get the current time and XP increase
		} else {
			stringMutation.put("{b_amount}", booster.getBoosterType().getBoostIncreasePercentage() + "%");
			stringMutation.put("{b_duration}", booster.getDurationInMinutes());
		}
		stringMutation.put("{b_type}", booster.getStatistic().getType());

		//Send messages
		switch (booster.getStatus()) {
			case ACTIVE:
				p.sendMessage(MainConfigDefaults.translateString("boosters.active_prompt", stringMutation));
				break;
			case INQUEUE:
				p.sendMessage(MainConfigDefaults.translateString("boosters.queue_prompt", stringMutation));
				break;
			case EXPIRED:
				p.sendMessage(MainConfigDefaults.translateString("boosters.expired_prompt", stringMutation));
				break;
			default:
				break;
		}
	}

	@Override
	public void sendNotification(UUID uuid) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
		if (!player.isOnline()) {
			return;
		}
		HashMap<String, Object> stringMutation = new HashMap<>();
		stringMutation.put("{p}", player.getName());
		stringMutation.put("{level}", PlayerLevelManager.getInstance().getCurrentLevel(player.getUniqueId()));

		((Player) player).sendMessage(MainConfigDefaults.translateString("levelling.level_up_prompt", stringMutation));
	}
}
