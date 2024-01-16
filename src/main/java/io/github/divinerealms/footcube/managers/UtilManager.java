package io.github.divinerealms.footcube.managers;

import io.github.divinerealms.footcube.Footcube;
import io.github.divinerealms.footcube.configs.Config;
import io.github.divinerealms.footcube.utils.Cooldown;
import io.github.divinerealms.footcube.utils.Logger;
import io.github.divinerealms.footcube.utils.Physics;
import lombok.Getter;

@Getter
public class UtilManager {
  private final Config config;
  private final Cooldown cooldown;
  private final Logger logger;
  private final Physics physics;

  public UtilManager(final Footcube plugin) {
    this.config = new Config(plugin);
    this.cooldown = new Cooldown(this);
    this.logger = new Logger(plugin);
    this.physics = new Physics(plugin, this);
  }

  public void reloadUtils() {
    getConfig().reload();
    getCooldown().reload();
    getPhysics().reload();
  }
}
