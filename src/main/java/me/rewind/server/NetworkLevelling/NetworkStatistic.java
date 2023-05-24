package me.rewind.server.NetworkLevelling;

import org.bukkit.entity.Player;

public abstract class NetworkStatistic {

	protected int value;

	public NetworkStatistic(int value) {
		this.value = value;
	}

	public abstract int getValue(Player player);

	public abstract void setValue(Player player, int value);

	public abstract void changeValue(Player player, int delta);

	public String getStatistic() {
		// some default implementation to get statistics
		return null;
	}
}
