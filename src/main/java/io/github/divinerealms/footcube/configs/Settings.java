package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings extends ConfigManager {
  @Getter private FileConfiguration settings;

  public Settings(final Plugin plugin) {
    super(plugin, "settings.yml", true);
  }

  public void reload() {
    reloadConfig("settings.yml");
    this.settings = getConfig("settings.yml");
  }

  public boolean getBoolean(final String path) {
    return getSettings().getBoolean(path, false);
  }

  public int getInt(final String path) {
    return getSettings().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return getSettings().getDouble(path, 0D);
  }

  public String getString(final String path) {
    return getSettings().getString(path, ChatColor.RED + "String \"" + path + "\" not found, check your settings.yml");
  }

  public Sound getSound(final String path) {
    return Sound.valueOf(getString(path));
  }

  public EntityEffect getEntityEffect(final String path) {
    return EntityEffect.valueOf(getString(path));
  }
}
