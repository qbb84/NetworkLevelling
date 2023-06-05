package rankednetwork.NetworkLevelling.Notifiers;

import org.bukkit.Bukkit;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

/**
 * A notifier that sends notifications to Discord.
 */
public class DiscordNotifier extends DiscordWebhook implements Notifier {

	private DiscordWebhook webhook;
	private String webHookURL;
	private EmbedObject object;

	/**
	 * Creates a new DiscordNotifier with the given webhook URL.
	 *
	 * @param webhookURL the URL of the Discord webhook to use
	 */
	public DiscordNotifier(String webhookURL) {
		super(webhookURL);
		this.webHookURL = webhookURL;
		this.webhook = new DiscordWebhook(webhookURL);
	}

	/**
	 * Sends a notification about a booster.
	 *
	 * @param booster the booster to notify about
	 */
	@Override
	public void sendNotification(Booster<? extends NetworkStatistic> booster) {
		try {
			webhook.execute();
		} catch (IOException ioException) {
			Bukkit.getServer().getLogger().severe(Arrays.toString(ioException.getStackTrace()));
		}
	}

	/**
	 * Sends a notification for a user.
	 *
	 * @param uuid the UUID of the user to notify
	 */
	@Override
	public void sendNotification(UUID uuid) {

	}

	/**
	 * Gets the URL of the webhook.
	 *
	 * @return the URL of the webhook
	 */
	public String getWebhookURL() {
		return this.webHookURL;
	}

	/**
	 * Sets the URL of the webhook.
	 *
	 * @param webhookURL the new URL of the webhook
	 */
	public void setWebhookURL(String webhookURL) {
		this.webHookURL = webhookURL;
		this.webhook = new DiscordWebhook(webhookURL);
	}

	/**
	 * Gets the DiscordWebhook object.
	 *
	 * @return the DiscordWebhook object
	 */
	public DiscordWebhook getWebhook() {
		return webhook;
	}

	/**
	 * Gets the embed object.
	 *
	 * @return the embed object
	 */
	public EmbedObject getEmbed() {
		return object;
	}

	/**
	 * Sets the embed object.
	 *
	 * @param object the new embed object
	 */
	public void setEmbed(EmbedObject object) {
		this.object = object;
		webhook.addEmbed(getEmbed());
	}


}

