package rankednetwork;

import org.bukkit.plugin.java.JavaPlugin;
import rankednetwork.NetworkLevelling.Boosters.BoosterManager;
import rankednetwork.NetworkLevelling.Commands.BoosterCommand;
import rankednetwork.NetworkLevelling.Config.DiscordConfigDefaults;
import rankednetwork.NetworkLevelling.PlayerExperience;
import rankednetwork.NetworkLevelling.PlayerLevelManager;

public final class Main extends JavaPlugin {

    private static Main main;


    @Override
    public void onEnable() {
        // ( locally testing 27/05/23 )
        main = this;
        new DiscordConfigDefaults();
        getCommand("booster").setExecutor(new BoosterCommand());
        BoosterManager.getInstance().initliazeStatisticsAvailableForBoosting();
        BoosterManager.getInstance().checkBoostersRunnable();
//        BoosterManager expBooster = new BoosterManager();
//        PlayerExperience experience = new PlayerExperience(Bukkit.getPlayer("Rewind"));
//        Booster<PlayerExperience> playerExpBooster = new Booster<>(Bukkit.getPlayer("Rewind"), experience, BoosterType.HALF, BoosterScope.PERSONAL);
//        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
//                .setColor(Color.AQUA)
//                .setTitle(playerExpBooster.getStatistic().getType() + " Booster Activation!")
//                .setThumbnail("https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
//                .setFooter("Thank you for the support!", "https://png.pngtree.com/png-clipart/20191120/original/pngtree-store-icon-in-line-style-png-image_5053711.jpg")
//                .setDescription(playerExpBooster.getPlayer().getName() + " has activated a " + playerExpBooster.getBoosterType().getBoostIncreasePercentage() + "% " + playerExpBooster.getStatistic().getName() + " Booster!");
//        expBooster.activateBooster(playerExpBooster);
//        expBooster.sendDiscordMessage("https://discord.com/api/webhooks/1111309366633709658/3z-lXUwJSodp3k9mb8Lsog897HxHlwd1rnaVATdGWvp7XIZM_So3eX7YK6Xddrmo88Tm", playerExpBooster, embedObject);

    }

    @Override
    public void onDisable() {

    }

    public static Main getMain() {
        return main;
    }
}
