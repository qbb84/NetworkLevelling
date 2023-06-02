package rankednetwork.NetworkLevelling;

import java.util.UUID;

public interface ExperienceChangeListener {
	void onExperienceChange(UUID playerUUID, int newValue);
}