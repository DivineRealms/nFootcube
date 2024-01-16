package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

@Getter
public class Config extends ConfigManager {
  private final String name = "config.yml";
  @Setter private FileConfiguration config;
  @Setter private boolean ballsHitsDebug, soundEnabled, cubeEffectEnabled;

  public Config(final Plugin plugin) {
    super(plugin, "");
  }

  public void reload() {
    reloadConfig(getName());
    setConfig(getConfig(getName()));
    setBallsHitsDebug(getBoolean("debug.ball-hits"));
    setSoundEnabled(getBoolean("cube.sounds.enabled"));
    setCubeEffectEnabled(getBoolean("cube.effect.enabled"));
  }

  public boolean getBoolean(final String path) {
    return getConfig().getBoolean(path, false);
  }

  public int getInt(final String path) {
    return getConfig().getInt(path, 0);
  }

  public double getDouble(final String path) {
    return getConfig().getDouble(path, 0);
  }

  public String getString(final String path) {
    return getConfig().getString(path, "Not Found");
  }

  public Sound getSound(final String path) {
    return Sound.valueOf(getString(path));
  }

  public EntityEffect getEntityEffect(final String path) {
    return EntityEffect.valueOf(getString(path));
  }
}