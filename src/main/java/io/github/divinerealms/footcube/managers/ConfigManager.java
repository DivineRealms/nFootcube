package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

@SuppressWarnings({"unused"})
public class ConfigManager {
  @Getter private final Plugin plugin;
  @Getter private final String configName;
  @Getter private File file;
  @Getter private FileConfiguration configuration;

  public ConfigManager(final Plugin plugin, final String name, final boolean singular) {
    this.plugin = plugin;
    this.configName = name;
    this.file = new File(plugin.getDataFolder(), name);
    if (singular) saveDefaultConfig(name);
    else saveConfig(name);
    reloadConfig(name);
  }

  public void reloadConfig(final String name) {
    if (getFile() == null) this.file = new File(getPlugin().getDataFolder(), name);
    this.configuration = YamlConfiguration.loadConfiguration(getFile());
  }

  public FileConfiguration getConfig(final String name) {
    if (getConfiguration() == null) reloadConfig(name);
    return getConfiguration();
  }

  public void saveConfig(final String name) {
    try {
      getConfig(name).save(getFile());
    } catch (IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }

  public void saveDefaultConfig(final String name) {
    if (getFile() == null) this.file = new File(getPlugin().getDataFolder(), name);
    if (!getFile().exists()) getPlugin().saveResource(name, false);
  }
}
