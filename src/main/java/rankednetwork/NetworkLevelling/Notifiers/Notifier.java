package rankednetwork.NetworkLevelling.Notifiers;

import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.UUID;

public interface Notifier {
	void sendNotification(Booster<? extends NetworkStatistic> booster);

	void sendNotification(UUID uuid);
}
