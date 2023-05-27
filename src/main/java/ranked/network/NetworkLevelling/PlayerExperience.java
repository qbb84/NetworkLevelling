package ranked.network.NetworkLevelling;

import org.bukkit.entity.Player;

public class PlayerExperience extends NetworkStatistic {

	private Player player;
	PlayerLevelManager levelManager;
	private String name = "XP";
	private String type = "Experience";


	public PlayerExperience(Player player) {
		super(0);
		this.player = player;
		this.levelManager = new PlayerLevelManager();
		name = "XP";
		type = "Experience";
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
