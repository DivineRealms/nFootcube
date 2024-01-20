package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigManager {
  @Getter private final Plugin plugin;
  private final String folderName;
  private FileConfiguration config;
  private File configFile;

  public ConfigManager(final Plugin plugin, final String folderName) {
    this.plugin = plugin;
    this.folderName = folderName;
  }

  public void createNewFile(final String name, final String message, final String header) {
    reloadConfig(name);
    saveConfig(name);
    loadConfig(name, header);

    if (message != null) getPlugin().getLogger().info(message);
  }

  public FileConfiguration getConfig(final String name) {
    if (config == null) reloadConfig(name);
    return config;
  }

  public void loadConfig(final String header, final String name) {
    config.options().header(header);
    config.options().copyDefaults(true);
    saveConfig(name);
  }

  public void reloadConfig(final String name) {
    if (configFile == null) configFile = new File(plugin.getDataFolder() + folderName, name);
    config = YamlConfiguration.loadConfiguration(configFile);
  }

  public void saveConfig(final String name) {
    if (config == null || configFile == null) return;

    try {
      getConfig(name).save(configFile);
    } catch (final IOException | IllegalArgumentException exception ) {
      plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, exception);
    }
  }
}