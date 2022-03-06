package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public class Config extends ConfigManager {
  @Getter private final String name = "config.yml";
  @Getter @Setter private FileConfiguration config;

  public Config(final Plugin plugin) {
    super(plugin, "config.yml");
  }

  public void reload() {
    reloadConfig(getName());
    setConfig(getConfig(getName()));
  }

  public boolean getBoolean(final String path) {
    return this.getConfig().getBoolean(path, false);
  }

  public int getInt(final String path) {
    return this.getConfig().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return this.getConfig().getDouble(path, 0);
  }

  public String getString(final String path) {
    return this.getConfig().getString(path, getNotFound(path, getName()));
  }

  public Sound getSound(final String path) {
    return Sound.valueOf(getString(path));
  }

  public EntityEffect getEntityEffect(final String path) {
    return EntityEffect.valueOf(getString(path));
  }
}
