package me.rewind.server.NetworkLevelling.Boosters;

import me.rewind.server.NetworkLevelling.NetworkStatistic;

import java.util.Comparator;

public class BoosterDurationComparator implements Comparator<Booster<? extends NetworkStatistic>> {
	@Override
	public int compare(Booster<? extends NetworkStatistic> o1, Booster<? extends NetworkStatistic> o2) {
		return Long.compare(o1.getDuration(), o2.getDuration());
	}
}