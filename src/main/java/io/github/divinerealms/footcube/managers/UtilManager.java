package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Messages;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;

public class UtilManager {
  @Getter private final Footcube plugin;
  @Getter private final Messages messages;
  @Getter private final Cooldown cooldown;
  @Getter private final Logger logger;
  @Getter private final Physics physics;

  public UtilManager(final Footcube plugin) {
    this.plugin = plugin;
    this.messages = new Messages(plugin);
    this.cooldown = new Cooldown(plugin, this);
    this.logger = new Logger(plugin, this);
    this.physics = new Physics(plugin);
  }

  public void reloadUtils() {
    getMessages().reload();
    getLogger().reload();
    getCooldown().reload();
    getPhysics().reload();
  }
}
