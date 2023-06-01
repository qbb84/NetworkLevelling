package rankednetwork.NetworkLevelling.Boosters.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rankednetwork.NetworkLevelling.Boosters.Booster;
import rankednetwork.NetworkLevelling.Boosters.BoosterScope;
import rankednetwork.NetworkLevelling.Boosters.BoosterType;
import rankednetwork.NetworkLevelling.NetworkStatistic;

public abstract class BoosterEvent extends Event {

	protected Player player;
	protected String boosterName;
	protected Double boosterPower;
	protected BoosterScope scope;
	protected BoosterType type;
	protected NetworkStatistic statistic;
	protected Booster<?> booster;
	protected Booster.Status status;

	public BoosterEvent(@NotNull Player player, @NotNull String boosterName, @NotNull Booster<?> booster) {
		this(player, boosterName, booster.getBoostAmount(), booster.getScope(), booster.getBoosterType(), booster.getStatistic(), booster.getStatus());
		this.booster = booster;
	}

	public BoosterEvent(@NotNull Player player, @NotNull String boosterName, @NotNull Double boosterPower,
						@NotNull BoosterScope scope, @NotNull BoosterType type, @NotNull NetworkStatistic statistic, Booster.Status status) {
		this.player = player;
		this.boosterName = boosterName;
		this.boosterPower = boosterPower;
		this.scope = scope;
		this.type = type;
		this.statistic = statistic;
		this.status = status;
	}

	public final @NotNull Player getPlayer() {
		return player;
	}

	public final @NotNull String getBoosterName() {
		return boosterName;
	}

	public final @NotNull Double getBoosterPower() {
		return boosterPower;
	}

	public final BoosterScope getScope() {
		return scope;
	}

	public final BoosterType getType() {
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
}

