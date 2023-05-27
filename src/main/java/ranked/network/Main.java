package ranked.network;

import ranked.network.NetworkLevelling.Boosters.Booster;
import ranked.network.NetworkLevelling.Boosters.BoosterManager;
import ranked.network.NetworkLevelling.Boosters.BoosterScope;
import ranked.network.NetworkLevelling.Boosters.BoosterType;
import ranked.network.NetworkLevelling.PlayerExperience;
import ranked.network.NetworkLevelling.Webhooks.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {


    @Override
    public void onEnable() {
        // ( locally testing 27/05/23 )
        BoosterManager expBooster = new BoosterManager();
        PlayerExperience experience = new PlayerExperience(Bukkit.getPlayer("Rewind"));
        Booster<PlayerExperience> playerExpBooster = new Booster<>(Bukkit.getPlayer("Rewind"), experience, BoosterType.HALF, BoosterScope.PERSONAL);
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
                .setColor(Color.AQUA)
                .setTitle(playerExpBooster.getStatistic().getType() + " Booster Activation!")
                .setThumbnail("https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
                .setFooter("Thank you for the support!", "https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
                .setDescription(playerExpBooster.getPlayer().getName() + " has activated a " + playerExpBooster.getBoosterType().getBoostIncreasePercentage() + "% " + playerExpBooster.getStatistic().getName() + " Booster!");
        expBooster.activateBooster(playerExpBooster);
        expBooster.sendDiscordMessage("https://discord.com/api/webhooks/1112096883452682310/rJNfwrXY7wmGABUZz-Z8UHGQVGiW8u102dPZ3YPIaedZmZidqVUeuYfgTRcnStk_q1Uf", playerExpBooster, embedObject);

    }

    @Override
    public void onDisable() {

    }
}
