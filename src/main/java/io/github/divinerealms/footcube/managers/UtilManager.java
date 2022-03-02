package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.utils.*;
import lombok.Getter;

public class UtilManager {
  @Getter private final Footcube plugin;
  @Getter private final ConfigManager configManager;
  @Getter private final Chatty chatty;
  @Getter private final Color color;
  @Getter private final Cooldown cooldown;
  @Getter private final Logger logger;
  @Getter private final Messages messages;
  @Getter private final Physics physics;

  public UtilManager(final Footcube plugin, final ConfigManager configManager) {
    this.plugin = plugin;
    this.configManager = configManager;
    this.chatty = new Chatty(this, configManager);
    this.color = new Color(plugin, configManager);
    this.cooldown = new Cooldown();
    this.logger = new Logger(plugin, this, configManager);
    this.messages = new Messages(this, configManager);
    this.physics = new Physics(plugin);
  }

  public void reloadUtils() {
    getChatty().reload();
    getColor().reload();
    getCooldown().reload(getPlugin(), getConfigManager());
    getLogger().reload();
    getMessages().reload();
    getPhysics().reload();
  }
}
