package rankednetwork.NetworkLevelling.Notifiers;

import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.Arrays;

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
	public void sendActivatedMessage(Booster<? extends NetworkStatistic> booster)  {
		try{
			webhook.execute();
		}catch (IOException ioException){
			Bukkit.getServer().getLogger().severe(Arrays.toString(ioException.getStackTrace()));
		}
	}

	public String getWebhookURL()  {
		return this.webHookURL;
	}

	public DiscordWebhook getWebhook() {
		return webhook;
	}

	public void setWebhookURL(String webhookURL) {
		this.webHookURL = webhookURL;
		this.webhook = new DiscordWebhook(webhookURL);
	}

	public EmbedObject getEmbed() {
		return object;
	}

	public void setEmbed(EmbedObject object) {
		this.object = object;
		webhook.addEmbed(getEmbed());;
	}



}

