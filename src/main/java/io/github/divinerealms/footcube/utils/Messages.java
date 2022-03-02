package io.github.divinerealms.footcube.utils;

import io.github.divinerealms.footcube.managers.ConfigManager;
import io.github.divinerealms.footcube.managers.UtilManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {
  @Getter private final UtilManager utilManager;
  @Getter private final ConfigManager configManager;
  @Getter private final FileConfiguration lang;

  public Messages(final UtilManager utilManager, final ConfigManager configManager) {
    this.utilManager = utilManager;
    this.configManager = configManager;
    this.lang = configManager.getConfig("messages.yml");
  }

  public void reload() {

  }
}
