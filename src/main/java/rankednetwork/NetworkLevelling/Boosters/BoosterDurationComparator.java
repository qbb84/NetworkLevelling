package rankednetwork.NetworkLevelling.Boosters;

import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.util.Comparator;

/**
 * A Comparator for Boosters that orders them by their duration.
 *
 * <p>This comparator can be used anywhere a sorted collection of Boosters is needed,
 * such as a SortedSet or a PriorityQueue. The compare method compares the durations
 * of two Boosters to determine their relative ordering.</p>
 */
public class BoosterDurationComparator implements Comparator<Booster<? extends NetworkStatistic>> {

	/**
	 * Compares two Boosters by duration.
	 *
	 * @param o1 the first Booster to compare
	 * @param o2 the second Booster to compare
	 * @return a negative integer, zero, or a positive integer as the first Booster's
	 * duration is less than, equal to, or greater than the second.
	 */
	@Override
	public int compare(Booster<? extends NetworkStatistic> o1, Booster<? extends NetworkStatistic> o2) {
		return Long.compare(o1.getDuration(), o2.getDuration());
	}
}