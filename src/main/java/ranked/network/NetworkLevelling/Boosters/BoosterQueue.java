package ranked.network.NetworkLevelling.Boosters;

import java.util.LinkedList;
import java.util.Queue;

public class BoosterQueue {
	private Queue<Booster<?>> personalBoosterQueue = new LinkedList<>();
	private Queue<Booster<?>> globalBoosterQueue = new LinkedList<>();


	public void addBooster(Booster<?> booster) {

		if (booster.getScope() == BoosterScope.PERSONAL) {

			if(personalBoosterQueue.contains(booster)){return;}

			personalBoosterQueue.add(booster);

		} else if (booster.getScope() == BoosterScope.GLOBAL) {

			if(globalBoosterQueue.contains(booster)){return;}

			globalBoosterQueue.add(booster);
		}
	}

	public long getTimeUntilActive(Booster<?> booster) {
		Queue<Booster<?>> relevantQueue;
		if (booster.getScope() == BoosterScope.PERSONAL) {
			relevantQueue = personalBoosterQueue;
		} else if (booster.getScope() == BoosterScope.GLOBAL) {
			relevantQueue = globalBoosterQueue;
		} else {
			throw new IllegalArgumentException("Unknown booster scope");
		}
		if (!relevantQueue.contains(booster)) {
			throw new IllegalArgumentException("Booster is not in queue");
		}

		long totalTime = 0;
		for (Booster<?> b : relevantQueue) {
			totalTime += b.getDuration();
			if (b == booster) {
				break;
			}
		}

		return totalTime;
	}
}
