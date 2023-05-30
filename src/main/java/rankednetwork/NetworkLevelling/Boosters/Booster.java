package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.Bukkit;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Booster<T extends NetworkStatistic> {


	private String boosterName;
	private Player player;

	private T statistic;

	private double boostAmount;
	private long duration;

	private long activationTime = 0;

	private boolean isActive;

	BoosterScope scope;
	BoosterType boosterType;

	private Status status = Status.INACTIVE;

	public enum Status {

		ACTIVE(Color.GREEN + "ACTIVE"),
		INACTIVE(Color.RED + "INACTIVE");

		final String statusName;

		Status(String statusName) {
			this.statusName = statusName;
		}

		public String getStatusName() {
			return statusName;
		}
	}


	public Booster(Player player, T statistic, BoosterType boosterType, BoosterScope scope, String boosterName) {
		this.player = player;
		this.statistic = statistic;
		this.boostAmount = boosterType.getBoostIncreasePercentage();
		this.duration = boosterType.getBoosterTime();
		this.scope = scope;
		this.boosterType = boosterType;
		this.boosterName = boosterName;
	}


	public Player getPlayer() {
		return player;
	}

	public T getStatistic() {
		return statistic;
	}

	public double getBoostAmount() {
		return boostAmount;
	}

	public long getDuration() {
		return duration;
	}

	public void setMultiplier(double boostAmount) {
		this.boostAmount = boostAmount;
	}

	public void setDuration(long duration) {
		this.duration = duration * 60 * 1000;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}


	public BoosterScope getScope() {
		return scope;
	}

	public void setScope(BoosterScope scope) {
		this.scope = scope;
	}

	public BoosterType getBoosterType() {
		return boosterType;
	}

	public String getBoosterName() {
		return boosterName;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void activate() {
		this.isActive = true;
		this.activationTime = System.currentTimeMillis();
		setStatus(Status.ACTIVE);
	}

	public void deactivate() {
		this.isActive = false;
	}



	public long getRemainingTime() {
		if (!isActive) return 0;
		long timePassed = System.currentTimeMillis() - this.activationTime; // in milliseconds
		long remainingTimeMillis = Math.max(0, this.getDuration()  - timePassed);
		return TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis);
	}

	public long getActivationTime() {
		return activationTime;
	}
}




