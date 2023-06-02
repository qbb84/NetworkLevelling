package rankednetwork.NetworkLevelling;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class NetworkStatistic {

	protected static final List<String> availableStats = new ArrayList<>();

	protected int value;
	protected String name;
	protected String type;
	protected UUID player;

	public NetworkStatistic() {
	}

	public NetworkStatistic(UUID player, String name, String type) {
		this.player = player;
		this.name = name;
		this.type = type;
	}

	public static List<String> getAvailableStats() {
		return availableStats;
	}

	public static <T extends NetworkStatistic> void addStatistic(T t) {
		BoosterMetadata className = t.getClass().getAnnotation(BoosterMetadata.class);
		if (className.name() == "null") {
			if (!NetworkStatistic.availableStats.contains(t.getClass().getSimpleName())) {
				NetworkStatistic.availableStats.add(t.getClass().getSimpleName());

			}
			return;
		}
		if (!NetworkStatistic.availableStats.contains(className.name())) {
			NetworkStatistic.availableStats.add(className.name());
		}
	}

	public static String statsList() {
		StringBuilder builder = new StringBuilder();
		for (String stats : availableStats) {
			if (getAvailableStats().get(getAvailableStats().size() - 1) != stats) {
				builder.append(stats).append(", ");
			} else {
				builder.append(stats);
			}
		}
		return builder.toString();
	}

	public abstract int getValue(Player player);

	public abstract void setValue(Player player, int value);

	public abstract void changeValue(Player player, int delta);

	public abstract String getName();

	public abstract String getType();

	public abstract String setName(String name);

	public abstract String setType(String type);

	public UUID getUuid() {
		return player;
	}

	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(getUuid());
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(getUuid());
	}

	public String getStatistic() {
		return null;
	}


}
