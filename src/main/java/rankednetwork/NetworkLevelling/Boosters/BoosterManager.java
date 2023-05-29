package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import rankednetwork.NetworkLevelling.Notifiers.DiscordNotifier;
import rankednetwork.NetworkLevelling.Notifiers.GameNotifier;
import rankednetwork.NetworkLevelling.PlayerExperience;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.awt.*;
import java.util.*;

/**
 * Singleton class used for managing Boosters
 */
public class BoosterManager {

	private Booster<? extends NetworkStatistic> activeBooster;

	private  SortedSet<Booster<? extends NetworkStatistic>> totalBoosters = new TreeSet<>(new BoosterDurationComparator());
	private  SortedSet<Booster<? extends NetworkStatistic>> activeBoosters = new TreeSet<>(new BoosterDurationComparator());

	private Map<Booster<? extends NetworkStatistic>, Long> boosterActivationTimes = new HashMap<>();

	private final Config createdBoosters = new Config(Main.getMain(), "created_boosters");
	private final Config playerBoostersList = new Config(Main.getMain(), "player_boosters_list");


//will move this to booster queueTM
	public enum Status{

		ACTIVE(Color.GREEN + "ACTIVE"),
		INACTIVE(Color.RED + "INACTIVE");
//		CAN_BOOST,
//		BOOSTER_COUNT,
//		GET_ACTIVE_BOOSTERS,
//		GET_TOTAL_BOOSTERS;
// TODO Implement

		  final String statusName;

		Status(String statusName){
			this.statusName = statusName;
		}

	}

	public static BoosterManager getInstance(){
		return new BoosterManager();
	}

	public <T extends NetworkStatistic> void activateBooster(Booster<T> booster) {
		activeBooster = booster;
		GameNotifier gameNotifier = new GameNotifier();

		switch (booster.getScope()){
			case PERSONAL:
				gameNotifier.sendActivatedMessage(booster);
				totalBoosters.add(booster);
				break;
			case GLOBAL:
				//Add personal
				//set Active boosters, setup queue
				break;
			default:
		}

	}

	public void setDiscordSettingDefaults(){

	}

	public <T extends  Booster<?>>void sendDiscordMessage(String url, T booster, DiscordWebhook.EmbedObject embedObject) {
		DiscordNotifier boosterNotifier = new DiscordNotifier(url);
		boosterNotifier.setEmbed(embedObject);
		boosterNotifier.sendActivatedMessage(booster);
	}

	public boolean hasExpired(Booster<? extends NetworkStatistic> booster) {
		if (!boosterActivationTimes.containsKey(booster)) {
			throw new IllegalStateException("Booster hasn't been activated yet!");
		}
		long activationTime = boosterActivationTimes.get(booster);
		long currentTime = System.currentTimeMillis();
		return currentTime > (activationTime + booster.getDuration());
	}

	public Booster<?> giveBooster(Player player, String boosterName, int amount) {
		if (!createdBoosters.getConfig().getKeys(true).contains(boosterName)) {
			player.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " doesn't exist!");
			return null;
		}

		String playerUniqueId = String.valueOf(player.getUniqueId());

		ConfigurationSection section = playerBoostersList.getConfig().getConfigurationSection(playerUniqueId);
		if(section == null){
			section = playerBoostersList.getConfig().createSection(playerUniqueId);
		}



		Player checkUUID = Bukkit.getPlayer(player.getUniqueId());
		if(!section.isSet("Player") || !section.get("Player").equals(checkUUID)){
			section.set("Player_Name", checkUUID.getName());
		}

		ConfigurationSection boosterSection = section.getConfigurationSection("boosters");

		if(section.getConfigurationSection("boosters") == null){
			boosterSection = section.createSection("boosters");
		}



		if(!boosterSection.isSet(boosterName)){
			boosterSection.set(boosterName, amount);
		}else if(boosterSection.isSet(boosterName)){
			boosterSection.set(boosterName, boosterSection.getInt(boosterName)+amount);
		}


		playerBoostersList.save();

		ConfigurationSection boostersCreatedList = createdBoosters.getConfig().getConfigurationSection(boosterName);
		String statisticName = (String) boostersCreatedList.get("statistic");
		BoosterType type = BoosterType.valueOf(boostersCreatedList.get("booster_type").toString());
		BoosterScope scope = BoosterScope.valueOf(boostersCreatedList.get("booster_scope").toString());

		NetworkStatistic statistic = getStatisticFromName(player, statisticName);

		return new Booster<>(player, statistic, type, scope);
	}

	public void removeBooster(Player player, String boosterName, int amount){
		boolean boosterExists = createdBoosters.getConfig().getKeys(true).stream().anyMatch(key -> key.equalsIgnoreCase(boosterName));

		if (!boosterExists) {
			player.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " doesn't exist!");
			return;
		}

		ConfigurationSection section = playerBoostersList.getConfig().getConfigurationSection(player.getUniqueId().toString());
		ConfigurationSection boosterSection = section.getConfigurationSection("boosters");

		boolean playerHasBooster = boosterSection.getKeys(true).stream().anyMatch(key -> key.equalsIgnoreCase(boosterName));

		if(!playerHasBooster){
			player.sendMessage(ChatColor.RED + " Error: the player " + ChatColor.BOLD + player.getName()  + ChatColor.RED + " doesn't have a " + ChatColor.BOLD + boosterName +
					ChatColor.RED + " booster");
			return;
		}

		String properBoosterName = boosterSection.getKeys(false).stream()
				.filter(key -> key.equalsIgnoreCase(boosterName))
				.findFirst()
				.get();


		int amountTaken = boosterSection.getInt(properBoosterName) - amount;

		if(amountTaken > 0){
			boosterSection.set(properBoosterName, amountTaken);
		}else{
			boosterSection.set(properBoosterName, null);
		}

		playerBoostersList.save();
	}

	@NotNull
	public NetworkStatistic getStatisticFromName(Player player, String name){
		switch (name){
			case "XP": return new PlayerExperience(player);
			default: throw new IllegalArgumentException("No statistic found for name: " + name + " see method: getStatisticFromName");
		}
	}

	public void create(String boosterName, String statistic, BoosterScope boosterScope, double multiplier, long duration, Player creator) {

		ConfigurationSection bSection = createdBoosters.getConfig().getConfigurationSection(boosterName);
		if(bSection == null) {
			bSection = createdBoosters.getConfig().createSection(boosterName);
		}else if (bSection.getName().equalsIgnoreCase(boosterName)) {
			creator.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " already exists!");
			return;
		}



		Map<String, Object> defaults = new LinkedHashMap<>();
		defaults.put("statistic", statistic);
		defaults.put("booster_scope", boosterScope.name());
		defaults.put("booster_type", BoosterType.CUSTOM.name());
		defaults.put("multiplier", multiplier);
		defaults.put("duration", duration);

		for(Map.Entry<String, Object> entries : defaults.entrySet()){
			if(!bSection.isSet(entries.getKey())){
				bSection.set(entries.getKey(), entries.getValue());
			}
		}

		createdBoosters.save();
		creator.sendMessage(ChatColor.GRAY + "Succesfully created a " + ChatColor.AQUA + ChatColor.BOLD +
				boosterName + ChatColor.GRAY +  " booster!");

	}

	public void create(String boosterName, String statistic, BoosterScope boosterScope, BoosterType boosterType, Player creator) {

		ConfigurationSection bSection = createdBoosters.getConfig().getConfigurationSection(boosterName);
		if(bSection == null) {
			bSection = createdBoosters.getConfig().createSection(boosterName);
		}else if (bSection.getName().equalsIgnoreCase(boosterName)) {
			creator.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " already exists!");
			return;
		}

		Map<String, Object> defaults = new LinkedHashMap<>();
		defaults.put("statistic", statistic);
		defaults.put("booster_scope", boosterScope.name());
		defaults.put("booster_type", boosterType.name());
		defaults.put("multiplier", boosterType.getBoostIncreasePercentage());
		defaults.put("duration", boosterType.getBoosterTime());


		for(Map.Entry<String, Object> entries : defaults.entrySet()){
			if(!bSection.isSet(entries.getKey())){
				bSection.set(entries.getKey(), entries.getValue());
			}
		}

		createdBoosters.save();
		creator.sendMessage(ChatColor.GRAY + "Succesfully created a " + ChatColor.AQUA + ChatColor.BOLD +
				boosterName + ChatColor.GRAY +  " booster!");

	}

	public Map<String, String> viewBoostersOfPlayer(Player player, Player sender){
		Map<String, String> boosterList = new LinkedHashMap<>();
			if(playerBoostersList.getConfig().getKeys(true).contains(player.getUniqueId().toString())){
				ConfigurationSection boosterSection = playerBoostersList.getConfig().getConfigurationSection(player.getUniqueId().toString()).getConfigurationSection("boosters");
				assert boosterSection != null;
					for(Map.Entry<String, Object> boosters : boosterSection.getValues(true).entrySet()){
						boosterList.put(boosters.getKey(), boosters.getValue().toString());

					}
				sender.sendMessage(boosterList.toString().replace('=', ':').replace('{', ' ').replace('}', ' '));

			}
		return boosterList;
	}



	public void initliazeStatisticsAvailableForBoosting(){
		NetworkStatistic.addStatistic(new PlayerExperience());
	}

	public int boosterCount(){
		return totalBoosters.size();
	}






}