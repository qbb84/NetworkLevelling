package ranked.network.NetworkLevelling.Boosters;

import ranked.network.NetworkLevelling.NetworkStatistic;
import ranked.network.NetworkLevelling.Notifiers.DiscordNotifier;
import ranked.network.NetworkLevelling.Notifiers.GameNotifier;
import ranked.network.NetworkLevelling.Webhooks.DiscordWebhook;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class BoosterManager {

	private Booster<? extends NetworkStatistic> activeBooster;

	private  SortedSet<Booster<? extends NetworkStatistic>> totalBoosters = new TreeSet<>(new BoosterDurationComparator());
	private  SortedSet<Booster<? extends NetworkStatistic>> activeBoosters = new TreeSet<>(new BoosterDurationComparator());

	private Map<Booster<? extends NetworkStatistic>, Long> boosterActivationTimes = new HashMap<>();



	public enum Status{

		ACTIVE(Color.GREEN + "ACTIVE"),
		INACTIVE(Color.RED + "INACTIVE");
//		CAN_BOOST,
//		BOOSTER_COUNT,
//		GET_ACTIVE_BOOSTERS,
//		GET_TOTAL_BOOSTERS;
// TODO Implement

		  String statusName;

		Status(String statusName){
			this.statusName = statusName;
		}

	}

	public <T extends NetworkStatistic> void activateBooster(Booster<T> booster) {
		activeBooster = booster;
		GameNotifier gameNotifier = new GameNotifier();

		switch (booster.getScope()){
			case PERSONAL:
				gameNotifier.sendActivatedMessage(booster);
				break;
			case GLOBAL:
				//Add personal
				break;
			default:
		}

	}

	public <T extends  Booster<?>>void sendDiscordMessage(String url, T booster, DiscordWebhook.EmbedObject embedObject) {
		DiscordNotifier boosterNotifier = new DiscordNotifier(url);
		boosterNotifier.setEmbed(embedObject);
		boosterNotifier.sendActivatedMessage(booster);
	}

	public boolean hasExpired(Booster<? extends NetworkStatistic> booster) {
		if (!boosterActivationTimes.containsKey(booster)) {
			throw new IllegalStateException("Booster hasn't been activated yet!");
		}
		long activationTime = boosterActivationTimes.get(booster);
		long currentTime = System.currentTimeMillis();
		return currentTime > (activationTime + booster.getDuration());
	}






}
