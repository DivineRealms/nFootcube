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

  public PlayerDataManager(final Plugin plugin, final UUID playerID) {
    this.plugin = plugin;
    this.file = new File(plugin.getDataFolder() + File.separator + "playerdata", playerID + ".yml");
    savePlayerData(playerID);
  }

  public void reloadPlayerData(final UUID playerID) {
    if (getFile() == null) setFile(new File(getPlugin().getDataFolder(), playerID + ".yml"));
    setData(YamlConfiguration.loadConfiguration(getFile()));
  }

  public int getInt(final String path) {
    return getData().getInt(path);
  }

  public void setInt(final String path, final int value) {
    getData().set(path, value);
  }

  public void riseInt(final String path) {
    getData().set(path, getInt(path) + 1);
  }

  public FileConfiguration getPlayerData(final UUID playerID) {
    if (this.getData() == null) reloadPlayerData(playerID);
    return this.getData();
  }

  public void savePlayerData(final UUID playerID) {
    try {
      getPlayerData(playerID).save(getFile());
    } catch (final IOException exception) {
      getPlugin().getLogger().log(Level.SEVERE, "Could not save file to " + getFile(), exception);
    }
  }
}
