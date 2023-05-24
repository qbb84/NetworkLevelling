package me.rewind.server.NetworkLevelling.Boosters;

import me.rewind.server.NetworkLevelling.NetworkStatistic;
import org.bukkit.entity.Player;

public class Booster<T extends NetworkStatistic> {

	private Player player;
	private T statistic;
	private int boostAmount;
	private long duration;

	public Booster(Player player, T statistic, int boostAmount, long duration) {
		this.player = player;
		this.statistic = statistic;
		this.boostAmount = boostAmount;
		this.duration = duration;
	}

	// getters

	public Player getPlayer() {
		return player;
	}

	public T getStatistic() {
		return statistic;
	}

	public int getBoostAmount() {
		return boostAmount;
	}

	public long getDuration() {
		return duration;
	}

	// other methods
}



