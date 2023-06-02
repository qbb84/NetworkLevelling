package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.Events.EventBase;
import rankednetwork.NetworkLevelling.NetworkStatistic;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BoosterEvent extends Event implements EventBase {

	protected final LocalDateTime creationTime;
	protected UUID playerUUID;
	protected String boosterName;
	protected Double boosterPower;
	protected BoosterScope scope;
	protected BoosterType type;
	protected NetworkStatistic statistic;
	protected Booster<?> booster;
	protected Booster.Status status;

	public BoosterEvent(@NotNull UUID uuid, @NotNull String boosterName, @NotNull Booster<?> booster) {
		this(uuid, boosterName, booster.getBoostAmount(), booster.getScope(), booster.getBoosterType(), booster.getStatistic(), booster.getStatus());
		this.booster = booster;
	}

	public BoosterEvent(@NotNull UUID playerUUID, @NotNull String boosterName, @NotNull Double boosterPower,
						@NotNull BoosterScope scope, @NotNull BoosterType type, @NotNull NetworkStatistic statistic, Booster.Status status) {
		this.playerUUID = playerUUID;
		this.boosterName = boosterName;
		this.boosterPower = boosterPower;
		this.scope = scope;
		this.type = type;
		this.statistic = statistic;
		this.status = status;
		this.creationTime = LocalDateTime.now();
	}

	/**
	 * Returns the Player object for this Booster if the player is currently online.
	 * <p>
	 * This method will return null if the player is not currently online on the server.
	 * <p>
	 * Note: This method can return null even if {@link #getOfflinePlayer()} returns a non-null OfflinePlayer,
	 * because a Player object can only represent a player who is currently online, while an OfflinePlayer
	 * object can represent a player who may be offline.
	 *
	 * @return The Player object for the player if they're online, or null if they're not.
	 */
	@Nullable
	public Player getOnlinePlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	/**
	 * Returns an OfflinePlayer object for this Booster's player.
	 * <p>
	 * This method will always return a non-null OfflinePlayer object, even if the player is not currently online
	 * on the server. An OfflinePlayer object can represent a player who may be offline.
	 * <p>
	 * Note: This method can return a non-null OfflinePlayer even if {@link #getOnlinePlayer()} returns null,
	 * because an OfflinePlayer object can represent a player who may be offline, while a Player object
	 * can only represent a player who is currently online.
	 *
	 * @return The OfflinePlayer object for the player.
	 */
	@NotNull
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(playerUUID);
	}

	public final @NotNull String getBoosterName() {
		return boosterName;
	}

	public final @NotNull Double getBoosterPower() {
		return boosterPower;
	}

	public final @NotNull BoosterScope getScope() {
		return scope;
	}

	@Override
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public final @NotNull BoosterType getType() {
		return type;
	}

	public final @NotNull NetworkStatistic getStatistic() {
		return statistic;
	}

	public final @NotNull Booster.Status getStatus() {
		return status;
	}

	@Nullable
	@Deprecated
	public Booster<?> getBooster() throws NullPointerException {
		return booster;
	}


	@Override
	public LocalDateTime getCreationTime() {
		return this.creationTime;
	}
}

