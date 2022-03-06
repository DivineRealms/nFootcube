package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings extends ConfigManager {
  @Getter private final String name = "settings.yml";
  @Getter @Setter private FileConfiguration settings;

  public Settings(final Plugin plugin) {
    super(plugin, "settings.yml");
  }

  public void reload() {
    reloadConfig(getName());
    setSettings(getConfig(getName()));
  }

  public int getInt(final String path) {
    return this.getSettings().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return this.getSettings().getDouble(path, 0);
  }

  public void setInt(final String path, final int value) {
    this.getSettings().set(path, value);
  }

  public void setDouble(final String path, final double value) {
    this.getSettings().set(path, value);
  }
}
