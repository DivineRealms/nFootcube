package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import lombok.Setter;
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
  @Getter private final File folder;
  @Getter private final boolean singular;
  @Getter @Setter private File file;
  @Getter @Setter private FileConfiguration configuration;

  public ConfigManager(final Plugin plugin, final String name, final boolean singular) {
    this.plugin = plugin;
    this.configName = name;
    this.singular = singular;
    this.folder = new File(plugin.getDataFolder() + File.separator + "playerdata");
    if (!singular && !getFolder().exists()) getFolder().mkdir();
    this.file = singular ? new File(plugin.getDataFolder(), name) : new File(folder, name);
    if (singular) saveDefaultConfig(name);
    else saveConfig(name);
    reloadConfig(name);
  }

  public void reloadConfig(final String name) {
    if (getFile() == null) setFile(isSingular() ? new File(getPlugin().getDataFolder(), name) : new File(getFolder(), name));
    setConfiguration(YamlConfiguration.loadConfiguration(getFile()));
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
    if (getFile() == null) setFile(new File(getPlugin().getDataFolder(), name));
    if (!getFile().exists()) getPlugin().saveResource(name, false);
  }
}
