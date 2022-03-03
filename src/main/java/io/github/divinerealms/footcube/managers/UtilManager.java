package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.utils.*;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

public class UtilManager {
  @Getter private final Footcube plugin;
  @Getter private final ConfigManager configManager;
  @Getter private final Chatty chatty;
  @Getter private final Cooldown cooldown;
  @Getter private final Logger logger;
  @Getter private final Physics physics;

  public UtilManager(final Footcube plugin, final ConfigManager configManager) {
    this.plugin = plugin;
    this.configManager = configManager;
    this.chatty = new Chatty(this);
    this.cooldown = new Cooldown(configManager);
    this.logger = new Logger(plugin, this);
    this.physics = new Physics(plugin);
  }

  public void reloadUtils(final FileConfiguration configuration) {
    getChatty().reload(configuration);
    getCooldown().reload(configuration);
    getLogger().reload(configuration);
    getPhysics().reload(configuration);
  }
}
