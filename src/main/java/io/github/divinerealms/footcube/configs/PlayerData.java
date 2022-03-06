package io.github.divinerealms.footcube.configs;

import io.github.divinerealms.footcube.managers.ConfigManager;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class PlayerData extends ConfigManager {
  public PlayerData(final Plugin plugin, final UUID playerID) {
    super(plugin, playerID + ".yml", false);
  }


}
