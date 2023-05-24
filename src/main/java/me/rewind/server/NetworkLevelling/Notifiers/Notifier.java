package me.rewind.server.NetworkLevelling.Notifiers;

import me.rewind.server.NetworkLevelling.Boosters.Booster;
import me.rewind.server.NetworkLevelling.NetworkStatistic;

public interface Notifier { void sendActivatedMessage(Booster<? extends NetworkStatistic> booster);}
