package rankednetwork.NetworkLevelling;

import org.bukkit.entity.Player;

@BoosterMetadata(name = "XP")
public class PlayerExperience extends NetworkStatistic {


	private Player player;
	PlayerLevelManager levelManager;


	public PlayerExperience(){
		this(null);
	}

	public PlayerExperience(Player player) {
		super();
		this.player = player;
		this.levelManager = new PlayerLevelManager();
	}

	@Override
	public int getValue(Player player) {
		return super.value;
	}

	@Override
	public void setValue(Player player, int value) {

	}

	@Override
	public void changeValue(Player player, int delta) {

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
		return null;
	}




}
