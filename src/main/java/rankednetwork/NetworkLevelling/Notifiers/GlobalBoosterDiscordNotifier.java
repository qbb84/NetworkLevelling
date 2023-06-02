package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.Config.MainConfigDefaults;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GlobalBoosterDiscordNotifier {

	private final YamlConfiguration mainConfig = MainConfigDefaults.getMainSettings();

	public GlobalBoosterDiscordNotifier() {

	}

	public void sendGlobalDiscordMessage(Booster<?> booster) {
		ConfigurationSection general = mainConfig.getConfigurationSection("discord");

		String url = general.get("webhook_url").toString();

		HashMap<String, Object> stringMutation = new HashMap<>();
		stringMutation.put("{b_name}", booster.getBoosterName());
		stringMutation.put("{p}", booster.getOfflinePlayer().getName());
		if (booster.getBoosterType().getBoosterTypeName().equalsIgnoreCase(BoosterType.CUSTOM.getBoosterTypeName())) {
			stringMutation.put("{b_amount}", booster.getBoostAmount() + "%");
			stringMutation.put("{b_duration}", booster.getDurationInMinutes());
		} else {
			stringMutation.put("{b_amount}", booster.getBoosterType().getBoostIncreasePercentage() + "%");
			stringMutation.put("{b_duration}", booster.getDurationInMinutes());
		}
		stringMutation.put("{b_type}", booster.getStatistic().getType());

		//May need to change this section if you're thinking of sending more discord messages for different statuses. Fine for now.
		boolean isBoosterActive = booster.getStatus().equals(Booster.Status.ACTIVE);
		boolean isRandomActive = isBoosterActive ? mainConfig.getBoolean("discord.booster_activation.Random_Color") : mainConfig.getBoolean("discord.booster_deactivation.Random_Color");

		String title = isBoosterActive ? mainConfig.getString("discord.booster_activation.Title") : mainConfig.getString("discord.booster_deactivation.Title");
		String description = isBoosterActive ? mainConfig.getString("discord.booster_activation.Description") : mainConfig.getString("discord.booster_deactivation.Description");
		String thumbnail = isBoosterActive ? mainConfig.getString("discord.booster_activation.Thumbnail") : mainConfig.getString("discord.booster_deactivation.Thumbnail");
		String footerImage = isBoosterActive ? mainConfig.getString("discord.booster_activation.footer.image") : mainConfig.getString("discord.booster_deactivation.footer.image");
		String footerDescription = isBoosterActive ? mainConfig.getString("discord.booster_activation.footer.message") : mainConfig.getString("discord.booster_deactivation.footer.message");

		int alpha = isBoosterActive ? mainConfig.getInt("discord.booster_activation.ALPHA") : mainConfig.getInt("discord.booster_deactivation.ALPHA");
		int red = isBoosterActive ? mainConfig.getInt("discord.booster_activation.RED") : mainConfig.getInt("discord.booster_deactivation.RED");
		int green = isBoosterActive ? mainConfig.getInt("discord.booster_activation.GREEN") : mainConfig.getInt("discord.booster_deactivation.GREEN");
		int blue = isBoosterActive ? mainConfig.getInt("discord.booster_activation.BLUE") : mainConfig.getInt("discord.booster_deactivation.BLUE");

		Color color = isRandomActive ? randomColor() : Color.fromARGB(alpha, red, green, blue);

		DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
				.setColor(color)
				.setTitle(MainConfigDefaults.translateString(title, stringMutation))
				.setThumbnail(thumbnail)
				.setFooter(MainConfigDefaults.translateString(footerDescription, stringMutation), footerImage)
				.setDescription(MainConfigDefaults.translateString(description, stringMutation));
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
