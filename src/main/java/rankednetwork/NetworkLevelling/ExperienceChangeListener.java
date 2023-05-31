package rankednetwork.NetworkLevelling;

import org.bukkit.entity.Player;

public interface ExperienceChangeListener {
	void onExperienceChange(Player player, int newValue);
}