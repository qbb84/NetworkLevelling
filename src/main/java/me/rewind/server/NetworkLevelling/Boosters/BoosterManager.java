package me.rewind.server.NetworkLevelling.Boosters;

import me.rewind.server.NetworkLevelling.NetworkStatistic;
import me.rewind.server.NetworkLevelling.Notifiers.DiscordNotifier;
import me.rewind.server.NetworkLevelling.Notifiers.GameNotifier;
import me.rewind.server.NetworkLevelling.Webhooks.DiscordWebhook;

import java.awt.*;
import java.util.SortedSet;
import java.util.TreeSet;

public class BoosterManager {

	private Booster<? extends NetworkStatistic> activeBooster;

	private final SortedSet<Booster<? extends NetworkStatistic>> totalBoosters = new TreeSet<>(new BoosterDurationComparator());
	private final SortedSet<Booster<? extends NetworkStatistic>> activeBoosters = new TreeSet<>(new BoosterDurationComparator());


	public enum Status{

		ACTIVE(Color.GREEN + "ACTIVE"),
		INACTIVE(Color.RED + "INACTIVE");
//		CAN_BOOST,
//		BOOSTER_COUNT,
//		GET_ACTIVE_BOOSTERS,
//		GET_TOTAL_BOOSTERS;
		// Change these all to methods

		  String statusName;

		Status(String statusName){
			this.statusName = statusName;
		}

	}

	public <T extends NetworkStatistic> void activateBooster(Booster<T> booster) {
		switch (booster.getScope()){
			case PERSONAL:
				activeBooster = booster;
				GameNotifier gameNotifier = new GameNotifier();
				gameNotifier.sendActivatedMessage(booster);
				break;
			case GLOBAL:
				//TODO SETUP QUEUE OF GLOBAL BOOSTER TYPES
				break;
			default:
		}

	}

	public <T extends  Booster<?>>void sendDiscordMessage(String url, T booster, DiscordWebhook.EmbedObject embedObject) {
		DiscordNotifier boosterNotifier = new DiscordNotifier(url);
		boosterNotifier.setEmbed(embedObject);
		boosterNotifier.sendActivatedMessage(booster);
	}





}
