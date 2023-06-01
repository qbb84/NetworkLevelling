package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Config.DiscordConfigDefaults;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GlobalBoosterDiscordNotifier {

	public GlobalBoosterDiscordNotifier() {

	}

	public void sendGlobalDiscordMessage(Booster<?> booster) {
		YamlConfiguration discordConfigDefaults = DiscordConfigDefaults.getDiscordSettings();

		ConfigurationSection general = discordConfigDefaults.getConfigurationSection("general");


		if (general == null) {
			general = discordConfigDefaults.createSection("general");
		}

		if (!general.isSet("webhook_url")) {
			general.set("webhook_url", "");
		}

		String url = general.get("webhook_url").toString();

		DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
				.setColor(randomColor())
				.setTitle(booster.getBoosterName() + " Booster Activation!")
				.setThumbnail("https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
				.setFooter("Thank you for the support!", "https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
				.setDescription(booster.getPlayer().getName() + " has activated a " + booster.getBoosterType().getBoostIncreasePercentage() + "% " + booster.getStatistic().getName() + " Booster!");
		BoosterManager.getInstance().sendDiscordMessage(url, booster, embedObject);
	}

	public Color randomColor() {
		Field[] declaredFields = Color.class.getDeclaredFields();
		List<Color> colors = new ArrayList<>();
		for (Field field : declaredFields) {
			if (field.getType() == Color.class) {
				try {
					colors.add((Color) field.get(null));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return colors.get(new Random().nextInt(colors.size() - 1));

	}
}
