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
  @Getter @Setter private File file;
  @Getter @Setter private FileConfiguration configuration;

  public ConfigManager(final Plugin plugin, final String name) {
    this.plugin = plugin;
    this.file = new File(plugin.getDataFolder(), name);
    saveDefaultConfig(name);
  }

  public void reloadConfig(final String name) {
    if (getFile() == null) setFile(new File(getPlugin().getDataFolder(), name));
    setConfiguration(YamlConfiguration.loadConfiguration(getFile()));
  }

  public FileConfiguration getConfig(final String name) {
    if (getConfiguration() == null) reloadConfig(name);
    return getConfiguration();
  }

  public void saveDefaultConfig(final String name) {
    if (getFile() == null) setFile(new File(getPlugin().getDataFolder(), name));
    if (!getFile().exists()) getPlugin().saveResource(name, false);
  }

  public void saveConfig(final String name) {
    try {
      getConfig(name).save(getFile());
    } catch (final IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }

  public static String getNotFound(final String path, final String file) {
    return "&cString \"&e%path%&c\" in \"&4%file%&c\" not found!"
        .replace("%path%", path)
        .replace("%file%", file)
        .replace('&', '\u00a7');
  }
}
