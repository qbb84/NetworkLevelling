package rankednetwork.NetworkLevelling;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@BoosterMetadata(name = "XP")
public class PlayerExperience extends NetworkStatistic  {


	private Player player;
	PlayerLevelManager levelManager;

	private List<ExperienceChangeListener> listeners = new ArrayList<>();

	public void addExperienceChangeListener(ExperienceChangeListener listener) {
		this.listeners.add(listener);
	}


	public PlayerExperience(){
		this(null);
	}

	public PlayerExperience(Player player) {
		super(player);
		this.player = player;
		this.levelManager = PlayerLevelManager.getInstance();
	}

	@Override
	public int getValue(Player player) {
		return this.value;
	}

	@Override
	public void setValue(Player player, int value) {
		this.value = value;
		notifyExperienceChange(player, value);
	}

	@Override
	public void changeValue(Player player, int delta) {
		this.value += delta;
		notifyExperienceChange(player, getValue(player));
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String setName(String name) {
		return this.name = name;
	}

	@Override
	public String setType(String type) {
		return this.type = type;
	}

	private void notifyExperienceChange(Player player, int newValue) {
		for (ExperienceChangeListener listener : listeners) {
			listener.onExperienceChange(player, newValue);
		}
	}




}
