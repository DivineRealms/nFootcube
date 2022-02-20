package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.Footcube;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class Configuration {
  private final Footcube plugin;
  private FileConfiguration configuration;
  private File configurationFile;

  public Configuration(final Footcube plugin, final String configFile) {
    this.plugin = plugin;
    this.saveDefault(configFile);
  }

  public FileConfiguration get(final String configFile) {
    if (this.configuration == null) this.reload(configFile);
    return this.configuration;
  }

  public void reload(final String configFile) {
    if (this.configurationFile == null) configurationFile = new File(this.plugin.getDataFolder(), configFile);
    this.configuration = YamlConfiguration.loadConfiguration(this.configurationFile);
    InputStream defaultStream = this.plugin.getResource(configFile);
    if (defaultStream != null) {
      YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
      this.configuration.setDefaults(defaultConfig);
    }
  }

  public void save(final String configFile) {
    if (this.configuration == null || this.configurationFile == null) return;
    try {
      this.get(configFile).save(this.configurationFile);
    } catch (IOException exception) {
      this.plugin.getLogger().log(Level.SEVERE, "Could not save file to " + this.configurationFile, exception);
    }
  }

  public void saveDefault(final String configFile) {
    if (this.configurationFile == null) this.configurationFile = new File(this.plugin.getDataFolder(), configFile);
    if (!this.configurationFile.exists()) this.plugin.saveResource(configFile, false);
  }
}
