package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Represents a Booster in the network levelling system.
 * This class encapsulates the functionality of a Booster that manipulates a specific NetworkStatistic for a player.
 *
 * @param <T> The type of NetworkStatistic that this booster is associated with.
 */
public class Booster<T extends NetworkStatistic> {


	private final String boosterName;

	private final UUID playerUUID;

	private final T statistic;

	BoosterScope scope;
	BoosterType boosterType;

	private double boostAmount;
	private long duration;
	private long activationTime = 0;
	private boolean isActive;

	private Status status;

	/**
	 * Creates a new Booster.
	 *
	 * @param playerUUID  The player for whom this booster is for.
	 * @param statistic   The specific NetworkStatistic that this booster enhances.
	 * @param boosterType The type of the booster.
	 * @param scope       The scope of the booster.
	 * @param boosterName The name of the booster.
	 */
	public Booster(UUID playerUUID, T statistic, BoosterType boosterType, BoosterScope scope, String boosterName) {
		this.playerUUID = playerUUID;
		this.statistic = statistic;
		this.boostAmount = boosterType.getBoostIncreasePercentage();
		this.duration = boosterType.getBoosterTime();
		this.scope = scope;
		this.boosterType = boosterType;
		this.boosterName = boosterName;
	}

	/**
	 * Creates a Booster object from its serialized String representation.
	 * The serialized Booster string is expected to contain the following comma-separated values in order:
	 * <ol>
	 *     <li>BoosterType name</li>
	 *     <li>Maximum duration (in minutes)</li>
	 *     <li>Player's name</li>
	 *     <li>BoosterScope name</li>
	 *     <li>Activation status (boolean)</li>
	 *     <li>Activation time (in milliseconds since the epoch)</li>
	 *     <li>Boost amount</li>
	 *     <li>Booster name</li>
	 *     <li>NetworkStatistic type</li>
	 * </ol>
	 * <p>
	 * This method is primarily used for loading Boosters from the Booster queue cache.
	 *
	 * @param boosterString The serialized Booster string.
	 * @return A new Booster object reconstructed from the serialized Booster string.
	 */
	public static Booster<?> fromString(String boosterString) {
		String[] parts = boosterString.split(",");
		BoosterType boosterType = BoosterType.valueOf(parts[0]);
		long maxDuration = Long.parseLong(parts[1]);
		//long remainingDuration = Long.parseLong(parts[2]);
		String playerUUD = parts[3];
		UUID uuid = UUID.fromString(playerUUD);
		BoosterScope scope = BoosterScope.valueOf(parts[4].toUpperCase());

		boolean isActive = Boolean.parseBoolean(parts[5]);
		long activationTime = Long.parseLong(parts[6]);
		double boostAmount = Double.parseDouble(parts[7]);

		String boosterName = parts[8];
		NetworkStatistic statistic = BoosterManager.getInstance().getStatisticFromName(uuid, parts[9]);
		Booster<?> booster = new Booster<>(uuid, statistic, boosterType, scope, boosterName);
		Status status = Status.valueOf(parts[10]);

		booster.setDuration(maxDuration);
		booster.setMultiplier(boostAmount);
		booster.setStatus(status);

		if (isActive) {
			booster.activate();
			booster.activationTime = activationTime;
		}

		return booster;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getPlayerUUID());
	}

	public Player getOnlinePlayer() {
		OfflinePlayer offlinePlayer = getOfflinePlayer();
		if (offlinePlayer.isOnline()) {
			return (Player) offlinePlayer;
		}
		return null;
	}

	/**
	 * Sets the multiplier for the boost amount of the booster.
	 *
	 * @param boostAmount The multiplier for the boost amount.
	 */
	public void setMultiplier(double boostAmount) {
		this.boostAmount = boostAmount;
	}

	/**
	 * Activates the booster.
	 */
	public void activate() {
		this.isActive = true;
		this.activationTime = System.currentTimeMillis();
		BoosterManager.getInstance().getTotalBoosters().add(this);
		BoosterManager.getInstance().getActiveBoosters().add(this);
		setStatus(Status.ACTIVE);
	}

	/**
	 * Gets the current status of the booster.
	 *
	 * @return The current status of the booster.
	 */
	public Status getStatus() {
		return this.status;
	}

	/**
	 * Sets the status of the booster.
	 *
	 * @param status The status to set.
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Deactivates the booster.
	 */
	public void deactivate() {
		this.isActive = false;
	}

	/**
	 * Gets the duration of the booster in minutes.
	 *
	 * @return The duration of the booster in minutes.
	 */
	public long getDurationInMinutes() {
		return TimeUnit.MILLISECONDS.toMinutes(duration);
	}

	/**
	 * Serializes this Booster object into a String representation.
	 * The resulting string contains the following comma-separated values in order:
	 *
	 * <ol>
	 *     <li>BoosterType name</li>
	 *     <li>Maximum duration (in minutes)</li>
	 *     <li>Player's name</li>
	 *     <li>BoosterScope name</li>
	 *     <li>Activation status (boolean)</li>
	 *     <li>Activation time (in milliseconds since the epoch)</li>
	 *     <li>Boost amount</li>
	 *     <li>Booster name</li>
	 *     <li>NetworkStatistic type</li>
	 * </ol>
	 * <p>
	 * This method is primarily used for saving Boosters to the Booster queue cache.
	 *
	 * @return The serialized Booster string.
	 */
	public String toString() {
		if (isActive) {
			return getBoosterType().name() + "," + TimeUnit.MILLISECONDS.toMinutes(getDuration()) + "," + getRemainingTime() + "," + getPlayerUUID().toString() + "," + getScope() + "," + isActive() + "," + getActivationTime() + "," + getBoostAmount() + "," + getBoosterName() + "," + getStatistic().getType() + "," + getStatus();
		}
		return getBoosterType().name() + "," + TimeUnit.MILLISECONDS.toMinutes(getDuration()) + "," + "IN QUEUE" + "," + getPlayerUUID().toString() + "," + getScope() + "," + isActive() + "," + getActivationTime() + "," + getBoostAmount() + "," + getBoosterName() + "," + getStatistic().getType() + "," + getStatus();
	}

	/**
	 * Gets the booster type. See below for more info.
	 *
	 * @return
	 * @see #getBoostAmount()
	 * @see #getDuration()
	 */
	public BoosterType getBoosterType() {
		return boosterType;
	}

	public long getDuration() {
		return duration;

	}

	/**
	 * This method converts the duration to minutes.
	 *
	 * @param duration in milliseconds
	 */
	public void setDuration(long duration) {
		this.duration = duration * 60 * 1000;
	}

	/**
	 * This method gets the remaining time of Active boosters
	 *
	 * @return The remaining time of a booster converted to minutes
	 */
	public long getRemainingTime() {
		if (!isActive) return 0;
		long timePassed = System.currentTimeMillis() - this.activationTime; // in milliseconds
		long remainingTimeMillis = Math.max(0, this.getDuration() - timePassed);
		return TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public BoosterScope getScope() {
		return scope;
	}

	public void setScope(BoosterScope scope) {
		this.scope = scope;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public long getActivationTime() {
		return activationTime;
	}

	public double getBoostAmount() {
		return boostAmount;
	}

	public String getBoosterName() {
		return boosterName;
	}

	public T getStatistic() {
		return statistic;
	}

	/**
	 * Represents the current status of a Booster.
	 */
	public enum Status {

		ACTIVE(Color.GREEN + "Active"),
		INACTIVE(Color.BLACK + "Inactive"),
		INQUEUE(Color.YELLOW + "Inqueue"),
		EXPIRED(Color.RED + "Expired");

		final String statusName;
		Status status;

		Status(String statusName) {
			this.statusName = statusName;
		}

		public String getStatusName() {
			return statusName;
		}

	}
}




