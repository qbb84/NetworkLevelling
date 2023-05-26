package me.rewind.server.NetworkLevelling;

import org.bukkit.entity.Player;

public abstract class NetworkStatistic {

	protected int value;
	protected String name;
	protected String type;

	public NetworkStatistic(int value) {
		this.value = value;
	}

	public abstract int getValue(Player player);

	public abstract void setValue(Player player, int value);

	public abstract void changeValue(Player player, int delta);

	public abstract String getName();

	public abstract String getType();

	public abstract String setName(String name);

	public abstract String setType(String type);


	public String getStatistic() {
		// some default implementation to get statistics
		return null;
	}



}
