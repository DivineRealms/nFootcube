package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class ConfigManager {
  @Getter private final Plugin plugin;
  @Getter private File file;
  @Getter private String configName;
  @Getter private FileConfiguration configuration;

  public ConfigManager(final Plugin plugin) {
    this.plugin = plugin;
  }

  public void reloadConfig(final String configName) {
    if (getFile() == null) file = new File(getPlugin().getDataFolder(), configName);
    configuration = YamlConfiguration.loadConfiguration(getFile());
  }

  public FileConfiguration getConfig(final String configName) {
    if (getConfiguration() == null) reloadConfig(configName);
    return getConfiguration();
  }

  public void saveConfig(final String configName) {
    try {
      getConfig(configName).save(getFile());
    } catch (IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }

  public void saveDefaultConfig(final String configName) {
    if (getFile() == null) file = new File(getPlugin().getDataFolder(), configName);
    if (!getFile().exists()) getPlugin().saveResource(configName, false);
  }

  public void createNewFile(final String configName, final boolean replaceExisting) {
    try {
      if (getFile().exists() && replaceExisting) getFile().delete();
      getFile().createNewFile();
    } catch (IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }
}
