package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.EntityEffect;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.InputStreamReader;
import java.util.logging.Level;

@Getter
public class Config extends ConfigManager {
  private final String name = "config.yml";
  @Getter @Setter
  private static FileConfiguration configFile;
  @Setter private boolean ballsHitsDebug, soundEnabled, cubeEffectEnabled;

  /**
   * Constructor for the Config class.
   *
   * @param plugin The plugin instance
   */
  public Config(final Plugin plugin) {
    super(plugin, "");
  }

  /**
   * Reloads the configuration file and updates corresponding fields.
   */
  public void reload() {
    // Attempt to reload the configuration file
    reloadConfig(name);

    configFile = getConfig(name);
    if (configFile == null) {
      getPlugin().getLogger().log(Level.WARNING, "Missing config.yml file. Loading default values.");

      // Load default values from the JAR resources
      configFile = YamlConfiguration.loadConfiguration(new InputStreamReader(getPlugin().getResource("config.yml")));
      saveConfig(name);

      // Log the loading of default values
      getPlugin().getLogger().info("Loading default values from JAR resources.");
    } else {
      // Log successful reload
      getPlugin().getLogger().info("Configuration reloaded successfully.");
    }

    setBallsHitsDebug(getBoolean("debug.ball-hits"));
    setSoundEnabled(getBoolean("cube.sounds.enabled"));
    setCubeEffectEnabled(getBoolean("cube.effect.enabled"));
  }

  /**
   * Sets the file configuration for the plugin.
   *
   * @param config The file configuration
   */
  public static void setFile(final FileConfiguration config) {
    configFile = config;
  }

  /**
   * Gets a boolean value from the configuration.
   *
   * @param path The path to the boolean value
   * @return The boolean value
   */
  public boolean getBoolean(final String path) {
    return configFile.getBoolean(path, false);
  }

  /**
   * Gets an integer value from the configuration.
   *
   * @param path The path to the integer value
   * @return The integer value
   */
  public int getInt(final String path) {
    return configFile.getInt(path, 0);
  }

  /**
   * Gets a double value from the configuration.
   *
   * @param path The path to the double value
   * @return The double value
   */
  public double getDouble(final String path) {
    return configFile.getDouble(path, 0);
  }

  /**
   * Gets a string value from the configuration.
   *
   * @param path The path to the string value
   * @return The string value
   */
  public String getString(final String path) {
    return configFile.getString(path, "Not Found");
  }

  /**
   * Gets a Sound enum value from the configuration.
   *
   * @param path The path to the Sound enum value
   * @return The Sound enum value, or null if not found
   */
  public Sound getSound(final String path) {
    String soundName = getValueAsString(path);
    try {
      return soundName != null ? Sound.valueOf(soundName) : Sound.CLICK;
    } catch (IllegalArgumentException e) {
      getPlugin().getLogger().log(Level.WARNING, "Invalid Sound enum value for path: " + path, e);
      return Sound.CLICK;
    }
  }

  /**
   * Gets an EntityEffect enum value from the configuration.
   *
   * @param path The path to the EntityEffect enum value
   * @return The EntityEffect enum value, or EntityEffect.HURT if not found
   */
  public EntityEffect getEntityEffect(final String path) {
    String effectName = getString(path);
    try {
      return effectName != null ? EntityEffect.valueOf(effectName) : EntityEffect.HURT;
    } catch (IllegalArgumentException e) {
      getPlugin().getLogger().log(Level.WARNING, "Invalid EntityEffect value for path: " + path, e);
      return EntityEffect.HURT;
    }
  }

  /**
   * Gets a String value from the configuration, or null if not found.
   *
   * @param path The path to the String value
   * @return The String value, or null if not found
   */
  private String getValueAsString(final String path) {
    return configFile.getString(path);
  }
}
