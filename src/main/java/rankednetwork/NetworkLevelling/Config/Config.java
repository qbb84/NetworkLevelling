package rankednetwork.NetworkLevelling.Config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

	private final File file;
	private final YamlConfiguration config;

	public Config(JavaPlugin plugin, String configName) {
		this.file = new File(plugin.getDataFolder(), configName + ".yml");
		this.config = new YamlConfiguration();
		createConfig();
	}

	private void createConfig() {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}

			config.load(file);
			config.save(file);


		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String getConfigName() {
		return file.getName();
	}

	public YamlConfiguration getConfig() {
		return config;
	}


	public void save() {
		try {
			getConfig().save(file.getPath());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
