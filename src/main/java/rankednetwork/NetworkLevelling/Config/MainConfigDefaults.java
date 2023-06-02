package rankednetwork.NetworkLevelling.Config;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import rankednetwork.Main;

import java.util.*;

/**
 * This class is used to handle the default configuration values in the `main_settings.yml` file.
 * It includes discord related configuration, booster activation and deactivation messages,
 * and player levelling messages.
 * <p>
 * The configuration settings include placeholders that will be replaced by variables:
 * - {p}: the player's name
 * - {b_name}: the name of the booster
 * - {b_amount}: the XP percentage increase provided by the booster
 * - {b_type}: the type of NetworkStatistic the booster applies to
 * - {p_level}: the player's level after levelling up
 */
public class MainConfigDefaults {

	private static final Config mainConfigSettings;

	static {
		mainConfigSettings = new Config(Main.getMain(), "main_settings");
	}


	public MainConfigDefaults() {
		createDefaults();
	}

	public static void createDefaults() {
		YamlConfiguration settings = mainConfigSettings.getConfig();

		ConfigurationSection discord = settings.getConfigurationSection("discord");

		if (discord == null) {
			discord = settings.createSection("discord");
			mainConfigSettings.save();
		}


		List<String> discordComments = new ArrayList<>();
		discordComments.add("The discord section contains settings for Discord integration.");
		discordComments.add("{p} will be replaced with the player's name.");
		discordComments.add("{b_name} will be replaced with the booster's name.");
		discordComments.add("{b_amount} will be replaced with the XP percent increase.");
		discordComments.add("{b_type} will be replaced with the NetworkStatistic the booster is for.");
		discordComments.add("{b_duration} will be replaced with the boosters duration");
		settings.setComments("discord", discordComments);


		final Map<String, Object> discordDefaults = new HashMap<>();
		discordDefaults.put("webhook_url", "https://discord.com/api/webhooks/1111309366633709658/3z-lXUwJSodp3k9mb8Lsog897HxHlwd1rnaVATdGWvp7XIZM_So3eX7YK6Xddrmo88Tm");

		for (Map.Entry<String, Object> defaults : discordDefaults.entrySet()) {
			if (!discord.isSet(defaults.getKey())) {
				discord.set(defaults.getKey(), defaults.getValue());
				mainConfigSettings.save();
			}
		}
		//Activation
		ConfigurationSection activationSection = discord.getConfigurationSection("booster_activation");
		if (activationSection == null) {
			activationSection = discord.createSection("booster_activation");
			mainConfigSettings.save();
		}

		final Map<String, Object> activation_defaults = new HashMap<>();
		activation_defaults.put("Color", Color.AQUA);
		activation_defaults.put("Random_Color", false);
		activation_defaults.put("Title", "");
		activation_defaults.put("Thumbnail", "");
		activation_defaults.put("Description", "");

		for (Map.Entry<String, Object> activations : activation_defaults.entrySet()) {
			if (!activationSection.isSet(activations.getKey())) {
				activationSection.set(activations.getKey(), activations.getValue());
				mainConfigSettings.save();
			}
		}

		ConfigurationSection footer = activationSection.getConfigurationSection("footer");
		if (footer == null) {
			footer = activationSection.createSection("footer");
			mainConfigSettings.save();
		}
		Map<String, Object> footerDefaults = new HashMap<>();
		footerDefaults.put("message", "");
		footerDefaults.put("image", "");

		for (Map.Entry<String, Object> footers : footerDefaults.entrySet()) {
			if (!footer.isSet(footers.getKey())) {
				footer.set(footers.getKey(), footers.getValue());
				mainConfigSettings.save();
			}
		}

		//Deactivation
		ConfigurationSection deactivationSection = discord.getConfigurationSection("booster_deactivation");
		if (deactivationSection == null) {
			deactivationSection = discord.createSection("booster_deactivation");
			mainConfigSettings.save();
		}


		for (Map.Entry<String, Object> activations : activation_defaults.entrySet()) {
			if (!deactivationSection.isSet(activations.getKey())) {
				deactivationSection.set(activations.getKey(), activations.getValue());
				mainConfigSettings.save();
			}
		}

		ConfigurationSection expiry_footer = deactivationSection.getConfigurationSection("footer");
		if (expiry_footer == null) {
			expiry_footer = deactivationSection.createSection("footer");
			mainConfigSettings.save();
		}

		for (Map.Entry<String, Object> footers : footerDefaults.entrySet()) {
			if (!expiry_footer.isSet(footers.getKey())) {
				expiry_footer.set(footers.getKey(), footers.getValue());
				mainConfigSettings.save();
			}
		}

		//boosters section
		ConfigurationSection boosters = settings.getConfigurationSection("boosters");

		if (boosters == null) {
			boosters = settings.createSection("boosters");
			mainConfigSettings.save();
		}

		List<String> boostersComments = new ArrayList<>();
		boostersComments.add("The boosters section contains settings for boosters.");
		boostersComments.add("{queue_prompt} will be replaced with the queue prompt message.");
		boostersComments.add("{active_prompt} will be replaced with the active prompt message.");
		boostersComments.add("{expired_prompt} will be replaced with the expired prompt message.");
		settings.setComments("boosters", boostersComments);

		final Map<String, Object> boosterDefaults = new HashMap<>();
		boosterDefaults.put("queue_prompt", "");
		boosterDefaults.put("active_prompt", "");
		boosterDefaults.put("expired_prompt", "");


		for (Map.Entry<String, Object> defaults : boosterDefaults.entrySet()) {
			if (!boosters.isSet(defaults.getKey())) {
				boosters.set(defaults.getKey(), defaults.getValue());
				mainConfigSettings.save();
			}
		}

		//Levelling
		ConfigurationSection levelling = settings.getConfigurationSection("levelling");

		if (levelling == null) {
			levelling = settings.createSection("levelling");
			mainConfigSettings.save();
		}

		List<String> levellingComments = new ArrayList<>();
		levellingComments.add("The levelling section contains settings for levelling.");
		levellingComments.add("{level_up_prompt} will be replaced with the level up prompt message.");
		settings.setComments("levelling", levellingComments);

		final Map<String, Object> levellingefaults = new HashMap<>();
		levellingefaults.put("level_up_prompt", "");
		levellingefaults.put("Base_XP", 3000);
		levellingefaults.put("Base_XP_Increase", 3000);
		levellingefaults.put("Scaling", "LINEAR");


		for (Map.Entry<String, Object> levelDefaults : levellingefaults.entrySet()) {
			if (!levelling.isSet(levelDefaults.getKey())) {
				levelling.set(levelDefaults.getKey(), levelDefaults.getValue());
				mainConfigSettings.save();
			}
		}
		//Main Comments
		settings.setComments("discord.webhook_url", new LinkedList<>(List.of("The channel URL of the webhook that Discord uses to send messages")));
		settings.setComments("discord.booster_activation", new LinkedList<>(List.of("Configuration for when a booster is activated")));
		settings.setComments("discord.booster_activation.Description", new LinkedList<>(List.of("The description of the activation message")));
		settings.setComments("discord.booster_activation.Random_Color", new LinkedList<>(List.of("If this is true, the color of the activation message will be randomized")));
		settings.setComments("discord.booster_activation.Color", new LinkedList<>(List.of("The color of the activation message")));
		settings.setComments("discord.booster_activation.Title", new LinkedList<>(List.of("The title of the activation message")));
		settings.setComments("discord.booster_activation.Thumbnail", new LinkedList<>(List.of("The thumbnail URL of the activation message")));
		settings.setComments("discord.booster_activation.footer.image", new LinkedList<>(List.of("The footer image URL of the activation message")));
		settings.setComments("discord.booster_activation.footer.message", new LinkedList<>(List.of("The footer message of the activation message")));
		settings.setComments("discord.booster_deactivation", new LinkedList<>(List.of("Similar fields as booster_activation, just change the messages as per your requirements")));
		settings.setComments("boosters.queue_prompt", new LinkedList<>(List.of("The prompt for when a booster is added to the queue")));
		settings.setComments("boosters.active_prompt", new LinkedList<>(List.of("he prompt for when a booster is activated")));
		settings.setComments("boosters.expired_prompt", new LinkedList<>(List.of("The prompt for when a booster has expired")));
		settings.setComments("levelling.level_up_prompt", new LinkedList<>(List.of("The prompt for when a player levels up")));
		settings.setComments("levelling.Base_XP", new LinkedList<>(List.of("The Base XP Required to Level - I SUGGEST NOT ALTERING LEAVE. LEAVE THEM DEFAULT")));
		settings.setComments("levelling.Base_XP_Increase", new LinkedList<>(List.of("The increase of XP needed for the next level when a player levels - I SUGGEST NOT ALTERING LEAVE. LEAVE THEM DEFAULT")));
		settings.setComments("levelling.Scaling", new LinkedList<>(List.of("The prompt for when a player levels up")));

		mainConfigSettings.save();

	}

	public static synchronized YamlConfiguration getMainSettings() {
		return mainConfigSettings.getConfig();

	}

	public static String translateString(String absoluteConfigStringLocation, HashMap<String, Object> replacement) {
		String result = getMainSettings().getString(absoluteConfigStringLocation);

		if (absoluteConfigStringLocation == null) {
			throw new NullPointerException(absoluteConfigStringLocation + " can't be found!");
		}

		for (Map.Entry<String, Object> replacements : replacement.entrySet()) {
			result = result.replace(replacements.getKey(), replacements.getValue().toString());
		}
		return ChatColor.translateAlternateColorCodes('&', result);
	}


}
