package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterEvent;

public class EventManager {

	public EventManager() {
	}

	@SafeVarargs
	public static <T extends BoosterEvent> void registerEvents(T... eventClass) {
		for (T eventInstance : eventClass) {
			Bukkit.getPluginManager().callEvent(eventInstance);
		}
	}
}
