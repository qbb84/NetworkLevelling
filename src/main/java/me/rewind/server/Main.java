package me.rewind.server;

import me.rewind.server.NetworkLevelling.Boosters.Booster;
import me.rewind.server.NetworkLevelling.Boosters.BoosterManager;
import me.rewind.server.NetworkLevelling.Boosters.BoosterScope;
import me.rewind.server.NetworkLevelling.Boosters.BoosterType;
import me.rewind.server.NetworkLevelling.PlayerExperience;
import me.rewind.server.NetworkLevelling.Webhooks.DiscordWebhook;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Random;

public final class Main extends JavaPlugin {



    @Override
    public void onEnable() {
        // Plugin startup logic
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
        expBooster.sendDiscordMessage("https://discord.com/api/webhooks/1111309366633709658/3z-lXUwJSodp3k9mb8Lsog897HxHlwd1rnaVATdGWvp7XIZM_So3eX7YK6Xddrmo88Tm", playerExpBooster, embedObject);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
