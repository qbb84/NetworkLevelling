package ranked.network.NetworkLevelling.Notifiers;

import ranked.network.NetworkLevelling.Boosters.Booster;
import ranked.network.NetworkLevelling.NetworkStatistic;

public interface Notifier { void sendActivatedMessage(Booster<? extends NetworkStatistic> booster);}
