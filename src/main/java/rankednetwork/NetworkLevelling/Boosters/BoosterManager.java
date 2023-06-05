package rankednetwork.NetworkLevelling.Boosters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import rankednetwork.Main;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterActivationEvent;
import rankednetwork.NetworkLevelling.Boosters.Events.BoosterQueueEvent;
import rankednetwork.NetworkLevelling.Config.Config;
import rankednetwork.NetworkLevelling.NetworkStatistic;
import rankednetwork.NetworkLevelling.Notifiers.DiscordNotifier;
import rankednetwork.NetworkLevelling.PlayerExperience;
import rankednetwork.NetworkLevelling.Webhooks.DiscordWebhook;

import java.util.*;
import java.util.function.Function;

import static rankednetwork.NetworkLevelling.EventManager.registerEvents;

/**
 * The BoosterManager class is a singleton that manages booster functionality in the plugin.
 * It provides methods to create, queue, check and manage boosters.
 */
public class BoosterManager {

	/**
	 * Singleton instance of the BoosterManager class.
	 * It's set to null initially and gets initialized via {@link #getInstance()} method.
	 */
	private static BoosterManager instance = null;
	/**
	 * Sorted set of all boosters available in the system, ordered by their duration.
	 * The order is defined by {@link BoosterDurationComparator}.
	 */
	private final SortedSet<Booster<? extends NetworkStatistic>> totalBoosters = new TreeSet<>(new BoosterDurationComparator());
	/**
	 * Sorted set of currently active boosters, ordered by their duration.
	 * The order is defined by {@link BoosterDurationComparator}.
	 */
	private final SortedSet<Booster<? extends NetworkStatistic>> activeBoosters = new TreeSet<>(new BoosterDurationComparator());
	/**
	 * A map storing the activation time of each booster.
	 * Each key is a booster, and its corresponding value is the time (as a long) at which it was activated.
	 */
	private final Map<Booster<? extends NetworkStatistic>, Long> boosterActivationTimes = new HashMap<>();
	/**
	 * Represents configuration related to created boosters.
	 * This configuration is persisted in the "created_boosters" file.
	 */
	private final Config createdBoosters = new Config(Main.getMain(), "created_boosters");
	/**
	 * Represents configuration related to player's boosters.
	 * This configuration is persisted in the "player_boosters_list" file.
	 */
	private final Config playerBoostersList = new Config(Main.getMain(), "player_boosters_list");
	/**
	 * Represents configuration related to booster queue.
	 * This configuration is persisted in the "booster_queue_cache" file.
	 */
	private final Config booster_cache = new Config(Main.getMain(), "booster_queue_cache");
	/**
	 * A map that stores a function for each type of network statistic.
	 * This function is responsible for creating an instance of that statistic for a given player.
	 * The key is a string which is the name of the statistic.
	 */
	private final Map<String, Function<UUID, NetworkStatistic>> nameToStatMap;
	/**
	 * Object to access functionality of the boosterQueue class.
	 */
	private final BoosterQueue boosterQueue = new BoosterQueue();


	/**
	 * Private constructor for singleton pattern.
	 */
	private BoosterManager() {
		nameToStatMap = new HashMap<>();
		nameToStatMap.put("XP", PlayerExperience::new);
		nameToStatMap.put("xp", PlayerExperience::new);
	}

	/**
	 * Static method to get the singleton instance of BoosterManager.
	 *
	 * @return the singleton instance of BoosterManager.
	 */
	public static BoosterManager getInstance() {
		if (instance == null) {
			instance = new BoosterManager();
		}
		return instance;
	}

	/**
	 * This method retrieves the config values for a custom booster given the booster's name.
	 *
	 * @param boosterName The name of the custom booster.
	 * @return A Pair containing the custom percentage and custom duration of the booster.
	 */
	public Pair<Double, Long> getConfigValuesForCustomBooster(String boosterName) {
		double customPercentage = createdBoosters.getConfig().getConfigurationSection(boosterName).getDouble("multiplier");
		long customDuration = createdBoosters.getConfig().getConfigurationSection(boosterName).getLong("duration");
		return new Pair<>(customPercentage, customDuration);
	}

	/**
	 * This method queues a booster.
	 *
	 * @param booster     The booster to be queued.
	 * @param player      The player who owns the booster.
	 * @param boosterName The name of the booster.
	 */
	public <T extends NetworkStatistic> void queueBooster(Booster<T> booster, Player player, String boosterName) {
		if (!checkIfPlayerHasBooster(player, boosterName)) return;

		if (booster.getScope().equals(BoosterScope.GLOBAL)) {
			for (Booster<?> boosters : boosterQueue.getGlobalBoosterQueue()) {
				if (boosters.getBoosterName().equalsIgnoreCase(booster.getBoosterName()) &&
						boosters.getOnlinePlayer() == player) {
					player.sendMessage("You can only queue one " + booster.getBoosterName() + " booster at a time!");
					return;
				}
			}
		} else {
			for (Booster<?> boosters : boosterQueue.getPersonalBoosterQueue()) {
				if (boosters.getOnlinePlayer().getUniqueId().equals(player.getUniqueId())) {
					player.sendMessage("You can only queue 1 personal booster at a time");
					return;
				}
			}
		}
		if (booster.getBoosterType() == BoosterType.CUSTOM) {
			Pair<Double, Long> customValues = getConfigValuesForCustomBooster(boosterName);
			booster.setMultiplier(customValues.first);
			booster.setDuration(customValues.second);
		} else {
			booster.setDuration(booster.getDuration());
			//Set multiplier
		}

		UUID playerUUID = player.getUniqueId();


		switch (booster.getScope()) {
			case PERSONAL:
				totalBoosters.add(booster);
				boolean wasPersonalQueueEmpty = boosterQueue.getPersonalBoosterQueue().isEmpty();
				boosterQueue.addBooster(booster);
				if (wasPersonalQueueEmpty) {
					booster.activate();
					registerEvents(new BoosterActivationEvent(player.getUniqueId(), boosterName, booster));
				} else {
					booster.setStatus(Booster.Status.INQUEUE);
					registerEvents(new BoosterQueueEvent(playerUUID, boosterName, booster));
				}
				break;
			case GLOBAL:
				totalBoosters.add(booster);
				boolean wasGlobalQueueEmpty = boosterQueue.getGlobalBoosterQueue().isEmpty();
				boosterQueue.addBooster(booster);
				if (wasGlobalQueueEmpty) {
					booster.activate();
					registerEvents(new BoosterActivationEvent(playerUUID, boosterName, booster));
				} else {
					booster.setStatus(Booster.Status.INQUEUE);
					registerEvents(new BoosterQueueEvent(playerUUID, boosterName, booster));
				}
				break;
			default:
				break;
		}

		removeBooster(player, boosterName, 1);
	}

	/**
	 * This method periodically checks if there's a new booster that should be activated.
	 */
	public void checkBoostersRunnable() {
		int delay = 1500;
		int period = 1500;
		Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getMain(), boosterQueue.checkAndActivateNextBooster(), delay, period);
	}

	/**
	 * This method sends a Discord message when a booster is activated.
	 *
	 * @param url         The webhook URL to send the Discord message to.
	 * @param booster     The activated booster.
	 * @param embedObject The embed object containing the details of the booster.
	 */
	public <T extends Booster<?>> void sendDiscordMessage(String url, T booster, DiscordWebhook.EmbedObject embedObject) {
		DiscordNotifier boosterNotifier = new DiscordNotifier(url);
		boosterNotifier.setEmbed(embedObject);
		boosterNotifier.sendNotification(booster);
	}

	/**
	 * This method checks if a booster has expired.
	 *
	 * @param booster The booster to check.
	 * @return A boolean representing whether the booster has expired.
	 */
	public boolean hasExpired(Booster<? extends NetworkStatistic> booster) {
		if (!boosterActivationTimes.containsKey(booster)) {
			throw new IllegalStateException("Booster hasn't been activated yet!");
		}
		long activationTime = boosterActivationTimes.get(booster);
		long currentTime = System.currentTimeMillis();
		return currentTime > (activationTime + booster.getDuration());
	}

	/**
	 * This method gives a player a booster.
	 *
	 * @param player      The player to give the booster to.
	 * @param boosterName The name of the booster.
	 * @param amount      The amount of the booster to give.
	 * @return The booster that was given to the player.
	 */
	public Booster<?> giveBooster(Player player, String boosterName, int amount) {
		if (!createdBoosters.getConfig().getKeys(true).contains(boosterName)) {
			player.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " doesn't exist!");
			return null;
		}

		UUID uuid = player.getUniqueId();
		String playerUniqueId = String.valueOf(uuid);

		ConfigurationSection section = playerBoostersList.getConfig().getConfigurationSection(playerUniqueId);
		if (section == null) {
			section = playerBoostersList.getConfig().createSection(playerUniqueId);
		}


		Player checkUUID = Bukkit.getPlayer(player.getUniqueId());
		if (!section.isSet("Player") || !section.get("Player").equals(checkUUID)) {
			section.set("Player_Name", checkUUID.getName());
		}

		ConfigurationSection boosterSection = section.getConfigurationSection("boosters");

		if (section.getConfigurationSection("boosters") == null) {
			boosterSection = section.createSection("boosters");
		}


		if (!boosterSection.isSet(boosterName)) {
			boosterSection.set(boosterName, amount);
		} else if (boosterSection.isSet(boosterName)) {
			boosterSection.set(boosterName, boosterSection.getInt(boosterName) + amount);
		}


		playerBoostersList.save();

		ConfigurationSection boostersCreatedList = createdBoosters.getConfig().getConfigurationSection(boosterName);
		String statisticName = (String) boostersCreatedList.get("statistic");
		BoosterType type = BoosterType.valueOf(boostersCreatedList.get("booster_type").toString());
		BoosterScope scope = BoosterScope.valueOf(boostersCreatedList.get("booster_scope").toString());

		NetworkStatistic statistic = getStatisticFromName(uuid, statisticName);

		return new Booster<>(uuid, statistic, type, scope, boosterName);
	}

	/**
	 * This method removes a booster from a player.
	 *
	 * @param player      The player to remove the booster from.
	 * @param boosterName The name of the booster to remove.
	 * @param amount      The amount of the booster to remove.
	 */
	public void removeBooster(Player player, String boosterName, int amount) {
		boolean boosterExists = createdBoosters.getConfig().getKeys(true).stream().anyMatch(key -> key.equalsIgnoreCase(boosterName));

		if (!boosterExists) {
			player.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " doesn't exist!");
			return;
		}

		ConfigurationSection section = playerBoostersList.getConfig().getConfigurationSection(player.getUniqueId().toString());
		ConfigurationSection boosterSection = section.getConfigurationSection("boosters");

		boolean playerHasBooster = boosterSection.getKeys(true).stream().anyMatch(key -> key.equalsIgnoreCase(boosterName));

		if (!playerHasBooster) {
			player.sendMessage(ChatColor.RED + " Error: the player " + ChatColor.BOLD + player.getName() + ChatColor.RED + " doesn't have a " + ChatColor.BOLD + boosterName +
					ChatColor.RED + " booster");
			return;
		}

		String properBoosterName = boosterSection.getKeys(false).stream()
				.filter(key -> key.equalsIgnoreCase(boosterName))
				.findFirst()
				.get();


		int amountTaken = boosterSection.getInt(properBoosterName) - amount;

		if (amountTaken > 0) {
			boosterSection.set(properBoosterName, amountTaken);
		} else {
			boosterSection.set(properBoosterName, null);
		}

		playerBoostersList.save();
	}

	/**
	 * This method creates a new custom booster.
	 *
	 * @param boosterName  The name of the new booster.
	 * @param statistic    The statistic that the booster affects.
	 * @param boosterScope The scope of the booster.
	 * @param boosterType  The type of the booster.
	 * @param multiplier   The multiplier of the booster.
	 * @param duration     The duration of the booster.
	 * @param creator      The player who created the booster.
	 */
	public void create(String boosterName, String statistic, BoosterScope boosterScope, BoosterType boosterType, double multiplier, long duration, Player creator) {

		ConfigurationSection bSection = createdBoosters.getConfig().getConfigurationSection(boosterName);
		if (bSection == null) {
			bSection = createdBoosters.getConfig().createSection(boosterName);
		} else if (bSection.getName().equalsIgnoreCase(boosterName)) {
			creator.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " already exists!");
			return;
		}


		Map<String, Object> defaults = new LinkedHashMap<>();
		defaults.put("statistic", statistic.toUpperCase());
		defaults.put("booster_scope", boosterScope.name());
		defaults.put("booster_type", boosterType.name());
		defaults.put("multiplier", multiplier);
		defaults.put("duration", duration);


		for (Map.Entry<String, Object> entries : defaults.entrySet()) {
			if (!bSection.isSet(entries.getKey())) {
				bSection.set(entries.getKey(), entries.getValue());
			}
		}

		createdBoosters.save();
		creator.sendMessage(ChatColor.GRAY + "Succesfully created a " + ChatColor.AQUA + ChatColor.BOLD +
				boosterName + ChatColor.GRAY + " booster!");

	}

	/**
	 * This method creates a new booster with Enum values from BoosterType.
	 *
	 * @param boosterName  The name of the new booster.
	 * @param statistic    The statistic that the booster affects.
	 * @param boosterScope The scope of the booster.
	 * @param boosterType  The type of the booster.
	 * @param creator      The player who created the booster.
	 * @see BoosterType
	 */
	public void create(String boosterName, String statistic, BoosterScope boosterScope, BoosterType boosterType, Player creator) {

		ConfigurationSection bSection = createdBoosters.getConfig().getConfigurationSection(boosterName);
		if (bSection == null) {
			bSection = createdBoosters.getConfig().createSection(boosterName);
		} else if (bSection.getName().equalsIgnoreCase(boosterName)) {
			creator.sendMessage(ChatColor.RED + " Error: the booster " + ChatColor.BOLD + boosterName + ChatColor.RED + " already exists!");
			return;
		}

		Map<String, Object> defaults = new LinkedHashMap<>();
		defaults.put("statistic", statistic.toUpperCase());
		defaults.put("booster_scope", boosterScope.name());
		defaults.put("booster_type", boosterType.name());
		defaults.put("multiplier", boosterType.getBoostIncreasePercentage());
		defaults.put("duration", boosterType.getBoosterTime());


		for (Map.Entry<String, Object> entries : defaults.entrySet()) {
			if (!bSection.isSet(entries.getKey())) {
				bSection.set(entries.getKey(), entries.getValue());
			}
		}

		createdBoosters.save();
		creator.sendMessage(ChatColor.GRAY + "Succesfully created a " + ChatColor.AQUA + ChatColor.BOLD +
				boosterName + ChatColor.GRAY + " booster!");

	}

	/**
	 * This method displays all available boosters.
	 *
	 * @param player The player to display the boosters to.
	 * @param sender The command executor.
	 */
	public Map<String, String> viewBoostersOfPlayer(Player player, Player sender) {
		Map<String, String> boosterList = new LinkedHashMap<>();
		if (playerBoostersList.getConfig().getKeys(true).contains(player.getUniqueId().toString())) {
			ConfigurationSection boosterSection = playerBoostersList.getConfig().getConfigurationSection(player.getUniqueId().toString()).getConfigurationSection("boosters");
			assert boosterSection != null;
			for (Map.Entry<String, Object> boosters : boosterSection.getValues(true).entrySet()) {
				boosterList.put(boosters.getKey(), boosters.getValue().toString());

			}
			sender.sendMessage(boosterList.toString().replace('=', ':').replace('{', ' ').replace('}', ' '));

		}
		return boosterList;
	}

	/**
	 * This method retrieves a NetworkStatistic given a name.
	 *
	 * @param player The player associated with the statistic.
	 * @param name   The name of the NetworkStatistic.
	 * @return The corresponding NetworkStatistic.
	 * @throws IllegalArgumentException if there is no statistic associated with the given name.
	 */
	@NotNull
	public NetworkStatistic getStatisticFromName(UUID player, @NotNull String name) {
		Function<UUID, NetworkStatistic> constructor = nameToStatMap.get(name);
		if (constructor != null) {
			return constructor.apply(player);
		} else {
			throw new IllegalArgumentException("No statistic found for name: " + name + " see method: getStatisticFromName");
		}
	}

	/**
	 * This method retrieves a Booster for a player given a name.
	 *
	 * @param player      The player to retrieve the booster for.
	 * @param boosterName The name of the booster.
	 * @return The corresponding Booster, or null if the player does not have the booster.
	 */
	public Booster<?> getBoosterForPlayer(@NotNull Player player, String boosterName) {
		UUID uuid = player.getUniqueId();
		String playerUniqueId = String.valueOf(uuid);
		ConfigurationSection playerSection = playerBoostersList.getConfig().getConfigurationSection(playerUniqueId);
		if (playerSection == null) {
			// The player doesn't have any boosters todo
			return null;
		}


		ConfigurationSection boosterSection = playerSection.getConfigurationSection("boosters");

		if (boosterSection == null || !boosterSection.isSet(boosterName)) {
			// The player doesn't have this booster
			return null;
		}

		// Get the data to recreate the Booster object
		ConfigurationSection boostersCreatedList = createdBoosters.getConfig().getConfigurationSection(boosterName);
		String statisticName = (String) boostersCreatedList.get("statistic");
		BoosterType type = BoosterType.valueOf(boostersCreatedList.get("booster_type").toString());
		BoosterScope scope = BoosterScope.valueOf(boostersCreatedList.get("booster_scope").toString());

		NetworkStatistic statistic = getStatisticFromName(uuid, statisticName);

		return new Booster<>(uuid, statistic, type, scope, boosterName);
	}

	/**
	 * This method initializes the list of statistics that are available for boosting.
	 * <p>
	 * {@link NetworkStatistic#addStatistic(NetworkStatistic)}
	 */
	public void initliazeStatisticsAvailableForBoosting() {
		NetworkStatistic.addStatistic(new PlayerExperience());
	}

	/**
	 * This method returns the total number of boosters.
	 *
	 * @return The total number of boosters.
	 */
	public int boosterCount() {
		return totalBoosters.size();
	}

	/**
	 * This method checks if a player has a specific booster.
	 *
	 * @param player      The player to check.
	 * @param boosterName The name of the booster.
	 * @return true if the player has the booster, false otherwise.
	 */
	public boolean checkIfPlayerHasBooster(Player player, String boosterName) {
		ConfigurationSection section = playerBoostersList.getConfig().getConfigurationSection(player.getUniqueId().toString());
		ConfigurationSection boosterSection = section.getConfigurationSection("boosters");

		boolean playerHasBooster = boosterSection.getKeys(true).stream().anyMatch(key -> key.equalsIgnoreCase(boosterName));

		if (!playerHasBooster) {
			player.sendMessage(ChatColor.RED + " Error: the player " + ChatColor.BOLD + player.getName() + ChatColor.RED + " doesn't have a " + ChatColor.BOLD + boosterName +
					ChatColor.RED + " booster");
			return false;
		}
		return true;
	}

	/**
	 * This method displays the booster queue to a player.
	 *
	 * @param player The player to display the booster queue to.
	 */
	public void displayBoosterQueue(Player player) {
		boosterQueue.displayBoosterQueue(player);
	}

	/**
	 * This method retrieves the activation times for all boosters.
	 *
	 * @return A map of boosters and their activation times.
	 */
	public Map<Booster<? extends NetworkStatistic>, Long> getBoosterActivationTimes() {
		return boosterActivationTimes;
	}

	/**
	 * This method retrieves the list of created boosters.
	 *
	 * @return The Config of created boosters.
	 */
	public Config getCreatedBoosters() {
		return createdBoosters;
	}

	/**
	 * This method retrieves the list of boosters for each player.
	 *
	 * @return The Config of player boosters.
	 */
	public Config getPlayerBoostersList() {
		return playerBoostersList;
	}

	/**
	 * This method saves the current state of the booster queues.
	 */
	public void saveBoosterQueues() {
		List<String> globalBoosterQueueAsString = new LinkedList<>();
		List<String> personalBoosterQueueAsString = new LinkedList<>();

		for (Booster<?> booster : boosterQueue.getGlobalBoosterQueue()) {
			globalBoosterQueueAsString.add(booster.toString());
		}
		for (Booster<?> booster : boosterQueue.getPersonalBoosterQueue()) {
			personalBoosterQueueAsString.add(booster.toString());
		}

		booster_cache.getConfig().set("globalQueue", globalBoosterQueueAsString);
		booster_cache.getConfig().set("personalQueue", personalBoosterQueueAsString);
		booster_cache.save();
	}

	/**
	 * This method gets the total boosters.
	 *
	 * @return the set containing a copy of the total boosters
	 */
	public SortedSet<Booster<? extends NetworkStatistic>> getTotalBoosters() {
		return totalBoosters;
	}

	/**
	 * This method get the total active boosters
	 *
	 * @return the set containing a copy of the active boosters
	 */
	public SortedSet<Booster<? extends NetworkStatistic>> getActiveBoosters() {
		return activeBoosters;
	}

	/**
	 * This method loads the booster queues from the booster cache.
	 *
	 * @see Booster#fromString(String)
	 * @see Booster#toString()
	 */
	public void loadBoosterQueue() {
		List<String> globalBoosterQueueAsString = booster_cache.getConfig().getStringList("globalQueue");
		List<String> personalBoosterQueueAsString = booster_cache.getConfig().getStringList("personalQueue");

		for (String boosterAsString : globalBoosterQueueAsString) {
			boosterQueue.getGlobalBoosterQueue().add(Booster.fromString(boosterAsString));
			totalBoosters.add(Booster.fromString(boosterAsString));
			if (Booster.fromString(boosterAsString).isActive()) {
				activeBoosters.add(Booster.fromString(boosterAsString));
			}

		}
		for (String boosterAsString : personalBoosterQueueAsString) {
			boosterQueue.getPersonalBoosterQueue().add(Booster.fromString(boosterAsString));
			totalBoosters.add(Booster.fromString(boosterAsString));
			if (Booster.fromString(boosterAsString).isActive()) {
				activeBoosters.add(Booster.fromString(boosterAsString));
			}
		}

	}
}
