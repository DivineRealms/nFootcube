package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

@Getter
public class PlayerDataManager {

  private final Plugin plugin;
  @Setter private File file;
  @Setter private FileConfiguration data;

  /**
   * Constructs a new PlayerDataManager instance.
   *
   * @param plugin   The main plugin instance.
   * @param playerID The UUID of the player for whom the data is managed.
   */
  public PlayerDataManager(final Plugin plugin, final UUID playerID) {
    this.plugin = plugin;
    this.file = new File(plugin.getDataFolder() + File.separator + "playerdata", playerID + ".yml");
    savePlayerData(playerID);
  }

  /**
   * Reloads the player data file from disk.
   *
   * @param playerID The UUID of the player for whom the data is managed.
   */
  public void reloadPlayerData(final UUID playerID) {
    if (getFile() == null) setFile(new File(getPlugin().getDataFolder(), playerID + ".yml"));
    setData(YamlConfiguration.loadConfiguration(getFile()));
  }

  /**
   * Retrieves a string value from the player data.
   *
   * @param path The path to the string value.
   * @return The string value at the specified path or "null" if not found.
   */
  public String getString(final String path) {
    return getData().getString(path, "null");
  }

  /**
   * Retrieves an integer value from the player data.
   *
   * @param path The path to the integer value.
   * @return The integer value at the specified path or 0 if not found.
   */
  public int getInt(final String path) {
    return getData().getInt(path, 0);
  }

  /**
   * Sets a string value in the player data.
   *
   * @param path  The path to set the string value.
   * @param value The string value to set.
   */
  public void setString(final String path, final String value) {
    getData().set(path, value);
  }

  /**
   * Sets an integer value in the player data.
   *
   * @param path  The path to set the integer value.
   * @param value The integer value to set.
   */
  public void setInt(final String path, final int value) {
    getData().set(path, value);
  }

  /**
   * Increments an integer value in the player data.
   *
   * @param path The path to the integer value.
   */
  public void riseInt(final String path) {
    getData().set(path, getInt(path) + 1);
  }

  /**
   * Gets the FileConfiguration object representing the player data.
   *
   * @param playerID The UUID of the player for whom the data is managed.
   * @return The FileConfiguration object.
   */
  public FileConfiguration getPlayerData(final UUID playerID) {
    if (this.getData() == null) reloadPlayerData(playerID);
    return this.getData();
  }

  /**
   * Saves the player data to disk.
   *
   * @param playerID The UUID of the player for whom the data is managed.
   */
  public void savePlayerData(final UUID playerID) {
    try {
      getPlayerData(playerID).save(getFile());
    } catch (final IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }
}
