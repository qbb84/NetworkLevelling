package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActivationEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActiveEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterExpirationEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static rankednetwork.NetworkLevelling.EventManager.registerEvents;

/**
 * Manages the queues for both personal and global boosters.
 *
 * <p>This class provides methods for adding a booster to the queue, getting the time
 * until a booster becomes active, displaying the booster queue to a player, and checking
 * and activating the next booster in the queue.
 *
 * <p>A booster queue contains a series of boosters that are activated in sequence.
 * Once a booster is activated, it remains active for a certain duration, after which
 * the next booster in the queue (if any) is activated.
 */
public class BoosterQueue {

	private final Queue<Booster<?>> personalBoosterQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Booster<?>> globalBoosterQueue = new ConcurrentLinkedQueue<>();

	/**
	 * Adds a booster to the relevant queue (personal or global) and sets its activation time.
	 *
	 * @param booster the booster to add
	 */
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

	/**
	 * Returns the time until the given booster becomes active.
	 *
	 * @param booster the booster to check
	 * @return the time until the booster becomes active
	 */
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

	/**
	 * Displays the booster queue to a player in chat.
	 *
	 * @param player the player to display the queue to
	 */
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
			player.sendMessage("size:" + BoosterManager.getInstance().boosterCount());
		}
		player.sendMessage(ChatColor.GOLD + "-------------------------");
	}

	public Queue<Booster<?>> getPersonalBoosterQueue() {
		return personalBoosterQueue;
	}

	public Queue<Booster<?>> getGlobalBoosterQueue() {
		return globalBoosterQueue;
	}

	/**
	 * Returns a Runnable that, when executed, will check and activate the next booster
	 * in the global and personal booster queues.
	 */
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

					Bukkit.getScheduler().runTask(Main.getMain(), () -> registerEvents(new BoosterActiveEvent(
							activeBooster.getPlayerUUID(),
							activeBooster.getBoosterName(),
							activeBooster
					)));
				}

				if (!boosterQueue.isEmpty() && boosterQueue.peek().getRemainingTime() <= 0) {
					Booster<?> expiredBooster = boosterQueue.peek();
					expiredBooster.setStatus(Booster.Status.EXPIRED);

					Booster<?> finalExpiredBooster = expiredBooster;
					Bukkit.getScheduler().runTask(Main.getMain(), () -> registerEvents(new BoosterExpirationEvent(
							finalExpiredBooster.getPlayerUUID(),
							finalExpiredBooster.getBoosterName(),
							finalExpiredBooster
					)));


					expiredBooster = boosterQueue.poll();
					Bukkit.broadcastMessage("Booster removed from queue: " + (expiredBooster == null ? "null" : expiredBooster.getBoosterName()));
					BoosterManager.getInstance().getBoosterActivationTimes().remove(expiredBooster);
					BoosterManager.getInstance().getTotalBoosters().remove(expiredBooster);
					BoosterManager.getInstance().getActiveBoosters().remove(expiredBooster);

					if (!boosterQueue.isEmpty()) {
						// If there's another booster in the queue, activate it
						Booster<?> nextBooster = boosterQueue.peek();
						nextBooster.activate();

						long activationTime = System.currentTimeMillis();
						BoosterManager.getInstance().getBoosterActivationTimes().put(nextBooster, activationTime);

						Bukkit.getScheduler().runTask(Main.getMain(), () -> registerEvents(new BoosterActivationEvent(
								nextBooster.getPlayerUUID(),
								nextBooster.getBoosterName(),
								nextBooster
						)));


					}
				}

			}
		};
	}

}

