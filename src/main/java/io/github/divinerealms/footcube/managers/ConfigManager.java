package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

/**
 * Manages configuration files for a Bukkit plugin.
 */
public class ConfigManager {

  @Getter
  private final Plugin plugin;
  private final String folderName;
  private FileConfiguration config;
  private File configFile;

  /**
   * Constructs a new ConfigManager instance.
   *
   * @param plugin     The plugin associated with this configuration manager.
   * @param folderName The name of the folder where configuration files are stored.
   */
  public ConfigManager(final Plugin plugin, final String folderName) {
    this.plugin = plugin;
    this.folderName = folderName;
  }

  /**
   * Creates a new configuration file, reloads it, and loads it with default values.
   *
   * @param name    The name of the configuration file.
   * @param header  The header to be added to the configuration file.
   * @param message A message to be logged after creating the configuration file.
   */
  public void createNewFile(final String name, final String header, final String message) {
    reloadConfig(name);
    saveConfig(name);
    loadConfig(header, name);

    if (message != null) {
      plugin.getLogger().info(message);
    }
  }

  /**
   * Gets the FileConfiguration for a specific configuration file.
   *
   * @param name The name of the configuration file.
   * @return The FileConfiguration associated with the specified configuration file.
   */
  public FileConfiguration getConfig(final String name) {
    if (config == null) {
      reloadConfig(name);
    }
    return config;
  }

  /**
   * Loads the configuration with default values and saves it.
   *
   * @param header The header to be added to the configuration file.
   * @param name   The name of the configuration file.
   */
  public void loadConfig(final String header, final String name) {
    config.options().header(header);
    config.options().copyDefaults(true);
    saveConfig(name);
  }

  /**
   * Reloads the configuration file.
   *
   * @param name The name of the configuration file.
   */
  public void reloadConfig(final String name) {
    if (configFile == null) {
      configFile = new File(plugin.getDataFolder() + folderName, name);
    }
    config = YamlConfiguration.loadConfiguration(configFile);
  }

  /**
   * Saves the configuration file.
   *
   * @param name The name of the configuration file.
   */
  public void saveConfig(final String name) {
    if (config == null || configFile == null) {
      plugin.getLogger().warning("Config or configFile is null. Unable to save config.");
      return;
    }

    try {
      getConfig(name).save(configFile);
    } catch (final IOException | IllegalArgumentException exception) {
      plugin.getLogger().severe("Could not save config to " + configFile);
      exception.printStackTrace();
    }
  }
}
