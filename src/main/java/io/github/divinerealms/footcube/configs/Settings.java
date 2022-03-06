package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
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

  public boolean getBoolean(final String path) {
    return getSettings().getBoolean(path, false);
  }

  public int getInt(final String path) {
    return getSettings().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return getSettings().getDouble(path, 0);
  }

  public String getString(final String path) {
    return getSettings().getString(path, getNotFound(path, getName()));
  }

  public Sound getSound(final String path) {
    return Sound.valueOf(getString(path));
  }

  public EntityEffect getEntityEffect(final String path) {
    return EntityEffect.valueOf(getString(path));
  }
}
