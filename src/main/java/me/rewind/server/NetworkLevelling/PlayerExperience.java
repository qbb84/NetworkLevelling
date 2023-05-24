package me.rewind.server.NetworkLevelling;

import org.bukkit.entity.Player;

public class PlayerExperience extends NetworkStatistic {


	public PlayerExperience(int value) {
		super(value);
	}

	@Override
	public int getValue(Player player) {
		return 0;
	}

	@Override
	public void setValue(Player player, int value) {

	}

	@Override
	public void changeValue(Player player, int delta) {

	}
}
