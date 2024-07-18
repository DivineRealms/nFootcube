package io.github.divinerealms.footcube.managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

@Getter
public class DataManager {
  @Getter private final Plugin plugin;
  @Getter @Setter private String folderName;
  private final Map<String, FileConfiguration> configCache = new HashMap<>();
  private final Queue<ConfigOperation> operationQueue = new LinkedList<>();
  private final Object lock = new Object();
  private File file;

  private final Map<UUID, String> playerUUIDs = new HashMap<>();
  private final Object uuidLock = new Object();

  public DataManager(Plugin plugin) {
    this.plugin = plugin;
    loadPlayerUUIDs();
    new Thread(this::processQueue).start();
  }

  public void createNewFile(String name, String message) {
    reloadConfig(name);
    saveConfig(name);
    loadConfig(name);

    if (message != null) getPlugin().getLogger().info(message);
  }

  public FileConfiguration getConfig(String name) {
    synchronized (lock) {
      try {
        return configCache.computeIfAbsent(name, this::reloadConfig);
      } catch (ConcurrentModificationException e) {
        getPlugin().getLogger().log(Level.SEVERE, "Could not get config " + name, e);
      }
      return null;
    }
  }

  public void setConfig(String folderName, String configName) {
    synchronized (lock) {
      this.folderName = folderName;
      file = new File(getPlugin().getDataFolder() + File.separator + folderName, configName + ".yml");
      configCache.put(configName, YamlConfiguration.loadConfiguration(file));
    }
  }

  public void loadConfig(String name) {
    synchronized (lock) {
      FileConfiguration config = getConfig(name);
      if (config != null) {
        config.options().copyDefaults(true);
        queueSaveConfig(name);
      }
    }
  }

  public FileConfiguration reloadConfig(String name) {
    synchronized (lock) {
      if (file == null) file = new File(getPlugin().getDataFolder() + File.separator + getFolderName(), name + ".yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      configCache.put(name, config);
      return config;
    }
  }

  public void saveConfig(String name) {
    synchronized (lock) {
      FileConfiguration config = getConfig(name);
      if (config != null && file != null) {
        queueSaveConfig(name);
      }
    }
  }

  private void queueSaveConfig(String name) {
    synchronized (lock) {
      FileConfiguration config = getConfig(name);
      operationQueue.add(new ConfigOperation(file, config));
      lock.notify();
    }
  }

  private void processQueue() {
    while (true) {
      ConfigOperation operation;
      synchronized (lock) {
        while (operationQueue.isEmpty()) {
          try {
            lock.wait();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          }
        }
        operation = operationQueue.poll();
      }

      try {
        if (operation != null) {
          operation.getConfig().save(operation.getFile());
        }
      } catch (IOException e) {
        getPlugin().getLogger().log(Level.SEVERE, "Could not save config to " + operation.getFile(), e);
      }
    }
  }

  public boolean deleteFiles(String name) {
    File file = new File(getPlugin().getDataFolder() + File.separator + getFolderName(), name + ".yml");
    return file.delete();
  }

  public boolean configExists(String folderName, String name) {
    File file = new File(getPlugin().getDataFolder() + File.separator + folderName, name + ".yml");
    return file.exists();
  }

  public void copyFile(final String oldFileName, final String newFileName) {
    File oldFile = new File(getPlugin().getDataFolder() + File.separator + getFolderName(), oldFileName + ".yml");
    File newFile = new File(getPlugin().getDataFolder() + File.separator + getFolderName(), newFileName + ".yml");
    FileUtil.copy(oldFile, newFile);
  }

  private void loadPlayerUUIDs() {
    File uuidFile = new File(getPlugin().getDataFolder(), "player_uuids.yml");
    if (!uuidFile.exists()) {
      try {
        uuidFile.createNewFile();
      } catch (IOException e) {
        getPlugin().getLogger().log(Level.SEVERE, "Error creating player_uuids.yml", e);
        return;
      }
    }

    FileConfiguration uuidConfig = YamlConfiguration.loadConfiguration(uuidFile);
    for (String uuidString : uuidConfig.getKeys(false)) {
      try {
        UUID uuid = UUID.fromString(uuidString);
        String playerName = uuidConfig.getString(uuidString);
        playerUUIDs.put(uuid, playerName);
      } catch (IllegalArgumentException e) {
        getPlugin().getLogger().warning("Invalid UUID found in player_uuids.yml: " + uuidString);
      }
    }
  }

  public String getPlayerName(UUID uuid) {
    synchronized (uuidLock) {
      return playerUUIDs.get(uuid);
    }
  }

  public void addPlayerUUID(UUID uuid, String playerName) {
    synchronized (uuidLock) {
      playerUUIDs.put(uuid, playerName);
      savePlayerUUIDs();
    }
  }

  public void removePlayerUUID(UUID uuid) {
    synchronized (uuidLock) {
      playerUUIDs.remove(uuid);
      savePlayerUUIDs();
    }
  }

  private void savePlayerUUIDs() {
    File uuidFile = new File(getPlugin().getDataFolder(), "player_uuids.yml");
    FileConfiguration uuidConfig = YamlConfiguration.loadConfiguration(uuidFile);

    for (Map.Entry<UUID, String> entry : playerUUIDs.entrySet()) {
      uuidConfig.set(entry.getKey().toString(), entry.getValue());
    }

    try {
      uuidConfig.save(uuidFile);
    } catch (IOException e) {
      getPlugin().getLogger().log(Level.SEVERE, "Error saving player_uuids.yml", e);
    }
  }

  @Getter
  private static class ConfigOperation {
    private final File file;
    private final FileConfiguration config;

    public ConfigOperation(File file, FileConfiguration config) {
      this.file = file;
      this.config = config;
    }
  }
}