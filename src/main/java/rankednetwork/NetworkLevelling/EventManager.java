package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import rankednetwork.NetworkLevelling.Events.EventBase;

/**
 * This class manages the registration of events.
 */
public class EventManager {

	public EventManager() {
	}

	/**
	 * Registers a list of events.
	 *
	 * @param eventClass list of events to register
	 * @param <T>        event type that extends EventBase
	 */
	@SafeVarargs
	public static <T extends EventBase> void registerEvents(T... eventClass) {
		for (T eventInstance : eventClass) {
			Bukkit.getPluginManager().callEvent((Event) eventInstance);
		}
	}
}
