package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActivationEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActiveEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterExpirationEvent;

import java.util.LinkedList;
import java.util.Queue;

import static rankednetwork.NetworkLevelling.EventManager.registerEvents;

public class BoosterQueue {

	private final Queue<Booster<?>> personalBoosterQueue = new LinkedList<>();
	private final Queue<Booster<?>> globalBoosterQueue = new LinkedList<>();


	public void addBooster(Booster<?> booster) {
		BoosterManager.getInstance().getBoosterActivationTimes().put(booster, System.currentTimeMillis());
		if (booster.getScope() == BoosterScope.PERSONAL) {
			if (personalBoosterQueue.contains(booster)) {
				return;
			}
			personalBoosterQueue.add(booster);

		} else if (booster.getScope() == BoosterScope.GLOBAL) {


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
			if (b == booster) {
				break;
			}
			long boosterDuration;
			if (b.isActive()) {
				boosterDuration = b.getRemainingTime();
			} else {
				boosterDuration = b.getDurationInMinutes();
			}
			totalTime += boosterDuration;
		}

		return totalTime + 1;
	}


	public void displayBoosterQueue(Player player) {
		player.sendMessage(ChatColor.GOLD + "----- Booster Queue -----");
		if (getGlobalBoosterQueue().isEmpty()) {
			player.sendMessage(ChatColor.YELLOW + "The booster queue is empty.");
			player.sendMessage(BoosterManager.getInstance().getBoosterActivationTimes().toString());
			player.sendMessage(getGlobalBoosterQueue().toString());
		} else {
			int position = 1;
			for (Booster<?> booster : getGlobalBoosterQueue()) {
				String boosterName = booster.getBoosterName();
				String timeStatus;
				if (booster.isActive()) {
					timeStatus = (booster.getRemainingTime() <= 0) ? " ending soon!" : booster.getRemainingTime() + " minutes remaining! ";
				} else if (getGlobalBoosterQueue().peek().equals(booster)) {
					timeStatus = "1 minute until activation! ";
				} else {
					timeStatus = getTimeUntilActive(booster) + " minutes until activation! ";
				}
				player.sendMessage(ChatColor.YELLOW + "Position " + position + ": " + boosterName + " (" + timeStatus + ") " + booster.getStatus());
				position++;
			}
		}
		player.sendMessage(ChatColor.GOLD + "-------------------------");
	}

	public Queue<Booster<?>> getPersonalBoosterQueue() {
		return personalBoosterQueue;
	}

	public Queue<Booster<?>> getGlobalBoosterQueue() {
		return globalBoosterQueue;
	}

	public Runnable checkAndActivateNextBooster() {
		return new Runnable() {
			@Override
			public void run() {
				checkAndActivateBoosterInQueue(globalBoosterQueue);
				checkAndActivateBoosterInQueue(personalBoosterQueue);
			}

			private void checkAndActivateBoosterInQueue(Queue<Booster<?>> boosterQueue) {
				if (!boosterQueue.isEmpty()) {
					Booster<?> activeBooster = boosterQueue.peek();

					registerEvents(new BoosterActiveEvent(
							activeBooster.getPlayer(),
							activeBooster.getBoosterName(),
							activeBooster.getBoostAmount(),
							activeBooster.getScope(),
							activeBooster.getBoosterType(),
							activeBooster.getStatistic(),
							activeBooster.getStatus()
					));
				}

				if (!boosterQueue.isEmpty() && boosterQueue.peek().getRemainingTime() <= 0) {
					Booster<?> expiredBooster = boosterQueue.peek();
					expiredBooster.setStatus(Booster.Status.EXPIRED);
					//Listener for expiration event
					registerEvents(new BoosterExpirationEvent(
							expiredBooster.getPlayer(),
							expiredBooster.getBoosterName(),
							expiredBooster
					));

					expiredBooster = boosterQueue.poll();
					BoosterManager.getInstance().getBoosterActivationTimes().remove(expiredBooster);

					if (!boosterQueue.isEmpty()) {
						// If there's another booster in the queue, activate it
						Booster<?> nextBooster = boosterQueue.peek();
						nextBooster.activate();

						long activationTime = System.currentTimeMillis();
						BoosterManager.getInstance().getBoosterActivationTimes().put(nextBooster, activationTime);

						//Call custom event
						registerEvents(new BoosterActivationEvent(
								nextBooster.getPlayer(),
								nextBooster.getBoosterName(),
								nextBooster.getBoostAmount(),
								nextBooster.getScope(),
								nextBooster.getBoosterType(),
								nextBooster.getStatistic(),
								nextBooster.getStatus()
						));

					}
				}

			}
		};
	}

}

