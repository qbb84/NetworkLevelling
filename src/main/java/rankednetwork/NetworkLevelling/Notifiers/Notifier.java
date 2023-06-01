package rankednetwork.NetworkLevelling.Notifiers;

import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public interface Notifier {
	void sendActivatedMessage(Booster<? extends NetworkStatistic> booster);
}
