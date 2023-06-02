package rankednetwork.NetworkLevelling.Events;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class LevelEvent extends Event implements EventBase {

	protected UUID playerUUID;
	protected int playerLevel;
	protected int playerExperience;
	protected LocalDateTime creationTime;


	public LevelEvent(@NotNull UUID playerUUID, int playerLevel, int playerExperience) {
		this.playerUUID = playerUUID;
		this.playerLevel = playerLevel;
		this.playerExperience = playerExperience;
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

	@Override
	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public int getPlayerExperience() {
		return playerExperience;
	}

	@Override
	public LocalDateTime getCreationTime() {
		return this.creationTime;
	}
}


