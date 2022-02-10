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
  private FileConfiguration messages;
  private File file;

  public Configuration(Footcube plugin) {
    this.plugin = plugin;
    this.saveDefaultMessages();
  }

  public FileConfiguration get() {
    if (this.messages == null) this.reloadMessages();
    return messages;
  }

  public void reloadMessages() {
    if (this.file == null) file = new File(this.plugin.getDataFolder(), "messages.yml");
    messages = YamlConfiguration.loadConfiguration(this.file);
    InputStream defaultStream = this.plugin.getResource("messages.yml");
    if (defaultStream != null) {
      YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
      this.messages.setDefaults(defaultConfig);
    }
  }

  public void saveMessages() {
    if (this.messages == null || this.file == null) return;
    try {
      this.get().save(this.file);
    } catch (IOException exception) {
      this.plugin.getLogger().log(Level.SEVERE, "Could not save messages to " + this.file, exception);
    }
  }

  public void saveDefaultMessages() {
    if (this.file == null) this.file = new File(this.plugin.getDataFolder(), "messages.yml");
    if (!this.file.exists()) this.plugin.saveResource("messages.yml", false);
  }
}
