package rankednetwork.NetworkLevelling.Config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The Config class provides an interface for managing a YamlConfiguration file.
 */
public class Config {

	private final File file;
	private final YamlConfiguration config;

	/**
	 * Constructs a Config instance for a specific configuration file.
	 *
	 * @param plugin     the JavaPlugin instance
	 * @param configName the name of the configuration file (without extension)
	 */
	public Config(JavaPlugin plugin, String configName) {
		this.file = new File(plugin.getDataFolder(), configName + ".yml");
		this.config = new YamlConfiguration();
		createConfig();
	}

	/**
	 * Creates and loads the configuration file if it does not exist, else it loads the existing one.
	 */
	private void createConfig() {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			config.load(file);
			config.save(file);


		} catch (IOException | InvalidConfigurationException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Returns the name of the configuration file.
	 *
	 * @return the name of the configuration file
	 */
	public String getConfigName() {
		return file.getName();
	}

	/**
	 * Returns the YamlConfiguration instance.
	 *
	 * @return the YamlConfiguration instance
	 */
	public YamlConfiguration getConfig() {
		return config;
	}

	/**
	 * Saves the configuration to the file.
	 */
	public void save() {
		try {
			getConfig().save(file.getPath());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
