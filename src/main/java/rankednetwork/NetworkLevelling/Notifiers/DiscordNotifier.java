package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.Bukkit;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class DiscordNotifier extends DiscordWebhook implements Notifier {

	private DiscordWebhook webhook;
	private String webHookURL;
	private EmbedObject object;


	public DiscordNotifier(String webhookURL) {
		super(webhookURL);
		this.webHookURL = webhookURL;
		this.webhook = new DiscordWebhook(webhookURL);
	}

	@Override
	public void sendNotification(Booster<? extends NetworkStatistic> booster) {
		try {
			webhook.execute();
		} catch (IOException ioException) {
			Bukkit.getServer().getLogger().severe(Arrays.toString(ioException.getStackTrace()));
		}
	}

	/**
	 * @param uuid
	 */
	@Override
	public void sendNotification(UUID uuid) {

	}

	public String getWebhookURL() {
		return this.webHookURL;
	}

	public void setWebhookURL(String webhookURL) {
		this.webHookURL = webhookURL;
		this.webhook = new DiscordWebhook(webhookURL);
	}

	public DiscordWebhook getWebhook() {
		return webhook;
	}

	public EmbedObject getEmbed() {
		return object;
	}

	public void setEmbed(EmbedObject object) {
		this.object = object;
		webhook.addEmbed(getEmbed());
	}


}

