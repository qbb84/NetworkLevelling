package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import rankednetwork.NetworkLevelling.Events.EventBase;

public class EventManager {

	public EventManager() {
	}

	@SafeVarargs
	public static <T extends EventBase> void registerEvents(T... eventClass) {
		for (T eventInstance : eventClass) {
			Bukkit.getPluginManager().callEvent((Event) eventInstance);
		}
	}
}
