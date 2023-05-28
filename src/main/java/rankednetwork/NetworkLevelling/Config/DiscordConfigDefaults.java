package rankednetwork.NetworkLevelling.Config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import rankednetwork.Main;

public class DiscordConfigDefaults {

	private static final Config discordSettings;

	static {
		discordSettings = new Config(Main.getMain(), "discord_settings");
	}

	public DiscordConfigDefaults(){
		createDefaults();
	}

	public static void createDefaults(){
		YamlConfiguration settings = discordSettings.getConfig();

		ConfigurationSection general = settings.getConfigurationSection("general");

		if(general == null) {
			general = settings.createSection("general");
			discordSettings.save();
		}

		if(!general.isSet("webhook_url")) {
			general.set("webhook_url", "https://discord.com/api/webhooks/1111309366633709658/3z-lXUwJSodp3k9mb8Lsog897HxHlwd1rnaVATdGWvp7XIZM_So3eX7YK6Xddrmo88Tm");
			discordSettings.save();
		}

	}


	public static synchronized YamlConfiguration getDiscordSettings(){
		return discordSettings.getConfig();

	}


}
